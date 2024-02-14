package dev.huskuraft.effortless.api.events.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;

@FunctionalInterface
public interface PlayerChangeWorld {
    void onPlayerChangeWorld(Player player, World origin, World destination);
}
