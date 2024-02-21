package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.PropertyValue;

public class MinecraftPropertyValue implements PropertyValue {

    private final Comparable<?> reference;

    public MinecraftPropertyValue(Comparable<?> reference) {
        this.reference = reference;
    }

    @Override
    public Object referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftPropertyValue obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
