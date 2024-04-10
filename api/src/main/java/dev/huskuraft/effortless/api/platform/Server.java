package dev.huskuraft.effortless.api.platform;

public interface Server extends PlatformReference {

    PlayerList getPlayerList();

    void execute(Runnable runnable);

}
