package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.DimensionType;

public record MinecraftDimensionType(
        net.minecraft.world.level.dimension.DimensionType referenceValue
) implements DimensionType {

    @Override
    public boolean hasSkyLight() {
        return referenceValue().hasSkyLight();
    }

    @Override
    public boolean hasCeiling() {
        return referenceValue().hasCeiling();
    }

    @Override
    public double coordinateScale() {
        return referenceValue().coordinateScale();
    }

    @Override
    public int minY() {
        return referenceValue().minY();
    }

    @Override
    public int height() {
        return referenceValue().height();
    }

    @Override
    public int logicalHeight() {
        return referenceValue().logicalHeight();
    }

}
