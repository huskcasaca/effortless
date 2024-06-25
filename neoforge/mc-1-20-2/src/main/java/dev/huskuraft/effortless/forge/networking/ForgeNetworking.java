package dev.huskuraft.effortless.forge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.INetworkDirection;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.event.EventNetworkChannel;

@AutoService(Networking.class)
public class ForgeNetworking implements Networking {

    public static final EventNetworkChannel CHANNEL;

    static {
        CHANNEL = NetworkRegistry.ChannelBuilder.named(Networking.getChannelId().reference())
                .networkProtocolVersion(() -> NetworkRegistry.ACCEPTVANILLA)
                .clientAcceptedVersions(NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA))
                .serverAcceptedVersions(NetworkRegistry.acceptMissingOr(NetworkRegistry.ACCEPTVANILLA))
                .eventNetworkChannel();
    }

    @Override
    public void registerClientReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().getDirection().getReceptionSide().isClient()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(Minecraft.getInstance().player));
            }
        });
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().getDirection().getReceptionSide().isServer()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(event.getSource().getSender()));
            }
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        var minecraftPacket = PlayNetworkDirection.PLAY_TO_CLIENT.buildPacket(new INetworkDirection.PacketData(new FriendlyByteBuf(byteBuf), 0), Networking.getChannelId().reference());
        ((ServerPlayer) player.reference()).connection.send(minecraftPacket);
    }

    @Override
    public void sendToServer(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        var minecraftPacket = PlayNetworkDirection.PLAY_TO_SERVER.buildPacket(new INetworkDirection.PacketData(new FriendlyByteBuf(byteBuf), 0), Networking.getChannelId().reference());
        Minecraft.getInstance().getConnection().send(minecraftPacket);
    }

}
