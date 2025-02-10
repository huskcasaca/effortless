package dev.huskuraft.effortless.networking.packets;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.networking.PacketListener;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildTooltipPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerPermissionCheckPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSettingsPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSnapshotCapturePacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSnapshotSharePacket;
import dev.huskuraft.effortless.networking.packets.session.SessionConfigPacket;
import dev.huskuraft.effortless.networking.packets.session.SessionPacket;

public interface AllPacketListener extends PacketListener {

    void handle(PlayerCommandPacket packet, Player player);

    void handle(PlayerSettingsPacket packet, Player player);

    void handle(PlayerBuildPacket packet, Player player);

    void handle(PlayerPermissionCheckPacket packet, Player player);

    void handle(PlayerBuildTooltipPacket packet, Player player);

    void handle(SessionPacket packet, Player player);

    void handle(SessionConfigPacket packet, Player player);

    void handle(PlayerSnapshotCapturePacket packet, Player player);

    void handle(PlayerSnapshotSharePacket packet, Player player);

}
