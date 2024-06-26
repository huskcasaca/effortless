package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    @Override
    public void registerClientReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        ClientPlayNetworking.registerGlobalReceiver(channelId.reference(), (client, handler, buf, responseSender) -> receiver.receiveBuffer(new NetByteBuf(buf), MinecraftPlayer.ofNullable(null)));
    }

    @Override
    public void registerServerReceiver(ResourceLocation channelId, ByteBufReceiver receiver) {
        ServerPlayNetworking.registerGlobalReceiver(channelId.reference(), (server, player, handler, buf, responseSender) -> receiver.receiveBuffer(new NetByteBuf(buf), MinecraftPlayer.ofNullable(player)));
    }

    @Override
    public void sendToClient(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        ServerPlayNetworking.send(player.reference(), channelId.reference(), new FriendlyByteBuf(byteBuf));
    }

    public void sendToServer(ResourceLocation channelId, ByteBuf byteBuf, Player player) {
        ClientPlayNetworking.send(channelId.reference(), new FriendlyByteBuf(byteBuf));
    }

}
