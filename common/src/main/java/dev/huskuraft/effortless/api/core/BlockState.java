package dev.huskuraft.effortless.api.core;

public abstract class BlockState {

    public abstract BlockState mirror(Axis axis);

    public abstract BlockState rotate(Revolve revolve);

    public abstract boolean isAir();

    public abstract Item getItem();

    public abstract boolean isReplaceable(Player player, BlockInteraction interaction);

    public abstract boolean isReplaceable();

    public abstract boolean isDestroyable();
}