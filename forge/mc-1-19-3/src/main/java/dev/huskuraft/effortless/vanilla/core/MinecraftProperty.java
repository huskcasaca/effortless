package dev.huskuraft.effortless.vanilla.core;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Property;
import dev.huskuraft.effortless.api.core.PropertyValue;

public record MinecraftProperty(
        net.minecraft.world.level.block.state.properties.Property<?> refs
) implements Property {

    public String getName() {
        return refs.getName();
    }

    @Override
    public String getName(PropertyValue value) {
        return refs.getName(value.reference());
    }

    @Override
    public Optional<PropertyValue> getValue(String value) {
        return refs.getValue(value).map(MinecraftPropertyValue::new);
    }

    @Override
    public Collection<PropertyValue> getPossibleValues() {
        return refs.getPossibleValues().stream().map(MinecraftPropertyValue::new).collect(Collectors.toList());
    }

    @Override
    public Class<?> getValueClass() {
        return refs.getValueClass();
    }

}
