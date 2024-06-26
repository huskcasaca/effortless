package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;
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

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        record Payload(ResourceLocation channelId, ByteBuf byteBuf) implements CustomPacketPayload {
            @Override
            public Type<Payload> type() {
                return new Type<>(channelId.reference());
            }
        }
        var type = new Payload.Type<Payload>(channelId.reference());
        var codec = new StreamCodec<RegistryFriendlyByteBuf, Payload>() {
            @Override
            public Payload decode(RegistryFriendlyByteBuf byteBuf) {
                return new Payload(channelId, new NetByteBuf(byteBuf.readBytes(byteBuf.readableBytes())));
            }
            @Override
            public void encode(RegistryFriendlyByteBuf byteBuf, Payload payload) {
                byteBuf.writeBytes(payload.byteBuf().readBytes(payload.byteBuf().readableBytes()));
            }
        };
        switch (side) {
            case CLIENT -> {
                PayloadTypeRegistry.playS2C().register(type, codec);
                ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                    receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
                });
            }
            case SERVER -> {
                PayloadTypeRegistry.playC2S().register(type, codec);
                ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                    receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
                });
            }
        }
        return switch (side) {
            case CLIENT -> (byteBuf, player) -> ClientPlayNetworking.send(new Payload(channelId, byteBuf));
            case SERVER -> (byteBuf, player) -> ServerPlayNetworking.send(player.reference(), new Payload(channelId, byteBuf));
        };

    }

}
