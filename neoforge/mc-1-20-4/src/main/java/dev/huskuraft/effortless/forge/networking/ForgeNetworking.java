package dev.huskuraft.effortless.forge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@AutoService(Networking.class)
public class ForgeNetworking implements Networking {

    public static IPayloadRegistrar REGISTRAR;

    @SubscribeEvent
    public void register(RegisterPayloadHandlerEvent event) {
        REGISTRAR = event.registrar(Networking.getChannelId().getNamespace()).optional();
    }

    @Override
    public void registerClientReceiver(NetByteBufReceiver receiver) {
        REGISTRAR.play(Networking.getChannelId().reference(), Wrapper::new, (payload, context) -> {
            if (context.flow().isClientbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player().orElse(null)));
        });
    }

    @Override
    public void registerServerReceiver(NetByteBufReceiver receiver) {
        REGISTRAR.play(Networking.getChannelId().reference(), Wrapper::new, (payload, context) -> {
            if (context.flow().isServerbound()) {
                return;
            }
            receiver.receiveBuffer(new NetByteBuf(payload.byteBuf()), MinecraftPlayer.ofNullable(context.player().orElse(null)));
        });
    }

    @Override
    public void sendToClient(NetByteBuf byteBuf, Player player) {
        PacketDistributor.PLAYER.with(player.reference()).send(new Wrapper(byteBuf));
    }

    @Override
    public void sendToServer(NetByteBuf byteBuf, Player player) {
        PacketDistributor.SERVER.noArg().send(new Wrapper(byteBuf));
    }

    record Wrapper(ByteBuf byteBuf) implements CustomPacketPayload {
        @Override
        public void write(FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeBytes(byteBuf().readBytes(byteBuf().readableBytes()));
        }

        @Override
        public ResourceLocation id() {
            return Networking.getChannelId().reference();
        }
    }

}
