package dev.huskuraft.effortless.building.structure;

import dev.huskuraft.effortless.building.SingleSelectFeature;

public enum RaisedEdge implements SingleSelectFeature {

    RAISE_SHORT_EDGE("raise_short_edge"),
    RAISE_LONG_EDGE("raise_long_edge");

    private final String name;

    RaisedEdge(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return BuildFeature.RAISED_EDGE.getName();
    }
}
