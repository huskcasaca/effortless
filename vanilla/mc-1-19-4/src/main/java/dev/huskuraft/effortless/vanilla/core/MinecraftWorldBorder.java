package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.WorldBorder;

public record MinecraftWorldBorder(
        net.minecraft.world.level.border.WorldBorder refs
) implements WorldBorder {

    @Override
    public double getMinX() {
        return refs.getMinX();
    }

    @Override
    public double getMinZ() {
        return refs.getMinZ();
    }

    @Override
    public double getMaxX() {
        return refs.getMaxX();
    }

    @Override
    public double getMaxZ() {
        return refs.getMaxZ();
    }

    @Override
    public double getCenterX() {
        return refs.getCenterX();
    }

    @Override
    public double getCenterZ() {
        return refs.getCenterZ();
    }

    @Override
    public double getSize() {
        return refs.getSize();
    }
}
