package dev.huskuraft.effortless.api.events.player;

import dev.huskuraft.effortless.api.core.Player;

@FunctionalInterface
public interface PlayerLoggedOut {
    void onPlayerLoggedOut(Player player);
}
