package dev.huskuraft.effortless.forge.networking;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;

import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.event.EventNetworkChannel;

import org.apache.commons.lang3.tuple.Pair;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

@AutoService(Networking.class)
public class ForgeNetworking implements Networking {

    private static final Map<ResourceLocation, EventNetworkChannel> MAP = new HashMap<>();

    private static void register(ResourceLocation channelId, Consumer<NetworkEvent> eventConsumer) {
        MAP.computeIfAbsent(channelId, id -> {
            return NetworkRegistry.ChannelBuilder.named(channelId.reference())
                    .networkProtocolVersion(() -> "0")
                    .clientAcceptedVersions(s -> true)
                    .serverAcceptedVersions(s -> true)
                    .eventNetworkChannel();
        }).addListener(eventConsumer);
    }

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        switch (side) {
            case CLIENT -> register(channelId, event -> {
                if (event.getPayload() != null && event.getSource().get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    receiver.receiveBuffer(event.getPayload(), MinecraftPlayer.ofNullable(event.getSource().get().getSender()));
                    event.getSource().get().setPacketHandled(true);
                }
            });
            case SERVER -> register(channelId, event -> {
                if (event.getPayload() != null && event.getSource().get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    receiver.receiveBuffer(event.getPayload(), MinecraftPlayer.ofNullable(event.getSource().get().getSender()));
                    event.getSource().get().setPacketHandled(true);
                }
            });
        }
        return switch (side) {
            case CLIENT ->
                    (byteBuf, player) -> PacketDistributor.SERVER.noArg().send(NetworkDirection.PLAY_TO_SERVER.buildPacket(Pair.of(new FriendlyByteBuf(byteBuf), 0), channelId.reference()).getThis());
            case SERVER ->
                    (byteBuf, player) -> PacketDistributor.PLAYER.with(player::reference).send(NetworkDirection.PLAY_TO_CLIENT.buildPacket(Pair.of(new FriendlyByteBuf(byteBuf), 0), channelId.reference()).getThis());
        };

    }

}
