package dev.huskuraft.effortless.renderer.outliner;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.renderer.Renderer;

public class OutlineRenderer {

    private final Map<Object, OutlineEntry> outlines = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<Object, OutlineEntry> outlinesView = Collections.unmodifiableMap(outlines);

    public OutlineRenderer() {
    }

    public Outline.OutlineParams showLine(Object id, Vector3d start, Vector3d end) {
        if (!outlines.containsKey(id)) {
            var outline = new LineOutline();
            outlines.put(id, new OutlineEntry(outline));
        }
        var entry = outlines.get(id);
        entry.ticksTillRemoval = 1;
        ((LineOutline) entry.outline).set(start, end);
        return entry.outline.getParams();
    }

    public Outline.OutlineParams endChasingLine(Object id, Vector3d start, Vector3d end, float chasingProgress, boolean lockStart) {
        if (!outlines.containsKey(id)) {
            var outline = new LineOutline.EndChasingLineOutline(lockStart);
            outlines.put(id, new OutlineEntry(outline));
        }
        var entry = outlines.get(id);
        entry.ticksTillRemoval = 1;
        ((LineOutline.EndChasingLineOutline) entry.outline).setProgress(chasingProgress)
                .set(start, end);
        return entry.outline.getParams();
    }

    public Outline.OutlineParams showBoundingBox(Object id, BoundingBox3d boundingBox, int ttl) {
        createBoundingBoxOutlineIfMissing(id, boundingBox);
        var outline = getAndRefreshBoundingBox(id, ttl);
        outline.prevBB = outline.targetBB = outline.bb = boundingBox;
        return outline.getParams();
    }

    public Outline.OutlineParams showBoundingBox(Object id, BoundingBox3d boundingBox) {
        createBoundingBoxOutlineIfMissing(id, boundingBox);
        var outline = getAndRefreshBoundingBox(id);
        outline.prevBB = outline.targetBB = outline.bb = boundingBox;
        return outline.getParams();
    }

    public Outline.OutlineParams chaseBoundingBox(Object id, BoundingBox3d boundingBox) {
        createBoundingBoxOutlineIfMissing(id, boundingBox);
        var outline = getAndRefreshBoundingBox(id);
        outline.targetBB = boundingBox;
        return outline.getParams();
    }

    public Outline.OutlineParams showCluster(Object id, Iterable<BlockPosition> selection) {
        BlockClusterOutline outline = new BlockClusterOutline(selection);
        var entry = new OutlineEntry(outline);
        outlines.put(id, entry);
        return entry.getOutline().getParams();
    }

    public void keep(Object id) {
        if (outlines.containsKey(id))
            outlines.get(id).ticksTillRemoval = 1;
    }

    public void keep(Object id, int ticks) {
        if (outlines.containsKey(id))
            outlines.get(id).ticksTillRemoval = ticks;
    }

    public void remove(Object id) {
        outlines.remove(id);
    }

    public Optional<Outline.OutlineParams> edit(Object id) {
        keep(id);
        if (outlines.containsKey(id))
            return Optional.of(outlines.get(id)
                    .getOutline()
                    .getParams());
        return Optional.empty();
    }

    public Map<Object, OutlineEntry> getOutlines() {
        return outlinesView;
    }

    private void createBoundingBoxOutlineIfMissing(Object id, BoundingBox3d bb) {
        if (!outlines.containsKey(id) || !(outlines.get(id).outline instanceof BlockBoundingBoxOutline)) {
            var outline = new ChasingBlockBoundingBoxOutline(bb);
            outlines.put(id, new OutlineEntry(outline));
        }
    }

    private ChasingBlockBoundingBoxOutline getAndRefreshBoundingBox(Object id) {
        var entry = outlines.get(id);
        entry.ticksTillRemoval = 1;
        return (ChasingBlockBoundingBoxOutline) entry.getOutline();
    }

    private ChasingBlockBoundingBoxOutline getAndRefreshBoundingBox(Object id, int ttl) {
        var entry = outlines.get(id);
        entry.ticksTillRemoval = ttl;
        return (ChasingBlockBoundingBoxOutline) entry.getOutline();
    }

    public void tick() {
        var iterator = outlines.values().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            entry.tick();
            if (!entry.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void render(Renderer renderer, float deltaTick) {

        outlines.forEach((key, entry) -> {
            var outline = entry.getOutline();
            var params = outline.getParams();
            params.alpha = 1;
            if (entry.isFading()) {
                var prevTicks = entry.ticksTillRemoval + 1;
                float fadeticks = OutlineEntry.FADE_TICKS;
                float lastAlpha = prevTicks >= 0 ? 1 : 1 + prevTicks / fadeticks;
                float currentAlpha = 1 + entry.ticksTillRemoval / fadeticks;
                float alpha = MathUtils.lerp(deltaTick, lastAlpha, currentAlpha);

                params.alpha = alpha * alpha * alpha;
                if (params.alpha < 1 / 8f)
                    return;
            }
            outline.render(renderer, deltaTick);
        });
    }

    public static class OutlineEntry {

        private static final int FADE_TICKS = 10;

        private final Outline outline;
        private int ticksTillRemoval;

        public OutlineEntry(Outline outline) {
            this.outline = outline;
            ticksTillRemoval = 1;
        }

        public void tick() {
            ticksTillRemoval--;
            outline.tick();
        }

        public boolean isAlive() {
            return ticksTillRemoval >= -FADE_TICKS;
        }

        public boolean isFading() {
            return ticksTillRemoval < 0;
        }

        public Outline getOutline() {
            return outline;
        }

    }

}
