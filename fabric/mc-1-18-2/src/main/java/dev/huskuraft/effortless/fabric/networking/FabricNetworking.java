package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {

        switch (side) {
            case CLIENT ->
                    ClientPlayNetworking.registerGlobalReceiver(channelId.reference(), (client, handler, buf, responseSender) -> receiver.receiveBuffer(buf, MinecraftPlayer.ofNullable(null)));
            case SERVER ->
                    ServerPlayNetworking.registerGlobalReceiver(channelId.reference(), (server, player, handler, buf, responseSender) -> receiver.receiveBuffer(buf, MinecraftPlayer.ofNullable(player)));
        }
        return switch (side) {
            case CLIENT ->
                    (byteBuf, player) -> ClientPlayNetworking.send(channelId.reference(), new FriendlyByteBuf(byteBuf));
            case SERVER ->
                    (byteBuf, player) -> ServerPlayNetworking.send(player.reference(), channelId.reference(), new FriendlyByteBuf(byteBuf));
        };

    }

}
