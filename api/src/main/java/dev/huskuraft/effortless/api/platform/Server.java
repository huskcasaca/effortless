package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.PlayerProfile;

public interface Server extends PlatformReference {

    PlayerList getPlayerList();

    void execute(Runnable runnable);

    default boolean isOperator(PlayerProfile profile) {
        return getPlayerList().isOperator(profile);
    }

    boolean isSinglePlayerOwner(PlayerProfile profile);

    boolean isDedicatedServer();

}
