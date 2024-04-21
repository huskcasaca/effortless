package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.DimensionType;

public class MinecraftDimensionType implements DimensionType {

    private final net.minecraft.world.level.dimension.DimensionType reference;

    MinecraftDimensionType(net.minecraft.world.level.dimension.DimensionType reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.world.level.dimension.DimensionType referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftDimensionType obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public boolean hasSkyLight() {
        return reference.hasSkyLight();
    }

    @Override
    public boolean hasCeiling() {
        return reference.hasCeiling();
    }

    @Override
    public double coordinateScale() {
        return reference.coordinateScale();
    }

    @Override
    public int minY() {
        return reference.minY();
    }

    @Override
    public int height() {
        return reference.height();
    }

    @Override
    public int logicalHeight() {
        return reference.logicalHeight();
    }


}
