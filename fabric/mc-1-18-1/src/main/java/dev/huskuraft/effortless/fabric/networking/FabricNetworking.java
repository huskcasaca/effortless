package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.networking.MinecraftBuffer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    @Override
    public void registerClientReceiver(BufferReceiver receiver) {
        ClientNetworking.registerReceiver(receiver, Networking.getChannelId());
    }

    @Override
    public void registerServerReceiver(BufferReceiver receiver) {
        ServerNetworking.registerReceiver(receiver, Networking.getChannelId());
    }

    @Override
    public void sendToClient(Buffer buffer, Player player) {
        ServerNetworking.send(buffer, player, Networking.getChannelId());
    }

    public void sendToServer(Buffer buffer, Player player) {
        ClientNetworking.send(buffer, Networking.getChannelId());
    }

    static class ClientNetworking {

        private static void registerReceiver(BufferReceiver receiver, ResourceLocation channelId) {
            ClientPlayNetworking.registerGlobalReceiver(channelId.reference(), (client, handler, buf, responseSender) -> receiver.receiveBuffer(new MinecraftBuffer(buf), new MinecraftPlayer(client.player)));
        }

        private static void send(Buffer buffer, ResourceLocation channelId) {
            ClientPlayNetworking.send(channelId.reference(), buffer.reference());
        }

    }

    static class ServerNetworking {

        private static void registerReceiver(BufferReceiver receiver, ResourceLocation channelId) {
            ServerPlayNetworking.registerGlobalReceiver(channelId.reference(), (server, player, handler, buf, responseSender) -> receiver.receiveBuffer(new MinecraftBuffer(buf), new MinecraftPlayer(player)));
        }

        private static void send(Buffer buffer, Player player, ResourceLocation channelId) {
            ServerPlayNetworking.send(player.reference(), channelId.reference(), buffer.reference());
        }

    }

}
