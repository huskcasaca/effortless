package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.PropertyValue;

public record MinecraftPropertyValue(
        Comparable<?> refs
) implements PropertyValue {

}
