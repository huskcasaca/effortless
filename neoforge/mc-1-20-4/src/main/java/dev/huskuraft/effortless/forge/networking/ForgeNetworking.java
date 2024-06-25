package dev.huskuraft.effortless.forge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.forge.platform.ForgeInitializer;
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
public class ForgeNetworking implements Networking {

    public static IPayloadRegistrar REGISTRAR;

    public ForgeNetworking() {
        ForgeInitializer.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void register(RegisterPayloadHandlerEvent event) {
        REGISTRAR = event.registrar(Effortless.MOD_ID).optional();
    }

    @Override
    public void registerClientReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        REGISTRAR.play(channelId.reference(), byteBuf -> new Wrapper(channelId, byteBuf), (payload, context) -> {
            if (context.flow().isClientbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player().orElse(null)));
        });
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        REGISTRAR.play(channelId.reference(), byteBuf -> new Wrapper(channelId, byteBuf), (payload, context) -> {
            if (context.flow().isServerbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player().orElse(null)));
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        PacketDistributor.PLAYER.with(player.reference()).send(new Wrapper(channelId, byteBuf));
    }

    @Override
    public void sendToServer(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        PacketDistributor.SERVER.noArg().send(new Wrapper(channelId, byteBuf));
    }

    record Wrapper(ResourceLocation channelId, ByteBuf byteBuf) implements CustomPacketPayload {
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
