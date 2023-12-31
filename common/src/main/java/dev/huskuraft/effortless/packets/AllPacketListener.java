package dev.huskuraft.effortless.packets;

import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.networking.PacketListener;
import dev.huskuraft.effortless.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.packets.player.PlayerBuildPreviewPacket;
import dev.huskuraft.effortless.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.packets.player.PlayerSettingsPacket;

public interface AllPacketListener extends PacketListener {

    void handle(PlayerCommandPacket packet, Player player);

    void handle(PlayerBuildPacket packet, Player player);

    void handle(PlayerSettingsPacket packet, Player player);

    void handle(PlayerBuildPreviewPacket packet, Player player);

}
