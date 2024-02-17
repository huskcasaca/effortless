package dev.huskuraft.effortless.vanilla.core;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Property;
import dev.huskuraft.effortless.api.core.PropertyValue;

public class MinecraftProperty implements Property {

    private final net.minecraft.world.level.block.state.properties.Property<?> reference;

    public MinecraftProperty(net.minecraft.world.level.block.state.properties.Property<?> reference) {
        this.reference = reference;
    }

    public String getName() {
        return reference.getName();
    }

    @Override
    public String getName(PropertyValue value) {
        return reference.getName(value.reference());
    }

    @Override
    public Optional<PropertyValue> getValue(String value) {
        return reference.getValue(value).map(MinecraftPropertyValue::new);
    }

    @Override
    public Collection<PropertyValue> getPossibleValues() {
        return reference.getPossibleValues().stream().map(MinecraftPropertyValue::new).collect(Collectors.toList());
    }

    @Override
    public Class<?> getValueClass() {
        return reference.getValueClass();
    }

    @Override
    public Object referenceValue() {
        return reference;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftProperty obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
