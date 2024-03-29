package dev.huskuraft.effortless.api.gui;

import java.awt.*;

public class Dimens {

    public static final int SLOT_OFFSET_X = 20;
    public static final int SLOT_OFFSET_Y = 20;

    public static final int ICON_WIDTH = 30;
    public static final int ICON_HEIGHT = 30;

    public static final int SLOT_WIDTH = 18;
    public static final int SLOT_HEIGHT = 18;

    public static class CellRing {
        public static final Color RADIAL_COLOR = new Color(0.44f, 0.44f, 0.44f, 1f);
        public static final Color HIGHLIGHT_COLOR = new Color(0.84f, 0.84f, 0.84f, 1f);
        public static final int RADIAL_SIZE = 12;
        public static final double RING_INNER_EDGE = 6;
        public static final double RING_OUTER_EDGE = 15;

        private CellRing() {
        }
    }

    public static class RegularEntry {
        public static final int ROW_WIDTH = 278;
        public static final int MAX_SLOT_COUNT = (ROW_WIDTH - ICON_WIDTH) / SLOT_OFFSET_X;
    }

}
