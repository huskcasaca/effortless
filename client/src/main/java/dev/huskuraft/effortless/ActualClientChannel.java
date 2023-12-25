package dev.huskuraft.effortless;

import dev.huskuraft.effortless.core.ClientEntrance;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.Channel;
import dev.huskuraft.effortless.networking.NetworkRegistry;
import dev.huskuraft.effortless.networking.Packet;
import dev.huskuraft.effortless.packets.AllPacketListener;
import dev.huskuraft.effortless.packets.player.PlayerActionPacket;
import dev.huskuraft.effortless.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.packets.player.PlayerBuildPreviewPacket;
import dev.huskuraft.effortless.packets.player.PlayerSettingsPacket;

final class ActualClientChannel extends Channel<AllPacketListener> {

    private final ClientEntrance entrance;
    private final AllPacketListener listener;

    public ActualClientChannel(Entrance entrance, Resource channelId) {
        super(channelId);
        this.entrance = (ClientEntrance) entrance;
        this.listener = new ClientPacketListener();

        registerPacket(PlayerActionPacket.class, new PlayerActionPacket.Serializer());
        registerPacket(PlayerSettingsPacket.class, new PlayerSettingsPacket.Serializer());
        registerPacket(PlayerBuildPacket.class, new PlayerBuildPacket.Serializer());
        registerPacket(PlayerBuildPreviewPacket.class, new PlayerBuildPreviewPacket.Serializer());

        getEntrance().getEventRegistry().onRegisterNetwork().register(this::onRegisterHandler);
    }

    private ClientEntrance getEntrance() {
        return entrance;
    }

    @Override
    public void receivePacket(Packet packet, Player player) {
        packet.handle(listener, player);
    }

    @Override
    public Buffer allocateButter() {
        return getEntrance().getGamePlatform().newBuffer();
    }

    private void onRegisterHandler(NetworkRegistry registry) {
        setPlatformSender(registry.register(this));
    }

    private class ClientPacketListener implements AllPacketListener {

        @Override
        public void handle(PlayerActionPacket packet, Player player) {
        }

        @Override
        public void handle(PlayerBuildPacket packet, Player player) {
        }

        @Override
        public void handle(PlayerSettingsPacket packet, Player player) {
        }

        @Override
        public void handle(PlayerBuildPreviewPacket packet, Player player) {

            if (getEntrance().getClient().getWorld() == null) {
                return;
            }
            var player1 = getEntrance().getClient().getWorld().getPlayer(packet.playerId());
            if (player1 == null) {
                return;
            }
            getEntrance().getStructureBuilder().onContextReceived(player1, packet.context());
        }
    }

}
