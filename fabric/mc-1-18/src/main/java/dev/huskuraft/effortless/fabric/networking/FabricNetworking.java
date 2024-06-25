package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    @Override
    public void registerClientReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        ClientNetworking.registerReceiver(receiver, Networking.getChannelId());
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, NetByteBufReceiver receiver) {
        ServerNetworking.registerReceiver(receiver, Networking.getChannelId());
    }

    @Override
    public void sendToClient(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        ServerNetworking.send(byteBuf, player, Networking.getChannelId());
    }

    public void sendToServer(ResourceLocation channelId, NetByteBuf byteBuf, Player player) {
        ClientNetworking.send(byteBuf, Networking.getChannelId());
    }

    static class ClientNetworking {

        private static void registerReceiver(NetByteBufReceiver receiver, ResourceLocation channelId) {
            ClientPlayNetworking.registerGlobalReceiver(channelId.reference(), (client, handler, buf, responseSender) -> receiver.receiveBuffer(new NetByteBuf(buf), MinecraftPlayer.ofNullable(client.player)));
        }

        private static void send(NetByteBuf byteBuf, ResourceLocation channelId) {
            ClientPlayNetworking.send(channelId.reference(), new FriendlyByteBuf(byteBuf));
        }

    }

    static class ServerNetworking {

        private static void registerReceiver(NetByteBufReceiver receiver, ResourceLocation channelId) {
            ServerPlayNetworking.registerGlobalReceiver(channelId.reference(), (server, player, handler, buf, responseSender) -> receiver.receiveBuffer(new NetByteBuf(buf), MinecraftPlayer.ofNullable(player)));
        }

        private static void send(NetByteBuf byteBuf, Player player, ResourceLocation channelId) {
            ServerPlayNetworking.send(player.reference(), channelId.reference(), new FriendlyByteBuf(byteBuf));
        }

    }

}
