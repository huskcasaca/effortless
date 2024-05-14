package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.api.text.Text;

public enum PositionType implements BuildModifier {
    ABSOLUTE("absolute"),
    RELATIVE("relative"),
    RELATIVE_ONCE("relative_once");

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
        return this == RELATIVE_ONCE;
    }

    @Override
    public BuildStage getStage() {
        return switch (this) {
            case ABSOLUTE -> BuildStage.TICK;
            case RELATIVE -> BuildStage.INTERACT;
            case RELATIVE_ONCE -> BuildStage.UPDATE_CONTEXT;
        };
    }

}
