package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetworkChannel;
import dev.huskuraft.effortless.api.networking.NetworkRegistry;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPreviewPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSettingsPacket;
import dev.huskuraft.effortless.networking.packets.session.SessionStartPacket;

public final class EffortlessNetworkChannel extends NetworkChannel<AllPacketListener> {

    private static final ResourceLocation DEFAULT_CHANNEL = ResourceLocation.of(Effortless.MOD_ID, "default_channel");
    private static final int COMPATIBILITY_VERSION = Effortless.PROTOCOL_VERSION;
    private final Effortless entrance;
    private final AllPacketListener listener;

    public EffortlessNetworkChannel(Effortless entrance) {
        this(entrance, DEFAULT_CHANNEL);
    }

    public EffortlessNetworkChannel(Effortless entrance, ResourceLocation channelId) {
        super(channelId, Side.SERVER);
        this.entrance = entrance;
        this.listener = new ServerPacketListener();

        registerPacket(PlayerCommandPacket.class, new PlayerCommandPacket.Serializer());
        registerPacket(PlayerSettingsPacket.class, new PlayerSettingsPacket.Serializer());
        registerPacket(PlayerBuildPacket.class, new PlayerBuildPacket.Serializer());
        registerPacket(PlayerBuildPreviewPacket.class, new PlayerBuildPreviewPacket.Serializer());
        registerPacket(SessionStartPacket.class, new SessionStartPacket.Serializer());

        getEntrance().getEventRegistry().getRegisterNetworkEvent().register(this::onRegisterNetwork);
    }

    private void onRegisterNetwork(NetworkRegistry registry) {
        getPlatformChannel().registerServerReceiver(this);

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
    public int getCompatibilityVersion() {
        return COMPATIBILITY_VERSION;
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

        @Override
        public void handle(SessionStartPacket packet, Player player) {

        }
    }

}
