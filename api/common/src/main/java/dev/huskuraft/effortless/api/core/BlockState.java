package dev.huskuraft.effortless.api.core;

public interface BlockState {

    BlockState mirror(Axis axis);

    BlockState rotate(Revolve revolve);

    boolean isAir();

    Item getItem();

    boolean isReplaceable(Player player, BlockInteraction interaction);

    boolean isReplaceable();

    boolean isDestroyable();
}