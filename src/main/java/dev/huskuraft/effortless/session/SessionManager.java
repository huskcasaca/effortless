package dev.huskuraft.effortless.session;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.effortless.session.config.SessionConfig;

public interface SessionManager {

    void onSession(Session session, Player player);

    void onSessionConfig(SessionConfig sessionConfig, Player player);

    boolean isSessionValid();

    SessionStatus getSessionStatus();

    Session getLastSession();

    SessionConfig getLastSessionConfig();

    enum SessionStatus {
        SUCCESS,
        MOD_MISSING,
        SERVER_MOD_MISSING,
        CLIENT_MOD_MISSING,
        PROTOCOL_NOT_MATCH
    }

}
