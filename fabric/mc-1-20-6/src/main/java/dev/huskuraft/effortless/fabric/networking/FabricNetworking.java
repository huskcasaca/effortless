package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    @Override
    public void registerClientReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        PayloadTypeRegistry.playS2C().register(getType(channelId), getCodec(channelId));
        ClientPlayNetworking.registerGlobalReceiver(getType(channelId), (payload, context) -> {
            receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
        });
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        PayloadTypeRegistry.playC2S().register(getType(channelId), getCodec(channelId));
        ServerPlayNetworking.registerGlobalReceiver(getType(channelId), (payload, context) -> {
            receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        ServerPlayNetworking.send(player.reference(), new Payload(channelId, byteBuf));
    }

    public void sendToServer(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        ClientPlayNetworking.send(new Payload(channelId, byteBuf));
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
