package dev.huskuraft.effortless.events.player;

import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.World;

@FunctionalInterface
public interface PlayerChangeWorld {
    void onPlayerChangeWorld(Player player, World origin, World destination);
}
