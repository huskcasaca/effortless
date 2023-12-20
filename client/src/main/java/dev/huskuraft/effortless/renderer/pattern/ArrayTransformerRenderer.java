package dev.huskuraft.effortless.renderer.pattern;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class ArrayTransformerRenderer extends TransformerRenderer {

    private final ArrayTransformer transformer;

    public ArrayTransformerRenderer(ArrayTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {

        var context = EffortlessClient.getInstance().getStructureBuilder().getContextTraced(
                EffortlessClient.getInstance().getClientManager().getRunningClient().getPlayer()
        );

        if (context.clicks() == 0) {
            return;
        }
        var typeface = EffortlessClient.getInstance().getClient().getTypeface();
        for (var result : context.interactions().results()) {
            if (result == null) {
                break; // global check
            }

            var interactionPosition = result.getBlockPosition().getCenter();

            for (var i = 0; i < transformer.copyCount(); i++) {

                var v1 = interactionPosition.add(transformer.offset().multiply(i));
                var v2 = interactionPosition.add(transformer.offset().multiply(i + 1));
                renderAACuboidLine(renderer, v1, v2, 1 / 32f, 0xFFFFFF, true);
                var cam = renderer.camera().position();
                renderer.pushPose();
                var mid = v1.add(v2).divide(2);
                renderer.translate(-cam.getX() + mid.getX(), -cam.getY() + mid.getY(), -cam.getZ() + mid.getZ());
                renderer.pushPose();
                renderer.drawNameTag(typeface, Text.text("X1 Y10 Z2"));
                renderer.popPose();
                renderer.popPose();
            }

        }

    }


}

