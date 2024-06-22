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
    public void registerClientReceiver(NetByteBufReceiver receiver) {
        ClientNetworking.registerReceiver(receiver, Networking.getChannelId());
    }

    @Override
    public void registerServerReceiver(NetByteBufReceiver receiver) {
        ServerNetworking.registerReceiver(receiver, Networking.getChannelId());
    }

    @Override
    public void sendToClient(NetByteBuf byteBuf, Player player) {
        ServerNetworking.send(byteBuf, player, Networking.getChannelId());
    }

    public void sendToServer(NetByteBuf byteBuf, Player player) {
        ClientNetworking.send(byteBuf, Networking.getChannelId());
    }

    static class ClientNetworking {

        private static void registerReceiver(NetByteBufReceiver receiver, ResourceLocation channelId) {
            PayloadTypeRegistry.playS2C().register(getType(channelId), getCodec(channelId));
            ClientPlayNetworking.registerGlobalReceiver(getType(channelId), (payload, context) -> {
                receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
            });
        }

        private static void send(NetByteBuf byteBuf, ResourceLocation channelId) {
            ClientPlayNetworking.send(new Payload(getType(channelId), byteBuf));
        }

    }

    static class ServerNetworking {

        private static void registerReceiver(NetByteBufReceiver receiver, ResourceLocation channelId) {
            PayloadTypeRegistry.playC2S().register(getType(channelId), getCodec(channelId));
            ServerPlayNetworking.registerGlobalReceiver(getType(channelId), (payload, context) -> {
                receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
            });
        }

        private static void send(NetByteBuf byteBuf, Player player, ResourceLocation channelId) {
            ServerPlayNetworking.send(player.reference(), new Payload(getType(channelId), byteBuf));
        }
    }

    static Payload.Type<Payload> getType(ResourceLocation channelId) {
        return new CustomPacketPayload.Type<>(channelId.reference());
    }

    static StreamCodec<RegistryFriendlyByteBuf, Payload> getCodec(ResourceLocation channelId) {
        return new StreamCodec<>() {
            @Override
            public Payload decode(RegistryFriendlyByteBuf byteBuf) {
                return new Payload(getType(channelId), new NetByteBuf(byteBuf.readBytes(byteBuf.readableBytes())));
            }

            @Override
            public void encode(RegistryFriendlyByteBuf byteBuf, Payload payload) {
                byteBuf.writeBytes(payload.byteBuf().readBytes(payload.byteBuf().readableBytes()));
            }
        };
    }

    record Payload(Type<Payload> type, NetByteBuf byteBuf) implements CustomPacketPayload {
    }

}
