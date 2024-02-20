package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftBuffer;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    @Override
    public void registerClientReceiver(BufferReceiver receiver) {
        ClientPlayNetworking.registerGlobalReceiver(Networking.getChannelId().reference(), (client, handler, buf, responseSender) -> {
            receiver.receiveBuffer(new MinecraftBuffer(buf), new MinecraftPlayer(client.player));
        });
    }

    @Override
    public void registerServerReceiver(BufferReceiver channel) {
        ServerPlayNetworking.registerGlobalReceiver(Networking.getChannelId().reference(), (server, player, handler, buf, responseSender) -> {
            channel.receiveBuffer(new MinecraftBuffer(buf), new MinecraftPlayer(player));
        });
    }

    @Override
    public void sendToClient(Buffer buffer, Player player) {
        ServerPlayNetworking.send(player.reference(), Networking.getChannelId().reference(), buffer.reference());
    }

    public void sendToServer(Buffer buffer, Player player) {
        ClientPlayNetworking.send(Networking.getChannelId().reference(), buffer.reference());
    }

}
