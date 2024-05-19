package dev.huskuraft.effortless.vanilla.core;

import java.util.Map;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.FluidState;
import dev.huskuraft.effortless.api.core.Property;
import dev.huskuraft.effortless.api.core.PropertyValue;

public record MinecraftFluidState(net.minecraft.world.level.material.FluidState referenceValue) implements FluidState {

    public static FluidState ofNullable(net.minecraft.world.level.material.FluidState value) {
        return value == null ? null : new MinecraftFluidState(value);
    }

    @Override
    public Map<Property, PropertyValue> getPropertiesMap() {
        return referenceValue().getValues().entrySet().stream().collect(Collectors.toMap(entry -> new MinecraftProperty(entry.getKey()), entry -> new MinecraftPropertyValue(entry.getValue())));
    }

    @Override
    public BlockState createLegacyBlock() {
        return MinecraftBlockState.ofNullable(referenceValue().createLegacyBlock());
    }
}
