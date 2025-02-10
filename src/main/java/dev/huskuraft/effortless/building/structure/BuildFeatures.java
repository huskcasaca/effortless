package dev.huskuraft.effortless.building.structure;

import java.util.Arrays;

public enum BuildFeatures {
    CIRCLE_START("circle_start", CircleStart.values()),
    CUBE_FILLING("cube_filling", CubeFilling.values()),
    CUBE_LENGTH("cube_length", CubeLength.values()),
    PLANE_FACING("plane_facing", PlaneFacing.values()),
    PLANE_FILLING("plane_filling", PlaneFilling.values()),
    PLANE_LENGTH("plane_length", PlaneLength.values()),
    LINE_DIRECTION("line_direction", LineDirection.values()),
    RAISED_EDGE("raised_edge", RaisedEdge.values()),
    ;

    private final String name;
    private final BuildFeature[] entries;

    BuildFeatures(String name, BuildFeature... defaultEntries) {
        this.name = name;
        this.entries = defaultEntries;
    }

    public String getName() {
        return name;
    }

    public BuildFeature[] getEntries() {
        return entries;
    }

    public BuildFeature getDefaultFeature() {
        return entries[0];
    }

    public boolean isSupported(BuildFeature feature) {
        return Arrays.stream(entries).toList().contains(feature);
    }

}
