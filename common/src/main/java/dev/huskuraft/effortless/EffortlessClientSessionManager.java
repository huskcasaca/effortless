package dev.huskuraft.effortless;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import dev.huskuraft.effortless.networking.packets.session.SessionConfigPacket;
import dev.huskuraft.effortless.session.Session;
import dev.huskuraft.effortless.session.SessionManager;
import dev.huskuraft.effortless.session.config.GeneralConfig;
import dev.huskuraft.effortless.session.config.SessionConfig;

public final class EffortlessClientSessionManager implements SessionManager {

    private final EffortlessClient entrance;

    private final AtomicReference<Session> serverSession = new AtomicReference<>();
    private final AtomicReference<Session> clientSession = new AtomicReference<>();
    private final AtomicReference<Boolean> isPlayerNotified = new AtomicReference<>(false);

    private final AtomicReference<SessionConfig> serverSessionConfig = new AtomicReference<>();

    public EffortlessClientSessionManager(EffortlessClient entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getClientTickEvent().register(this::onClientTick);
    }

    private EffortlessClient getEntrance() {
        return entrance;
    }


    @Override
    public void onSession(Session session, Player player) {
        serverSession.set(session);
        isPlayerNotified.set(false);
    }

    @Override
    public void onSessionConfig(SessionConfig sessionConfig, Player player) {
        serverSessionConfig.set(sessionConfig);
        getEntrance().getStructureBuilder().onSessionConfig(sessionConfig);
    }

    public void updateSessionConfig(SessionConfig sessionConfig) {
        getEntrance().getChannel().sendPacket(new SessionConfigPacket(sessionConfig));
    }

    public boolean updateGlobalConfig(GeneralConfig generalConfig) {
        var serverSessionConfig = getServerSessionConfig();
        if (serverSessionConfig == null) return false;
        updateSessionConfig(serverSessionConfig.withGlobalConfig(generalConfig));
        return true;
    }

    public boolean updatePlayerConfig(Map<UUID, GeneralConfig> playerConfigs) {
        var serverSessionConfig = getServerSessionConfig();
        if (serverSessionConfig == null) return false;
        updateSessionConfig(serverSessionConfig.withPlayerConfig(playerConfigs));
        return true;
    }

    public boolean updatePlayerConfig(UUID id, GeneralConfig generalConfig) {
        var serverSessionConfig = getServerSessionConfig();
        if (serverSessionConfig == null) return false;
        updateSessionConfig(serverSessionConfig.withPlayerConfig(id, generalConfig));
        return true;
    }

    @Override
    public boolean isServerSessionValid() {
        return serverSession.get() != null;
    }

    @Override
    public SessionStatus getSessionStatus() {
        if (serverSession.get() == null && clientSession.get() == null) {
            return SessionStatus.BOTH_NOT_LOADED;
        }
        if (serverSession.get() == null) {
            return SessionStatus.SERVER_NOT_LOADED;
        }
        if (clientSession.get() == null) {
            return SessionStatus.CLIENT_NOT_LOADED;
        }
        var serverMod = serverSession.get().mods().stream().filter(mod -> mod.getId().equals(Effortless.MOD_ID)).findFirst().orElseThrow();
        var clientMod = clientSession.get().mods().stream().filter(mod -> mod.getId().equals(Effortless.MOD_ID)).findFirst().orElseThrow();

        if (!serverMod.getVersionStr().equals(clientMod.getVersionStr())) {
            return SessionStatus.PROTOCOL_VERSION_MISMATCH;
        }
        return SessionStatus.SUCCESS;
    }

    @Override
    public Session getLastSession() {
        var platform = Platform.getInstance();
        var protocolVersion = Effortless.PROTOCOL_VERSION;
        return new Session(
                platform.getLoaderType(),
                platform.getLoaderVersion(),
                platform.getGameVersion(),
                platform.getRunningMods(),
                protocolVersion

        );
    }

    @Override
    public SessionConfig getLastSessionConfig() {
        return null;
    }

    public Session getServerSession() {
        return serverSession.get();
    }

    public SessionConfig getServerSessionConfig() {
        return serverSessionConfig.get();
    }

    public void notifyPlayer() {
        if (!isPlayerNotified.compareAndSet(false, true)) {
            return;
        }
        var id = TextStyle.GRAY + "[" + Text.translate("effortless.name") + "]" + TextStyle.RESET + " ";
        var message = switch (getSessionStatus()) {
            case SERVER_NOT_LOADED -> TextStyle.RED + "Mod is not found on SERVER side.";
            case CLIENT_NOT_LOADED -> TextStyle.RED + "Mod is not found on CLIENT side.";
            case BOTH_NOT_LOADED -> TextStyle.RED + "Mod not found on SERVER and CLIENT, it cannot happen.";
            case PROTOCOL_VERSION_MISMATCH -> {
                yield TextStyle.WHITE + "Protocol version mismatch! " + TextStyle.GOLD + "Server: [" + serverSession.get().protocolVersion() + "]" + TextStyle.WHITE + ", " + TextStyle.GOLD + "Client: [" + clientSession.get().protocolVersion() + "]";
            }
            case SUCCESS -> TextStyle.WHITE + "Mod found on SERVER and CLIENT, running with loader type " + TextStyle.GOLD + "[" + serverSession.get().loaderType().name() + "]";
        };
        getEntrance().getClientManager().getRunningClient().getPlayer().sendMessage(id + message);
    }

    public void onClientTick(Client client, ClientTick.Phase phase) {
        if (phase == ClientTick.Phase.END) {
            return;
        }

        if (getEntrance().getClient() == null || getEntrance().getClient().getPlayer() == null) {
            serverSession.set(null);
            serverSessionConfig.set(null);
            isPlayerNotified.set(false);
            return;
        }

        var player = getEntrance().getClient().getPlayer();

        if (getEntrance().getStructureBuilder().getContext(player).isDisabled()) {
            return;
        }

        clientSession.set(getLastSession());

        notifyPlayer();

    }

}
