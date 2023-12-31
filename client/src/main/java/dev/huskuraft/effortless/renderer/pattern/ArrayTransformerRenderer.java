package dev.huskuraft.effortless.renderer.pattern;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.renderer.FontDisplay;
import dev.huskuraft.effortless.renderer.LightTexture;
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

        if (context.interactionsSize() == 0) {
            return;
        }
        var typeface = EffortlessClient.getInstance().getClient().getTypeface();
        for (var result : context.interactions().results()) {
            if (result == null) {
                break; // global check
            }

            var interactionPosition = result.getBlockPosition().getCenter();

            for (var i = 0; i < transformer.copyCount(); i++) {

                var v1 = interactionPosition.add(transformer.offset().mul(i));
                var v2 = interactionPosition.add(transformer.offset().mul(i + 1));
                renderAACuboidLine(renderer, v1, v2, 1 / 32f, 0xFFFFFF, true);
                var cam = renderer.camera().position();
                renderer.pushPose();
                var mid = v1.add(v2).div(2);
                renderer.translate(mid.sub(cam));
                renderer.pushPose();

                renderer.rotate(renderer.camera().rotation());
                renderer.scale(-0.025F, -0.025F, 0.025F);
                var text = Text.text(transformer.offset().toString());
                renderer.renderText(typeface, text, -typeface.measureWidth(text) / 2, 0, 0xFFFFFFFF, 0, false, FontDisplay.NORMAL, LightTexture.FULL_BRIGHT);
                renderer.popPose();
                renderer.popPose();
            }

        }

    }


}

