package dev.huskuraft.effortless.building.config;

public record RenderConfig(
        boolean showOtherPlayersBuild,
        boolean showOtherPlayersBuildTooltips,
        boolean showBlockPreview,
        int maxRenderVolume,
        int maxRenderDistance
) {
    public static final int MAX_RENDER_VOLUME_DEFAULT = 1024;
    public static final int MAX_RENDER_VOLUME_MIN = 0;
    public static final int MAX_RENDER_VOLUME_MAX = 4096;

    public RenderConfig() {
        this(
                true,
                true,
                true,
                MAX_RENDER_VOLUME_DEFAULT,
                128
        );
    }

    public static RenderConfig DEFAULT = new RenderConfig();

}
