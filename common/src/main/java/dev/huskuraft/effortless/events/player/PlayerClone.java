package dev.huskuraft.effortless.events.player;

import dev.huskuraft.effortless.core.Player;

@FunctionalInterface
public interface PlayerClone {
    void onPlayerClone(Player from, Player to, boolean isDeath);
}
