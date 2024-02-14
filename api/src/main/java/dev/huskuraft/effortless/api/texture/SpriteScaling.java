package dev.huskuraft.effortless.api.texture;

sealed public interface SpriteScaling permits SpriteScaling.Stretch, SpriteScaling.Tile, SpriteScaling.NineSlice {

    Type type();

    enum Type {
        STRETCH,
        TILE,
        NINE_SLICE,
    }

    record Stretch() implements SpriteScaling {

        @Override
        public Type type() {
            return Type.STRETCH;
        }
    }

    record Tile(int width, int height) implements SpriteScaling {

        @Override
        public Type type() {
            return Type.TILE;
        }
    }

    record NineSlice(int width, int height, int left, int right, int top, int bottom) implements SpriteScaling {

        public NineSlice(int width, int height) {
            this(width, height, 0, 0, 0, 0);
        }

        public NineSlice(int width, int height, int border) {
            this(width, height, border, border, border, border);
        }

        @Override
        public Type type() {
            return Type.NINE_SLICE;
        }
    }

}
