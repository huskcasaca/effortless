package dev.huskuraft.effortless.building.structure;

public enum RaisedEdge implements BuildFeature {
    SHORT("raise_short_edge"),
    LONG("raise_long_edge");

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
