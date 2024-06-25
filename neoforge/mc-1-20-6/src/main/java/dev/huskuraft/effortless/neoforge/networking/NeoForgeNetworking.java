package dev.huskuraft.effortless.neoforge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.neoforge.platform.NeoForgeInitializer;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@AutoService(Networking.class)
public class NeoForgeNetworking implements Networking {

    private static PayloadRegistrar REGISTRAR;

    public NeoForgeNetworking() {
        NeoForgeInitializer.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void register(RegisterPayloadHandlersEvent event) {
        REGISTRAR = event.registrar(Effortless.MOD_ID);
    }

    @Override
    public void registerClientReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        REGISTRAR.playBidirectional(getType(channelId), getCodec(channelId), (payload, context) -> {
            if (context.flow().isClientbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player()));
        });
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        REGISTRAR.playToClient(getType(channelId), getCodec(channelId), (payload, context) -> {
            if (context.flow().isServerbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player()));
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        PacketDistributor.sendToPlayer(player.reference(), new Payload(channelId, byteBuf));
    }

    @Override
    public void sendToServer(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        PacketDistributor.sendToServer(new Payload(channelId, byteBuf));
    }

    private static Payload.Type<Payload> getType(ResourceLocation channelId) {
        return new Payload.Type<>(channelId.reference());
    }

    private static StreamCodec<RegistryFriendlyByteBuf, Payload> getCodec(ResourceLocation channelId) {
        return new StreamCodec<>() {
            @Override
            public Payload decode(RegistryFriendlyByteBuf byteBuf) {
                return new Payload(channelId, new NetByteBuf(byteBuf.readBytes(byteBuf.readableBytes())));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf byteBuf, Payload payload) {
                byteBuf.writeBytes(payload.byteBuf().readBytes(payload.byteBuf().readableBytes()));
            }
        };
    }

    private record Payload(ResourceLocation channelId, ByteBuf byteBuf) implements CustomPacketPayload {
        @Override
        public Type<Payload> type() {
            return getType(channelId);
        }
    }

}
