package dev.huskuraft.effortless.forge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.ChannelBuilder;
import net.neoforged.neoforge.network.EventNetworkChannel;
import net.neoforged.neoforge.network.PacketDistributor;

@AutoService(Networking.class)
public class ForgeNetworking implements Networking {

    public static final EventNetworkChannel CHANNEL;

    static {
        var channel = Entrance.getInstance().getChannel();

        CHANNEL = ChannelBuilder.named((ResourceLocation) channel.getChannelId().reference())
                .acceptedVersions((status, version) -> true)
                .optional()
                .networkProtocolVersion(channel.getCompatibilityVersion())
                .eventNetworkChannel();
    }

    @Override
    public void registerClientReceiver(NetByteBufReceiver receiver) {
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().isClientSide()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(Minecraft.getInstance().player));
                event.getSource().setPacketHandled(true);
            }
        });

    }

    @Override
    public void registerServerReceiver(NetByteBufReceiver receiver) {
        CHANNEL.addListener(event -> {
            if (event.getPayload() != null && event.getSource().isServerSide()) {
                receiver.receiveBuffer(new NetByteBuf(event.getPayload()), MinecraftPlayer.ofNullable(event.getSource().getSender()));
                event.getSource().setPacketHandled(true);
            }
        });
    }

    @Override
    public void sendToClient(NetByteBuf byteBuf, Player player) {
        CHANNEL.send(new FriendlyByteBuf(byteBuf), PacketDistributor.PLAYER.with(player.reference()));
    }

    @Override
    public void sendToServer(NetByteBuf byteBuf, Player player) {
        CHANNEL.send(new FriendlyByteBuf(byteBuf), PacketDistributor.SERVER.noArg());
    }

}
