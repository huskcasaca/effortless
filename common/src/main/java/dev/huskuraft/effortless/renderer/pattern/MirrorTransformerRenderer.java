package dev.huskuraft.effortless.renderer.pattern;

import java.awt.*;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;

public class MirrorTransformerRenderer extends TransformerRenderer {

    private final MirrorTransformer transformer;
    private final boolean renderPlanes;

    public MirrorTransformerRenderer(ClientEntrance entrance, MirrorTransformer transformer, boolean renderPlanes) {
        super(entrance);
        this.transformer = transformer;
        this.renderPlanes = renderPlanes;
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {

        renderPlaneByAxis(renderer, transformer.position(), transformer.size(), transformer.axis(), new Color(0, 0, 0, 72));
        for (var value : Axis.values()) {
            if (value != transformer.axis()) {
                renderLineByAxis(renderer, transformer.position(), transformer.size(), value, new Color(0, 0, 0, 200));
            }
        }
        renderBoundingBox(renderer, transformer.getPositionBoundingBox(), new Color(0, 0, 0, 140).getRGB());
    }

}

