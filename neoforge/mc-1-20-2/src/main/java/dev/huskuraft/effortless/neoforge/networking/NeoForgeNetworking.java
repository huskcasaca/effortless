package dev.huskuraft.effortless.neoforge.networking;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.INetworkDirection;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.event.EventNetworkChannel;

@AutoService(Networking.class)
public class NeoForgeNetworking implements Networking {

    private static final Map<ResourceLocation, EventNetworkChannel> MAP = new HashMap<>();

    private static void register(ResourceLocation channelId, Consumer<NetworkEvent> eventConsumer) {
        MAP.computeIfAbsent(channelId, id -> {
            return NetworkRegistry.ChannelBuilder.named(channelId.reference())
                    .networkProtocolVersion(NetworkRegistry.ACCEPTVANILLA::toString)
                    .clientAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
                    .serverAcceptedVersions(NetworkRegistry.ACCEPTVANILLA::equals)
                    .eventNetworkChannel();
        }).addListener(eventConsumer);
    }

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        switch (side) {
            case CLIENT -> register(channelId, event -> {
                if (event.getPayload() != null && event.getSource().getDirection().equals(PlayNetworkDirection.PLAY_TO_CLIENT)) {
                    receiver.receiveBuffer(event.getPayload(), MinecraftPlayer.ofNullable(event.getSource().getSender()));
                    event.getSource().setPacketHandled(true);
                }
            });
            case SERVER -> register(channelId, event -> {
                if (event.getPayload() != null && event.getSource().getDirection().equals(PlayNetworkDirection.PLAY_TO_SERVER)) {
                    receiver.receiveBuffer(event.getPayload(), MinecraftPlayer.ofNullable(event.getSource().getSender()));
                    event.getSource().setPacketHandled(true);
                }
            });
        }
        return switch (side) {
            case CLIENT ->
                    (byteBuf, player) -> PacketDistributor.SERVER.noArg().send(PlayNetworkDirection.PLAY_TO_SERVER.buildPacket(new INetworkDirection.PacketData(new FriendlyByteBuf(byteBuf), 0), channelId.reference()));
            case SERVER ->
                    (byteBuf, player) -> PacketDistributor.PLAYER.with(player::reference).send(PlayNetworkDirection.PLAY_TO_CLIENT.buildPacket(new INetworkDirection.PacketData(new FriendlyByteBuf(byteBuf), 0), channelId.reference()));
        };

    }

}
