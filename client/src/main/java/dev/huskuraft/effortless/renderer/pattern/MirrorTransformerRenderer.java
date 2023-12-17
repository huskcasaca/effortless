package dev.huskuraft.effortless.renderer.pattern;

import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.renderer.Renderer;

import java.awt.*;

public class MirrorTransformerRenderer extends TransformerRenderer {

    private final MirrorTransformer transformer;
    private final boolean renderPlanes;

    public MirrorTransformerRenderer(MirrorTransformer transformer, boolean renderPlanes) {
        this.transformer = transformer;
        this.renderPlanes = renderPlanes;
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {
        renderPlaneByAxis(renderer, transformer.position(), 1024, transformer.axis(), new Color(0, 0, 0, 72));
//        if (drawPlanes) {
//            for (Axis a : axis) {
//                VertexConsumer buffer = ((MultiBufferSource) multiBufferSource).getBuffer(FabricRenderLayers.planes());
//                drawAxisPlane(buffer, position, radius, a, COLOR_PLANE);
//                multiBufferSource.endBatch();
//            }
//        }
//        if (drawLines) {
//            for (Axis a : axis) {
//                VertexConsumer buffer = multiBufferSource.getBuffer(FabricRenderLayers.lines());
//                for (Axis a1 : Axis.values()) {
//                    if (a1 != a) {
//                        drawAxisLine(buffer, position, radius, a1, COLOR_LINE);
//                    }
//                }
//                multiBufferSource.endBatch();
//            }
//        }

    }

}

