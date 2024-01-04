package dev.huskuraft.effortless.renderer.pattern;

import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.math.Vector3d;

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
        var axis = transformer.axis();
        var cam = renderer.camera().position();
        var position = transformer.position();
        var planeCenter = new Vector3d(axis != Axis.X ? cam.x() : position.x(), axis != Axis.Y ? cam.y() : position.y(), axis != Axis.Z ? cam.z() : position.z());

        renderPlaneByAxis(renderer, planeCenter, 1024, transformer.axis(), new Color(0, 0, 0, 72));
        for (var value : Axis.values()) {
            if (value == axis) {
                continue;
            }
            renderLineByAxis(renderer, planeCenter, 1024, value, new Color(0, 0, 0, 200));
        }
    }

}

