package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    @Override
    public void registerClientReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        PayloadTypeRegistry.playS2C().register(getType(), getCodec());
        ClientPlayNetworking.registerGlobalReceiver(getType(), (payload, context) -> {
            receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
        });
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        PayloadTypeRegistry.playC2S().register(getType(), getCodec());
        ServerPlayNetworking.registerGlobalReceiver(getType(), (payload, context) -> {
            receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        ServerPlayNetworking.send(player.reference(), new Payload(byteBuf));
    }

    public void sendToServer(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        ClientPlayNetworking.send(new Payload(byteBuf));
    }

    static Payload.Type<Payload> getType() {
        return new CustomPacketPayload.Type<>(Networking.getChannelId().reference());
    }

    static StreamCodec<RegistryFriendlyByteBuf, Payload> getCodec() {
        return new StreamCodec<>() {
            @Override
            public Payload decode(RegistryFriendlyByteBuf byteBuf) {
                return new Payload(new NetByteBuf(byteBuf.readBytes(byteBuf.readableBytes())));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf byteBuf, Payload payload) {
                byteBuf.writeBytes(payload.byteBuf().readBytes(payload.byteBuf().readableBytes()));
            }
        };
    }

    record Payload(NetByteBuf byteBuf) implements CustomPacketPayload {
        @Override
        public Type<Payload> type() {
            return getType();
        }
    }

}
