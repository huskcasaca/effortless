package dev.huskuraft.effortless.building;

public enum BuildType {
    BUILD,
    COMMAND,
    PREVIEW,
    PREVIEW_ONCE,
    ;

    public boolean isBuild() {
        return this == BUILD || this == COMMAND;
    }

    public boolean isPreview() {
        return this == PREVIEW || this == PREVIEW_ONCE;
    }

}
