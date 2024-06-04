package dev.huskuraft.effortless.networking.packets;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.PacketListener;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerOperationTooltipPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerPermissionCheckPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSettingsPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSnapshotCapturePacket;
import dev.huskuraft.effortless.networking.packets.session.SessionConfigPacket;
import dev.huskuraft.effortless.networking.packets.session.SessionPacket;

public interface AllPacketListener extends PacketListener {

    void handle(PlayerCommandPacket packet, Player player);

    void handle(PlayerSettingsPacket packet, Player player);

    void handle(PlayerBuildPacket packet, Player player);

    void handle(PlayerPermissionCheckPacket packet, Player player);

    void handle(PlayerOperationTooltipPacket packet, Player player);

    void handle(SessionPacket packet, Player player);

    void handle(SessionConfigPacket packet, Player player);

    void handle(PlayerSnapshotCapturePacket packet, Player player);

}
