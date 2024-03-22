package dev.huskuraft.effortless.session;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.session.config.SessionConfig;

public interface SessionManager {

    void onSession(Session session, Player player);

    void onSessionConfig(SessionConfig sessionConfig, Player player);

    boolean isServerSessionValid();

    SessionStatus getSessionStatus();

    Session getLastSession();

    SessionConfig getLastSessionConfig();

    enum SessionStatus {
        SERVER_NOT_LOADED,
        CLIENT_NOT_LOADED,
        BOTH_NOT_LOADED,
        PROTOCOL_VERSION_MISMATCH,
        SUCCESS
    }

}
