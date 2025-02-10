package dev.huskuraft.effortless.renderer.outliner;

import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.renderer.Renderer;

public class LineOutline extends Outline {

    protected Vector3d start = Vector3d.ZERO;
    protected Vector3d end = Vector3d.ZERO;

    public LineOutline set(Vector3d start, Vector3d end) {
        this.start = start;
        this.end = end;
        return this;
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {
        renderCuboidLine(renderer, start, end);
    }

    public static class EndChasingLineOutline extends LineOutline {

        private final boolean lockStart;
        float prevProgress = 0;
        float progress = 0;

        public EndChasingLineOutline(boolean lockStart) {
            this.lockStart = lockStart;
        }

        @Override
        public void tick() {
        }

        public EndChasingLineOutline setProgress(float progress) {
            prevProgress = this.progress;
            this.progress = progress;
            return this;
        }

        @Override
        public LineOutline set(Vector3d start, Vector3d end) {
            if (!end.equals(this.end))
                super.set(start, end);
            return this;
        }

        @Override
        public void render(Renderer renderer, float deltaTick) {
            float distanceToTarget = MathUtils.lerp(deltaTick, prevProgress, progress);
            if (!lockStart) {
                distanceToTarget = 1 - distanceToTarget;
            }
            var start = lockStart ? this.end : this.start;
            var end = lockStart ? this.start : this.end;

            start = end.add(this.start.sub(end).mul(distanceToTarget));
            renderCuboidLine(renderer, start, end);
        }

    }

}
