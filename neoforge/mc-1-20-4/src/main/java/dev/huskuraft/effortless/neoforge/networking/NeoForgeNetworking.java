package dev.huskuraft.effortless.neoforge.networking;

import java.lang.reflect.InvocationTargetException;
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
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

@AutoService(Networking.class)
public class NeoForgeNetworking implements Networking {

    private static final Map<ResourceLocation, List<IPayloadHandler<Payload>>> MAP = new HashMap<>();

    private static void register(ResourceLocation channelId, IPayloadHandler<Payload> listener) {
        MAP.computeIfAbsent(channelId, id -> {
            var listeners = new ArrayList<IPayloadHandler<Payload>>();
            var registrar = (IPayloadRegistrar) null;
            try {
                var clazz = Class.forName("net.neoforged.neoforge.network.registration.ModdedPacketRegistrar");
                var constructor = clazz.getDeclaredConstructor(String.class);
                constructor.setAccessible(true);
                registrar = (IPayloadRegistrar) constructor.newInstance(channelId.getNamespace());
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            registrar.play(channelId.reference(), byteBuf -> new Payload(channelId, byteBuf), (payload, context) -> {
                for (var listener1 : listeners) {
                    listener1.handle(payload, context);
                }
            });
            return listeners;
        }).add(listener);
    }

    private record Payload(ResourceLocation channelId, ByteBuf byteBuf) implements CustomPacketPayload {
        @Override
        public void write(FriendlyByteBuf byteBuf) {
            byteBuf.writeBytes(byteBuf.readBytes(byteBuf.readableBytes()));
        }

        @Override
        public net.minecraft.resources.ResourceLocation id() {
            return channelId.reference();
        }
    }

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        switch (side) {
            case CLIENT -> register(channelId, (payload, context) -> {
                if (context.protocol().equals(ConnectionProtocol.PLAY) && context.flow().isClientbound()) {
                    receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player().orElse(null)));
                }
            });
            case SERVER -> register(channelId, (payload, context) -> {
                if (context.protocol().equals(ConnectionProtocol.PLAY) && context.flow().isServerbound()) {
                    receiver.receiveBuffer(payload.byteBuf(), MinecraftPlayer.ofNullable(context.player().orElse(null)));
                }
            });
        }
        return switch (side) {
            case CLIENT -> (byteBuf, player) -> PacketDistributor.SERVER.noArg().send(new Payload(channelId, byteBuf));
            case SERVER ->
                    (byteBuf, player) -> PacketDistributor.PLAYER.with(player.reference()).send(new Payload(channelId, byteBuf));
        };
    }

}
