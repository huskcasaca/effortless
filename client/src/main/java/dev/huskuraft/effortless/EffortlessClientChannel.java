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

final class EffortlessClientChannel extends Channel<AllPacketListener> {

    private final ClientEntrance entrance;
    private final AllPacketListener listener;

    private static final Resource DEFAULT_CHANNEL = Resource.of("default_channel");
    private static final int COMPATIBILITY_VERSION = Effortless.VERSION_NUMBER;

    public EffortlessClientChannel(Entrance entrance) {
        this(entrance, DEFAULT_CHANNEL);
    }

    public EffortlessClientChannel(Entrance entrance, Resource channelId) {
        super(channelId);
        this.entrance = (ClientEntrance) entrance;
        this.listener = new ClientPacketListener();

        registerPacket(PlayerActionPacket.class, new PlayerActionPacket.Serializer());
        registerPacket(PlayerSettingsPacket.class, new PlayerSettingsPacket.Serializer());
        registerPacket(PlayerBuildPacket.class, new PlayerBuildPacket.Serializer());
        registerPacket(PlayerBuildPreviewPacket.class, new PlayerBuildPreviewPacket.Serializer());

        getEntrance().getEventRegistry().getRegisterNetworkEvent().register(this::onRegisterHandler);
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
        return getEntrance().getPlatform().newBuffer();
    }

    @Override
    public int getCompatibilityVersion() {
        return COMPATIBILITY_VERSION;
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
