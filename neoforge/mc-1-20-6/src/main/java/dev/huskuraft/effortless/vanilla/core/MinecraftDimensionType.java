package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.DimensionType;

public record MinecraftDimensionType(
        net.minecraft.world.level.dimension.DimensionType refs
) implements DimensionType {

    @Override
    public boolean hasSkyLight() {
        return refs.hasSkyLight();
    }

    @Override
    public boolean hasCeiling() {
        return refs.hasCeiling();
    }

    @Override
    public double coordinateScale() {
        return refs.coordinateScale();
    }

    @Override
    public int minY() {
        return refs.minY();
    }

    @Override
    public int height() {
        return refs.height();
    }

    @Override
    public int logicalHeight() {
        return refs.logicalHeight();
    }

}
