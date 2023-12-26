package dev.huskuraft.effortless;

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

final class EffortlessServerChannel extends Channel<AllPacketListener> {

    private final Entrance entrance;
    private final AllPacketListener listener;

    public EffortlessServerChannel(Entrance entrance, Resource channelId) {
        super(channelId);
        this.entrance = entrance;
        this.listener = new ServerPacketListener();

        registerPacket(PlayerActionPacket.class, new PlayerActionPacket.Serializer());
        registerPacket(PlayerSettingsPacket.class, new PlayerSettingsPacket.Serializer());
        registerPacket(PlayerBuildPacket.class, new PlayerBuildPacket.Serializer());
        registerPacket(PlayerBuildPreviewPacket.class, new PlayerBuildPreviewPacket.Serializer());

        getEntrance().getEventRegistry().onRegisterNetwork().register(this::onRegisterHandler);
    }

    public Entrance getEntrance() {
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

    private void onRegisterHandler(NetworkRegistry registry) {
        setPlatformSender(registry.register(this));
    }

    private class ServerPacketListener implements AllPacketListener {

        @Override
        public void handle(PlayerActionPacket packet, Player player) {
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
