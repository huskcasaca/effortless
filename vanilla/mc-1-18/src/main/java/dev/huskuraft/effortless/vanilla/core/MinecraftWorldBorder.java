package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.WorldBorder;

public class MinecraftWorldBorder implements WorldBorder {

    private final net.minecraft.world.level.border.WorldBorder reference;

    MinecraftWorldBorder(net.minecraft.world.level.border.WorldBorder reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.world.level.border.WorldBorder referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftWorldBorder obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }


    @Override
    public double getMinX() {
        return reference.getMinX();
    }

    @Override
    public double getMinZ() {
        return reference.getMinZ();
    }

    @Override
    public double getMaxX() {
        return reference.getMaxX();
    }

    @Override
    public double getMaxZ() {
        return reference.getMaxZ();
    }

    @Override
    public double getCenterX() {
        return reference.getCenterX();
    }

    @Override
    public double getCenterZ() {
        return reference.getCenterZ();
    }

    @Override
    public double getSize() {
        return reference.getSize();
    }
}
