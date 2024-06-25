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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@AutoService(Networking.class)
public class NeoForgeNetworking implements Networking {

    private IPayloadRegistrar REGISTRAR;

    public NeoForgeNetworking() {
        NeoForgeInitializer.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void register(RegisterPayloadHandlerEvent event) {
        REGISTRAR = event.registrar(Effortless.MOD_ID);
    }

    @Override
    public void registerClientReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        REGISTRAR.play(channelId.reference(), byteBuf -> new Payload(channelId, byteBuf), (payload, context) -> {
            if (context.flow().isClientbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player().orElse(null)));
        });
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        REGISTRAR.play(channelId.reference(), byteBuf -> new Payload(channelId, byteBuf), (payload, context) -> {
            if (context.flow().isServerbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player().orElse(null)));
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        PacketDistributor.PLAYER.with(player.reference()).send(new Payload(channelId, byteBuf));
    }

    @Override
    public void sendToServer(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        PacketDistributor.SERVER.noArg().send(new Payload(channelId, byteBuf));
    }

    record Payload(ResourceLocation channelId, ByteBuf byteBuf) implements CustomPacketPayload {
        @Override
        public void write(FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeBytes(byteBuf().readBytes(byteBuf().readableBytes()));
        }

        @Override
        public net.minecraft.resources.ResourceLocation id() {
            return channelId.reference();
        }
    }

}
