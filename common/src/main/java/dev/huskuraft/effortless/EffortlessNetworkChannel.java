package dev.huskuraft.effortless;

import java.util.logging.Logger;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.NetworkChannel;
import dev.huskuraft.effortless.api.networking.NetworkRegistry;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildTooltipPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerPermissionCheckPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSettingsPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerSnapshotCapturePacket;
import dev.huskuraft.effortless.networking.packets.session.SessionConfigPacket;
import dev.huskuraft.effortless.networking.packets.session.SessionPacket;

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

        registerPacket(SessionPacket.class, new SessionPacket.Serializer());
        registerPacket(SessionConfigPacket.class, new SessionConfigPacket.Serializer());

        registerPacket(PlayerCommandPacket.class, new PlayerCommandPacket.Serializer());
        registerPacket(PlayerSettingsPacket.class, new PlayerSettingsPacket.Serializer());
        registerPacket(PlayerBuildPacket.class, new PlayerBuildPacket.Serializer());
        registerPacket(PlayerPermissionCheckPacket.class, new PlayerPermissionCheckPacket.Serializer());
        registerPacket(PlayerBuildTooltipPacket.class, new PlayerBuildTooltipPacket.Serializer());
        registerPacket(PlayerSnapshotCapturePacket.class, new PlayerSnapshotCapturePacket.Serializer());

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
        try {
            packet.handle(listener, player);
        } catch (Exception exception) {
            if (listener.shouldPropagateHandlingExceptions()) {
                throw exception;
            }
            Logger.getAnonymousLogger().severe("Failed to handle packet " + packet + ", suppressing error" + exception);
        }
    }

    @Override
    public int getCompatibilityVersion() {
        return COMPATIBILITY_VERSION;
    }

    private class ServerPacketListener implements AllPacketListener {

        @Override
        public void handle(PlayerCommandPacket packet, Player player) {
            getEntrance().getServer().execute(() -> {
                switch (packet.action()) {
                    case REDO -> getEntrance().getStructureBuilder().redo(player);
                    case UNDO -> getEntrance().getStructureBuilder().undo(player);
                }
            });
        }

        @Override
        public void handle(PlayerSettingsPacket packet, Player player) {
        }

        @Override
        public void handle(PlayerBuildPacket packet, Player player) {
            getEntrance().getServer().execute(() -> {
                getEntrance().getStructureBuilder().onContextReceived(player, packet.context());
            });

        }

        @Override
        public void handle(PlayerPermissionCheckPacket packet, Player player) {
            getEntrance().getServer().execute(() -> {
                var isSinglePlayerOwner = getEntrance().getServerManager().getRunningServer().isSinglePlayerOwner(player.getProfile());
                var isOperator = getEntrance().getServerManager().getRunningServer().isOperator(player.getProfile());
                getEntrance().getChannel().sendPacket(new PlayerPermissionCheckPacket(packet.responseId(), packet.playerId(), isSinglePlayerOwner || isOperator), player);
            });

        }

        @Override
        public void handle(PlayerBuildTooltipPacket packet, Player player) {

        }

        @Override
        public void handle(SessionPacket packet, Player player) {
            getEntrance().getServer().execute(() -> {
                getEntrance().getSessionManager().onSession(packet.session(), player);
            });
        }

        @Override
        public void handle(SessionConfigPacket packet, Player player) {
            getEntrance().getServer().execute(() -> {
                getEntrance().getSessionManager().onSessionConfig(packet.sessionConfig(), player);
            });
        }

        @Override
        public void handle(PlayerSnapshotCapturePacket packet, Player player) {

        }
    }

}
