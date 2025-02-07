package dev.huskuraft.effortless.building.interceptor;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;

public interface BuildInterceptor {

    boolean isEnabled();

    boolean allowInteraction(Player player, World world, BlockPosition blockPosition);

}
