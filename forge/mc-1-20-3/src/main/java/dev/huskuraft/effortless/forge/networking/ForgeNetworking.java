package dev.huskuraft.effortless.forge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import net.minecraftforge.network.PacketDistributor;

@AutoService(Networking.class)
public class ForgeNetworking implements Networking {

    private EventNetworkChannel CHANNEL;

    private void register(ResourceLocation channelId) {
        CHANNEL = ChannelBuilder.named((net.minecraft.resources.ResourceLocation) channelId.reference())
                .acceptedVersions(Channel.VersionTest.ACCEPT_VANILLA)
                .clientAcceptedVersions(Channel.VersionTest.ACCEPT_VANILLA)
                .serverAcceptedVersions(Channel.VersionTest.ACCEPT_VANILLA)
                .eventNetworkChannel();
    }

    @Override
    public void registerClientReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        if (CHANNEL == null) register(channelId);
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().isClientSide()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(Minecraft.getInstance().player));
                event.getSource().setPacketHandled(true);
            }
        });

    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        if (CHANNEL == null) register(channelId);
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().isServerSide()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(event.getSource().getSender()));
                event.getSource().setPacketHandled(true);
            }
        });
    }

    @Override
    public void sendToClient(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        CHANNEL.send(new FriendlyByteBuf(byteBuf), PacketDistributor.PLAYER.with(player.reference()));
    }

    @Override
    public void sendToServer(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        CHANNEL.send(new FriendlyByteBuf(byteBuf), PacketDistributor.SERVER.noArg());
    }

}
