package dev.huskuraft.effortless.renderer.pattern;

import java.awt.*;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;

public class RadialTransformerRenderer extends TransformerRenderer {

    private final RadialTransformer transformer;
    private final boolean renderPlanes;

    public RadialTransformerRenderer(ClientEntrance entrance, RadialTransformer transformer, boolean renderPlanes) {
        super(entrance);
        this.transformer = transformer;
        this.renderPlanes = renderPlanes;
    }

    @Override
    public void render(Renderer renderer, float deltaTick) {

        renderPlaneByAxis(renderer, transformer.position(), 1024, Axis.X, new Color(0, 0, 0, 72));
        renderLineByAxis(renderer, transformer.position(), 1024, Axis.Y, new Color(0, 0, 0, 200));
    }

}

