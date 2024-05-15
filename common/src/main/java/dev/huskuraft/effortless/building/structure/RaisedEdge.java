package dev.huskuraft.effortless.building.structure;

public enum RaisedEdge implements BuildFeature {

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
    public BuildFeatures getType() {
        return BuildFeatures.RAISED_EDGE;
    }
}
