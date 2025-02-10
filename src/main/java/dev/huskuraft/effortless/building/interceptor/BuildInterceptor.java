package dev.huskuraft.effortless.building.interceptor;

import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.core.World;

public interface BuildInterceptor {

    boolean isEnabled();

    boolean allowInteraction(Player player, World world, BlockPosition blockPosition);

}
