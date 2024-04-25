package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.Feature;

public enum BuildFeature {
    CIRCLE_START("circle_start", CircleStart.values()),
    CUBE_FILLING("cube_filling", CubeFilling.values()),
    PLANE_FACING("plane_facing", PlaneFacing.values()),
    PLANE_FILLING("plane_filling", PlaneFilling.values()),
    RAISED_EDGE("raised_edge", RaisedEdge.values()),
    UNIFORM_LENGTH("uniform_length", PlaneLength.values()),
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
