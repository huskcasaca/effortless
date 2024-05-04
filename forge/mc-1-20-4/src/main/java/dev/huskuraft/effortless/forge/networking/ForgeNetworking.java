package dev.huskuraft.effortless.forge.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import net.minecraftforge.network.NetworkDirection;

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
    public void registerClientReceiver(BufferReceiver receiver) {
        ClientNetworking.registerReceiver(receiver);
    }

    @Override
    public void registerServerReceiver(BufferReceiver receiver) {
        ServerNetworking.registerReceiver(receiver);
    }

    @Override
    public void sendToClient(Buffer buffer, Player player) {
        ServerNetworking.send(buffer, player);
    }

    @Override
    public void sendToServer(Buffer buffer, Player player) {
        ClientNetworking.send(buffer, player);
    }

    static class ClientNetworking {
        public static void registerReceiver(BufferReceiver receiver) {
            CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    receiver.receiveBuffer(new Buffer(event1.getPayload()), MinecraftPlayer.ofNullable(Minecraft.getInstance().player));
                }
            });

        }

        public static void send(Buffer buffer, Player player) {
            var minecraftPacket = NetworkDirection.PLAY_TO_SERVER.buildPacket(new FriendlyByteBuf(buffer), CHANNEL.getName()).getThis();
            Minecraft.getInstance().getConnection().send(minecraftPacket);
        }
    }

    static class ServerNetworking {
        public static void registerReceiver(BufferReceiver receiver) {
            CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    receiver.receiveBuffer(new Buffer(event1.getPayload()), MinecraftPlayer.ofNullable(event1.getSource().getSender()));
                }
            });
        }

        public static void send(Buffer buffer, Player player) {
            var minecraftPacket = NetworkDirection.PLAY_TO_CLIENT.buildPacket(new FriendlyByteBuf(buffer), CHANNEL.getName()).getThis();
            ((ServerPlayer) player.reference()).connection.send(minecraftPacket);
        }
    }

}
