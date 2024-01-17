package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface BlockState extends PlatformReference {

    BlockState mirror(Axis axis);

    BlockState rotate(Revolve revolve);

    boolean isAir();

    Item getItem();

    boolean isReplaceable(Player player, BlockInteraction interaction);

    boolean isReplaceable();

    boolean isDestroyable();
}