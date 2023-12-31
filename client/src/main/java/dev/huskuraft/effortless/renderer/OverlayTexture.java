package dev.huskuraft.effortless.renderer;

public abstract class OverlayTexture {

    public static final int NO_WHITE_U = 0;
    public static final int RED_OVERLAY_V = 3;
    public static final int WHITE_OVERLAY_V = 10;
    public static final int NO_OVERLAY = pack(0, 10);
    private static final int SIZE = 16;

    private OverlayTexture() {

    }

    public static int pack(int u, int v) {
        return u | v << 16;
    }

}
