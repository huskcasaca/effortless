package dev.huskuraft.effortless.api.events.player;

import dev.huskuraft.effortless.api.core.Player;

@FunctionalInterface
public interface PlayerRespawn {
    void onPlayerRespawn(Player oldPlayer, Player newPlayer, boolean alive);
}
