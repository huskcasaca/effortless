package dev.huskuraft.effortless.building.config;

public record RenderConfig(
        boolean showOtherPlayersBuild,
        boolean showOtherPlayersBuildTooltips,
        boolean showBlockPreview,
        int maxRenderVolume,
        int maxRenderDistance
) {

    public static final int MAX_RENDER_VOLUME_MIN = 16;
    public static final int MAX_RENDER_VOLUME_MAX = 4096;

    public RenderConfig() {
        this(
                true,
                true,
                true,
                1024,
                128
        );
    }

}
