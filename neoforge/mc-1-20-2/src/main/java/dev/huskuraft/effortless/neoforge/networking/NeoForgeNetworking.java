package dev.huskuraft.effortless.neoforge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.INetworkDirection;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.event.EventNetworkChannel;

@AutoService(Networking.class)
public class NeoForgeNetworking implements Networking {

    private EventNetworkChannel CHANNEL;

    private void register(ResourceLocation channelId) {
        CHANNEL = NetworkRegistry.ChannelBuilder.named(channelId.reference())
                .networkProtocolVersion(() -> NetworkRegistry.ACCEPTVANILLA)
                .clientAcceptedVersions(NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA))
                .serverAcceptedVersions(NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA))
                .eventNetworkChannel();
    }

    @Override
    public void registerClientReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        if (CHANNEL == null) register(channelId);
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().getDirection().getReceptionSide().isClient()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(Minecraft.getInstance().player));
            }
        });
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        register(channelId);
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().getDirection().getReceptionSide().isServer()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(event.getSource().getSender()));
            }
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        var packet = PlayNetworkDirection.PLAY_TO_CLIENT.buildPacket(new INetworkDirection.PacketData(new FriendlyByteBuf(byteBuf), 0), channelId.reference());
        PacketDistributor.PLAYER.with(player.reference()).send(packet);
    }

    @Override
    public void sendToServer(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        var packet = PlayNetworkDirection.PLAY_TO_SERVER.buildPacket(new INetworkDirection.PacketData(new FriendlyByteBuf(byteBuf), 0), channelId.reference());
        PacketDistributor.SERVER.noArg().send(packet);
    }

}
