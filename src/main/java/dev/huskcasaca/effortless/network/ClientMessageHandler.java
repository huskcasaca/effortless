package dev.huskcasaca.effortless.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
public interface ClientMessageHandler<M extends Message> {

    void handleClientSide(Minecraft client, LocalPlayer player, ClientPacketListener handler, M message, PacketSender responseSender);

}
