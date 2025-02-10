package dev.huskuraft.effortless.renderer.pattern;

import java.awt.*;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.platform.ClientEntrance;
import dev.huskuraft.universal.api.renderer.Renderer;
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
        var axis = transformer.axis();
        var center0 = transformer.position();
        var radius = transformer.radius();
        var length = transformer.length();
        var planeColor = new Color(0, 0, 0, 72).getRGB();
        var sidePlaneColor = new Color(0, 0, 0, 112).getRGB();

        for (int i = 0; i < transformer.slices(); i++) {
            var ang1 = 2 * MathUtils.PI / transformer.slices() * i;
            var pos1 = switch (axis) {
                case X ->
                        center0.withY(center0.y() + radius * Math.cos(ang1)).withZ(center0.z() + radius * Math.sin(ang1));
                case Y ->
                        center0.withX(center0.x() + radius * Math.cos(ang1)).withZ(center0.z() + radius * Math.sin(ang1));
                case Z ->
                        center0.withX(center0.x() + radius * Math.cos(ang1)).withY(center0.y() + radius * Math.sin(ang1));
            };
            renderRadialPlane(renderer, center0, pos1, length, axis, planeColor);
        }

        var center1 = center0.add(axis == Axis.X ? length : 0, axis == Axis.Y ? length : 0, axis == Axis.Z ? length : 0);
        var center2 = center0.sub(axis == Axis.X ? length : 0, axis == Axis.Y ? length : 0, axis == Axis.Z ? length : 0);

        var slices = 8 * radius;

        for (int i = 0; i < slices; i++) {
            var ang1 = 2 * MathUtils.PI / (slices) * i;
            var ang2 = 2 * MathUtils.PI / (slices) * (i + 1);
            var pos10 = switch (axis) {
                case X ->
                        center0.withY(center0.y() + radius * Math.cos(ang1)).withZ(center0.z() + radius * Math.sin(ang1));
                case Y ->
                        center0.withX(center0.x() + radius * Math.cos(ang1)).withZ(center0.z() + radius * Math.sin(ang1));
                case Z ->
                        center0.withX(center0.x() + radius * Math.cos(ang1)).withY(center0.y() + radius * Math.sin(ang1));
            };
            var pos20 = switch (axis) {
                case X ->
                        center0.withY(center0.y() + radius * Math.cos(ang2)).withZ(center0.z() + radius * Math.sin(ang2));
                case Y ->
                        center0.withX(center0.x() + radius * Math.cos(ang2)).withZ(center0.z() + radius * Math.sin(ang2));
                case Z ->
                        center0.withX(center0.x() + radius * Math.cos(ang2)).withY(center0.y() + radius * Math.sin(ang2));
            };
            renderRadialPlane(renderer, pos10, pos20, length, axis, planeColor);

            var pos1a = pos10.add(axis == Axis.X ? length : 0, axis == Axis.Y ? length : 0, axis == Axis.Z ? length : 0);
            var pos1b = pos10.sub(axis == Axis.X ? length : 0, axis == Axis.Y ? length : 0, axis == Axis.Z ? length : 0);
            var pos2a = pos20.add(axis == Axis.X ? length : 0, axis == Axis.Y ? length : 0, axis == Axis.Z ? length : 0);
            var pos2b = pos20.sub(axis == Axis.X ? length : 0, axis == Axis.Y ? length : 0, axis == Axis.Z ? length : 0);

            renderRadialFloor(renderer, center1, pos1a, pos2a, sidePlaneColor);
            renderRadialFloor(renderer, center2, pos1b, pos2b, sidePlaneColor);

            for (var value : Axis.values()) {
                if (value != axis) {
                    renderLine(renderer, pos10, pos20);
                    renderLine(renderer, pos1a, pos2a);
                    renderLine(renderer, pos1b, pos2b);
                }
            }
        }

        renderLineByAxis(renderer, center0, length, axis);
        for (var value : Axis.values()) {
            if (value != axis) {
                renderLineByAxis(renderer, center0, radius, value);
                renderLineByAxis(renderer, center1, radius, value);
                renderLineByAxis(renderer, center2, radius, value);
            }
        }

    }

}

