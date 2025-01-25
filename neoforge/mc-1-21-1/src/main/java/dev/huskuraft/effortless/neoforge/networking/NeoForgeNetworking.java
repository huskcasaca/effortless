package dev.huskuraft.effortless.neoforge.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@AutoService(Networking.class)
public class NeoForgeNetworking implements Networking {

    private static final Map<CustomPacketPayload.Type<Payload>, List<IPayloadHandler<Payload>>> MAP = new HashMap<>();

    private static void register(CustomPacketPayload.Type<Payload> type, IPayloadHandler<Payload> listener) {
        MAP.computeIfAbsent(type, id -> {
            var codec = new StreamCodec<RegistryFriendlyByteBuf, Payload>() {
                @Override
                public Payload decode(RegistryFriendlyByteBuf byteBuf) {
                    return new Payload(type, byteBuf.readBytes(byteBuf.readableBytes()));
                }

                @Override
                public void encode(RegistryFriendlyByteBuf byteBuf, Payload payload) {
                    byteBuf.writeBytes(payload.byteBuf());
                }
            };
            var listeners = new ArrayList<IPayloadHandler<Payload>>();
            var version = String.valueOf(Effortless.PROTOCOL_VERSION);
            new PayloadRegistrar(version).playBidirectional(type, codec, (payload, context) -> {
                for (var listener1 : listeners) {
                    listener1.handle(payload, context);
                }
            });
            return listeners;
        }).add(listener);
    }

    private record Payload(Type<Payload> type, ByteBuf byteBuf) implements CustomPacketPayload {

        @Override
        public ByteBuf byteBuf() {
            return byteBuf.duplicate();
        }

    }

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        var type = new Payload.Type<Payload>(channelId.reference());
        switch (side) {
            case CLIENT -> register(type, (payload, context) -> {
                if (context.protocol().equals(ConnectionProtocol.PLAY) && context.flow().isClientbound()) {
                    receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
                }
            });
            case SERVER -> register(type, (payload, context) -> {
                if (context.protocol().equals(ConnectionProtocol.PLAY) && context.flow().isServerbound()) {
                    receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
                }
            });
        }
        return switch (side) {
            case CLIENT -> (byteBuf, player) -> PacketDistributor.sendToServer(new Payload(type, byteBuf));
            case SERVER ->
                    (byteBuf, player) -> PacketDistributor.sendToPlayer(player.reference(), new Payload(type, byteBuf));
        };
    }

}
