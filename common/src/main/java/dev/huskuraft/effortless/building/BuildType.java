package dev.huskuraft.effortless.building;

public enum BuildType {
    BUILD,
    PREVIEW,
    PREVIEW_ONCE,
    ;

    public boolean isPreview() {
        return this == PREVIEW || this == PREVIEW_ONCE;
    }

}
