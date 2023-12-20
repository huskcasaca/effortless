package dev.huskuraft.effortless.renderer;

public final class OverlayTexture {

    private OverlayTexture() {

    }

    private static final int SIZE = 16;
    public static final int NO_WHITE_U = 0;
    public static final int RED_OVERLAY_V = 3;
    public static final int WHITE_OVERLAY_V = 10;
    public static final int NO_OVERLAY = pack(0, 10);

    public static int pack(int u, int v) {
        return u | v << 16;
    }

}
