package dev.huskuraft.effortless.api.core;

import java.util.Collection;
import java.util.Optional;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Property extends PlatformReference {

    String getName();

    String getName(PropertyValue value);

    Optional<PropertyValue> getValue(String value);

    Collection<PropertyValue> getPossibleValues();

    Class<?> getValueClass();

}
