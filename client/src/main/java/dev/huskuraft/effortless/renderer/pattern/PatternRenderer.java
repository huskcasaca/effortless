package dev.huskuraft.effortless.renderer.pattern;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.renderer.Renderer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PatternRenderer {

    private final Map<UUID, PatternEntry> entries = Collections.synchronizedMap(new HashMap<>());

    public PatternRenderer() {

    }

    public void tick() {
        var iterator = entries.values().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            entry.tick();
            if (!entry.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void showPattern(UUID uuid, Context context) {
        entries.put(uuid, new PatternEntry(context.pattern()));
    }

    public void render(Renderer renderer, float deltaTick) {
        entries.forEach((k, v) -> {
            for (var transformer : v.getPattern().transformers()) {
                if (transformer instanceof MirrorTransformer mirrorTransformer) {
                    new MirrorTransformerRenderer(mirrorTransformer, true).render(renderer, deltaTick);
                }
            }
        });
    }

    public static class PatternEntry {

        private static final int FADE_TICKS = 5;

        private final Pattern pattern;
        private int ticksTillRemoval;

        public PatternEntry(Pattern pattern) {
            this.pattern = pattern;
            ticksTillRemoval = 1;
        }

        public void tick() {
            ticksTillRemoval--;
        }

        public boolean isAlive() {
            return ticksTillRemoval >= -FADE_TICKS;
        }

        public boolean isFading() {
            return ticksTillRemoval < 0;
        }

        public Pattern getPattern() {
            return pattern;
        }

    }


}

