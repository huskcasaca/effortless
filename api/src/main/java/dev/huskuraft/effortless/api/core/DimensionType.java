package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface DimensionType extends PlatformReference {

    boolean hasSkyLight();

    boolean hasCeiling();

    double coordinateScale();

    int minY();

    int height();

    int logicalHeight();


}
