package dev.huskuraft.effortless;

import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.Channel;
import dev.huskuraft.effortless.networking.NetworkRegistry;
import dev.huskuraft.effortless.networking.Packet;
import dev.huskuraft.effortless.packets.AllPacketListener;
import dev.huskuraft.effortless.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.packets.player.PlayerBuildPreviewPacket;
import dev.huskuraft.effortless.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.packets.player.PlayerSettingsPacket;

final class EffortlessClientChannel extends Channel<AllPacketListener> {

    private final EffortlessClient entrance;
    private final AllPacketListener listener;

    private static final Resource DEFAULT_CHANNEL = Resource.of("default_channel");
    private static final int COMPATIBILITY_VERSION = Effortless.VERSION_NUMBER;

    public EffortlessClientChannel(EffortlessClient entrance) {
        this(entrance, DEFAULT_CHANNEL);
    }

    public EffortlessClientChannel(EffortlessClient entrance, Resource channelId) {
        super(channelId);
        this.entrance = entrance;
        this.listener = new ClientPacketListener();

        registerPacket(PlayerCommandPacket.class, new PlayerCommandPacket.Serializer());
        registerPacket(PlayerSettingsPacket.class, new PlayerSettingsPacket.Serializer());
        registerPacket(PlayerBuildPacket.class, new PlayerBuildPacket.Serializer());
        registerPacket(PlayerBuildPreviewPacket.class, new PlayerBuildPreviewPacket.Serializer());

        getEntrance().getEventRegistry().getRegisterNetworkEvent().register(this::onRegisterHandler);
    }

    private EffortlessClient getEntrance() {
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
        public void handle(PlayerCommandPacket packet, Player player) {
            switch (packet.action()) {
                case REDO, UNDO -> {
                    // noop
                }
                case RESET_BUILD_STATE -> getEntrance().getStructureBuilder().resetBuildState(player);
            }
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
