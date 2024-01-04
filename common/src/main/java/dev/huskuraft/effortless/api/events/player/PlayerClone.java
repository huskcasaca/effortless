package dev.huskuraft.effortless.api.events.player;

import dev.huskuraft.effortless.api.core.Player;

@FunctionalInterface
public interface PlayerClone {
    void onPlayerClone(Player from, Player to, boolean isDeath);
}
