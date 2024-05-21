package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.api.text.Text;

public enum PositionType implements BuildModifier {
    ABSOLUTE("absolute"),
    RELATIVE("relative");

    private final String name;

    PositionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Text getDisplayName() {
        return Text.translate("effortless.position.%s".formatted(name));
    }

    @Override
    public boolean isIntermediate() {
        return this == RELATIVE;
    }

    @Override
    public BuildStage getStage() {
        return switch (this) {
            case ABSOLUTE -> BuildStage.TICK;
            case RELATIVE -> BuildStage.UPDATE_CONTEXT;
        };
    }

}
