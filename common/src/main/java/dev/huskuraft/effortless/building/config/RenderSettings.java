package dev.huskuraft.effortless.building.config;

public record RenderSettings(
        boolean showOtherPlayersBuild,
        boolean showBlockPreview,
        int maxRenderBlocks,
        int maxRenderDistance
) {

    public static final int MIN_MAX_RENDER_BLOCKS = 0;
    public static final int MAX_MAX_RENDER_BLOCKS = 4096;

    public static final int MIN_MAX_RENDER_DISTANCE = 16;
    public static final int MAX_MAX_RENDER_DISTANCE = 512;

    public RenderSettings() {
        this(
                true,
                true,
                1024,
                128
        );
    }

}
