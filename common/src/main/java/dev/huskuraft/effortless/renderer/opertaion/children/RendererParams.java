package dev.huskuraft.effortless.renderer.opertaion.children;

public interface RendererParams {

    boolean showBlockPreview();

    int maxRenderBlocks();

    int maxRenderDistance();

    int accumulated();

    void accumulate();

    default boolean shouldRenderBlocks() {
        return accumulated() <= maxRenderBlocks() && showBlockPreview();
    }

    final class Default implements RendererParams {
        private final boolean showBlockPreview;
        private final int maxRenderBlocks;
        private final int maxRenderDistance;
        private int accumulated = 0;

        public Default(
                boolean showBlockPreview,
                int maxRenderBlocks,
                int maxRenderDistance
        ) {
            this.showBlockPreview = showBlockPreview;
            this.maxRenderBlocks = maxRenderBlocks;
            this.maxRenderDistance = maxRenderDistance;
        }

        @Override
        public int accumulated() {
            return accumulated;
        }

        @Override
        public void accumulate() {
            accumulated++;
        }

        @Override
        public boolean showBlockPreview() {
            return showBlockPreview;
        }

        @Override
        public int maxRenderBlocks() {
            return maxRenderBlocks;
        }

        @Override
        public int maxRenderDistance() {
            return maxRenderDistance;
        }

    }


}
