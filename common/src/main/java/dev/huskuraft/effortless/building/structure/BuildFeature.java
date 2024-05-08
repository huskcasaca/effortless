package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.Feature;

public enum BuildFeature {
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
    private final Feature[] entries;

    BuildFeature(String name, Feature... defaultEntries) {
        this.name = name;
        this.entries = defaultEntries;
    }

    public String getName() {
        return name;
    }

    public Feature[] getEntries() {
        return entries;
    }

}
