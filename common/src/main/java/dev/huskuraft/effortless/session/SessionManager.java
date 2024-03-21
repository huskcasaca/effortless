package dev.huskuraft.effortless.session;

public interface SessionManager {

    void onServerSession(Session session);

    boolean isServerSessionValid();

    SessionStatus getSessionStatus();

    Session getLastSession();

    enum SessionStatus {
        SERVER_NOT_LOADED,
        CLIENT_NOT_LOADED,
        BOTH_NOT_LOADED,
        PROTOCOL_VERSION_MISMATCH,
        SUCCESS
    }

}
