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

final class EffortlessServerChannel extends Channel<AllPacketListener> {

    private final Effortless entrance;
    private final AllPacketListener listener;

    private static final Resource DEFAULT_CHANNEL = Resource.of("default_channel");
    private static final int COMPATIBILITY_VERSION = Effortless.VERSION_NUMBER;

    public EffortlessServerChannel(Effortless entrance) {
        this(entrance, DEFAULT_CHANNEL);
    }

    public EffortlessServerChannel(Effortless entrance, Resource channelId) {
        super(channelId);
        this.entrance = entrance;
        this.listener = new ServerPacketListener();

        registerPacket(PlayerCommandPacket.class, new PlayerCommandPacket.Serializer());
        registerPacket(PlayerSettingsPacket.class, new PlayerSettingsPacket.Serializer());
        registerPacket(PlayerBuildPacket.class, new PlayerBuildPacket.Serializer());
        registerPacket(PlayerBuildPreviewPacket.class, new PlayerBuildPreviewPacket.Serializer());

        getEntrance().getEventRegistry().getRegisterNetworkEvent().register(this::onRegisterHandler);
    }

    public Effortless getEntrance() {
        return entrance;
    }

    @Override
    public void receivePacket(Packet packet, Player player) {
        player.getServer().execute(() -> {
            try {
                packet.handle(listener, player);
            } catch (Exception exception) {
                if (listener.shouldPropagateHandlingExceptions()) {
                    throw exception;
                }
                LOGGER.severe("Failed to handle packet " + packet + ", suppressing error" + exception);
            }
        });
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

    private class ServerPacketListener implements AllPacketListener {

        @Override
        public void handle(PlayerCommandPacket packet, Player player) {
            switch (packet.action()) {
                case REDO -> getEntrance().getStructureBuilder().redo(player);
                case UNDO -> getEntrance().getStructureBuilder().undo(player);
                case RESET_BUILD_STATE -> getEntrance().getStructureBuilder().resetBuildState(player);
            }
        }

        @Override
        public void handle(PlayerBuildPacket packet, Player player) {
            getEntrance().getStructureBuilder().onContextReceived(player, packet.context());
        }

        @Override
        public void handle(PlayerSettingsPacket packet, Player player) {
        }

        @Override
        public void handle(PlayerBuildPreviewPacket packet, Player player) {

        }
    }

}
