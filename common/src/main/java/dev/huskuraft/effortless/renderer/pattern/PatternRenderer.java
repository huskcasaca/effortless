package dev.huskuraft.effortless.renderer.pattern;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import dev.huskuraft.effortless.api.renderer.RenderFadeEntry;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;

public class PatternRenderer {

    private final Map<UUID, RenderFadeEntry<Context>> entries = Collections.synchronizedMap(new LinkedHashMap<>());

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
        entries.put(uuid, new RenderFadeEntry<>(context));
    }

    public void render(Renderer renderer, float deltaTick) {
        entries.forEach((k, v) -> {
            for (var transformer : v.getValue().pattern().transformers()) {
                if (transformer instanceof MirrorTransformer mirrorTransformer) {
                    new MirrorTransformerRenderer(mirrorTransformer, true).render(renderer, deltaTick);
                }
                if (transformer instanceof ArrayTransformer arrayTransformer) {
                    new ArrayTransformerRenderer(arrayTransformer).render(renderer, deltaTick);
                }
                if (transformer instanceof RadialTransformer radialTransformer) {
                    new RadialTransformerRenderer(radialTransformer, true).render(renderer, deltaTick);
                }
            }
        });
    }


}

