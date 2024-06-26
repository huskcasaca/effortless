package dev.huskuraft.effortless.neoforge.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@AutoService(Networking.class)
public class NeoForgeNetworking implements Networking {

    private static final Map<ResourceLocation, List<IPayloadHandler<Payload>>> MAP = new HashMap<>();

    private static void register(ResourceLocation channelId, IPayloadHandler<Payload> listener) {
        MAP.computeIfAbsent(channelId, c -> {
            var listeners = new ArrayList<IPayloadHandler<Payload>>();
            new PayloadRegistrar("0").playBidirectional(new Payload.Type<>(channelId.reference()), new StreamCodec<RegistryFriendlyByteBuf, Payload>() {
                @Override
                public Payload decode(RegistryFriendlyByteBuf byteBuf) {
                    return new Payload(channelId, byteBuf.readBytes(byteBuf.readableBytes()));
                }

                @Override
                public void encode(RegistryFriendlyByteBuf byteBuf, Payload payload) {
                    byteBuf.writeBytes(payload.byteBuf().readBytes(payload.byteBuf().readableBytes()));
                }
            }, (payload, context) -> {
                for (var listener1 : listeners) {
                    listener1.handle(payload, context);
                }
            });
            return listeners;
        }).add(listener);
    }

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        switch (side) {
            case CLIENT -> register(channelId, (payload, context) -> {
                if (context.flow().isClientbound()) {
                    return;
                }
                receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
            });
            case SERVER -> register(channelId, (payload, context) -> {
                if (context.flow().isServerbound()) {
                    return;
                }
                receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player()));
            });
        }
        return switch (side) {
            case CLIENT -> (byteBuf, player) -> PacketDistributor.sendToServer(new Payload(channelId, byteBuf));
            case SERVER -> (byteBuf, player) -> PacketDistributor.sendToPlayer(player.reference(), new Payload(channelId, byteBuf));
        };
    }

    private record Payload(ResourceLocation channelId, ByteBuf byteBuf) implements CustomPacketPayload {
        @Override
        public Type<Payload> type() {
            return new Type<>(channelId.reference());
        }
    }

}
