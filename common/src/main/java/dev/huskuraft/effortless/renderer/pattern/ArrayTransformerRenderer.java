package dev.huskuraft.effortless.renderer.pattern;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.renderer.LightTexture;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;

public class ArrayTransformerRenderer extends TransformerRenderer {

    private final ArrayTransformer transformer;

    public ArrayTransformerRenderer(ClientEntrance entrance, ArrayTransformer transformer) {
        super(entrance);
        this.transformer = transformer;
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {

        var context = getEntrance().getStructureBuilder().getContextTraced(
                EffortlessClient.getInstance().getClientManager().getRunningClient().getPlayer()
        );

        if (context.interactionsSize() == 0) {
            return;
        }
        var typeface = EffortlessClient.getInstance().getClient().getTypeface();
        for (var result : context.buildInteractions().results()) {
            if (result == null) {
                break; // global check
            }

            var interactionPosition = result.getBlockPosition().getCenter();

            for (var i = 0; i < transformer.copyCount(); i++) {

                var v1 = interactionPosition.add(transformer.offset().mul(i).toVector3d());
                var v2 = interactionPosition.add(transformer.offset().mul(i + 1).toVector3d());
                renderAACuboidLine(renderer, v1, v2, 1 / 32f, 0xFFFFFF, true);
                var cam = renderer.camera().position();
                renderer.pushPose();
                var mid = v1.add(v2).div(2);
                renderer.translate(mid.sub(cam));
                renderer.pushPose();

                renderer.rotate(renderer.camera().rotation());
                renderer.scale(-0.025F, -0.025F, 0.025F);
                var text = Text.text(transformer.offset().toString());
                renderer.renderText(typeface, text, -typeface.measureWidth(text) / 2, 0, 0xFFFFFFFF, 0, false, false, LightTexture.FULL_BRIGHT);
                renderer.popPose();
                renderer.popPose();
            }

        }

    }


}

