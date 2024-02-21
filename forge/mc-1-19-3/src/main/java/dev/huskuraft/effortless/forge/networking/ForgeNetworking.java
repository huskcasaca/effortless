package dev.huskuraft.effortless.forge.networking;

import org.apache.commons.lang3.tuple.Pair;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.networking.MinecraftBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;

@AutoService(Networking.class)
public class ForgeNetworking implements Networking {

    public static final EventNetworkChannel CHANNEL;

    static {
        CHANNEL = NetworkRegistry.ChannelBuilder.named(Networking.getChannelId().reference())
                .clientAcceptedVersions(Networking.getCompatibilityVersionStr()::equals)
                .serverAcceptedVersions(Networking.getCompatibilityVersionStr()::equals)
                .networkProtocolVersion(Networking::getCompatibilityVersionStr)
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
                if (event1.getPayload() != null && event1.getSource().get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    receiver.receiveBuffer(new MinecraftBuffer(event1.getPayload()), MinecraftPlayer.ofNullable(Minecraft.getInstance().player));
                }
            });

        }

        public static void send(Buffer buffer, Player player) {
            var minecraftPacket = NetworkDirection.PLAY_TO_SERVER.buildPacket(Pair.of(buffer.reference(), -1), Networking.getChannelId().reference()).getThis();
            Minecraft.getInstance().getConnection().send(minecraftPacket);
        }
    }

    static class ServerNetworking {
        public static void registerReceiver(BufferReceiver receiver) {
            CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    receiver.receiveBuffer(new MinecraftBuffer(event1.getPayload()), MinecraftPlayer.ofNullable(event1.getSource().get().getSender()));
                }
            });
        }

        public static void send(Buffer buffer, Player player) {
            var minecraftPacket = NetworkDirection.PLAY_TO_CLIENT.buildPacket(Pair.of(buffer.reference(), -1), Networking.getChannelId().reference()).getThis();
            ((ServerPlayer) player.reference()).connection.send(minecraftPacket);
        }
    }

}
