package dev.huskuraft.effortless.renderer.opertaion.children;

public interface RendererParams {

    int maxRenderBlocks();

    int maxRenderDistance();

    int accumulated();

    void accumulate();

    default boolean isFull() {
        return accumulated() >= maxRenderBlocks();
    }

    final class Default implements RendererParams {
        private final int maxRenderBlocks;
        private final int maxRenderDistance;
        private int accumulated = 0;

        public Default(
                int maxRenderBlocks,
                int maxRenderDistance
        ) {
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
        public int maxRenderBlocks() {
            return maxRenderBlocks;
        }

        @Override
        public int maxRenderDistance() {
            return maxRenderDistance;
        }

    }


}
