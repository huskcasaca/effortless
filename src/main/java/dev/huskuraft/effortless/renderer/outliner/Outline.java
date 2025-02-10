package dev.huskuraft.effortless.renderer.outliner;

import java.awt.*;
import java.util.Optional;

import javax.annotation.Nullable;

import dev.huskuraft.universal.api.core.Direction;
import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.renderer.LightTexture;
import dev.huskuraft.universal.api.renderer.RenderUtils;
import dev.huskuraft.universal.api.renderer.Renderer;

public abstract class Outline {

    protected OutlineParams params;

    protected Outline() {
        params = new OutlineParams();
    }

    public abstract void render(Renderer renderer, float deltaTick);

    public void tick() {
    }

    public OutlineParams getParams() {
        return params;
    }

    public void renderCuboidLine(Renderer renderer, Vector3d start, Vector3d end) {
        var diff = end.sub(start);
//        var hAngle = MathUtils.deg(MathUtils.atan2(diff.getX(), diff.getZ()));
        var hDistance = (float) diff.mul(1, 0, 1).length();
//        var vAngle = MathUtils.deg(MathUtils.atan2(hDistance, diff.getY())) - 90;
        renderer.pushPose();
        // TODO: 27/1/23
        renderer.translate(start);
//			.rotateY(hAngle).rotateX(vAngle);
        renderAACuboidLine(renderer, Vector3d.ZERO, new Vector3d(0, 0, diff.length()));
        renderer.popPose();
    }

    public void renderAACuboidLine(Renderer renderer, Vector3d start, Vector3d end) {
        var camera = renderer.getCamera().position();
        start = start.sub(camera);
        end = end.sub(camera);
        var lineWidth = getParams().getLineWidth();
        if (lineWidth == 0)
            return;

        var renderLayer = OutlineRenderLayers.outlineSolid(true);

        var diff = end.sub(start);
        if (diff.x() + diff.y() + diff.z() < 0) {
            var temp = start;
            start = end;
            end = temp;
            diff = diff.mul(-1);
        }

        var extension = diff.normalize().mul(lineWidth / 2);
        var plane = RenderUtils.calculateAxisAlignedPlane(diff);
        var face = Direction.getNearest(diff.x(), diff.y(), diff.z());
        var axis = face.getAxis();

        start = start.sub(extension);
        end = end.add(extension);
        plane = plane.mul(lineWidth / 2);

        var a1 = plane.add(start);
        var b1 = plane.add(end);
        plane = RenderUtils.rotate(plane, -90, axis);
        var a2 = plane.add(start);
        var b2 = plane.add(end);
        plane = RenderUtils.rotate(plane, -90, axis);
        var a3 = plane.add(start);
        var b3 = plane.add(end);
        plane = RenderUtils.rotate(plane, -90, axis);
        var a4 = plane.add(start);
        var b4 = plane.add(end);

        if (getParams().disableNormals) {
            face = Direction.UP;
            renderer.renderQuad(renderLayer, b4, b3, b2, b1, getParams().getLightMap(), getParams().getColor().getRGB(), face);
            renderer.renderQuad(renderLayer, a1, a2, a3, a4, getParams().getLightMap(), getParams().getColor().getRGB(), face);
            renderer.renderQuad(renderLayer, a1, b1, b2, a2, getParams().getLightMap(), getParams().getColor().getRGB(), face);
            renderer.renderQuad(renderLayer, a2, b2, b3, a3, getParams().getLightMap(), getParams().getColor().getRGB(), face);
            renderer.renderQuad(renderLayer, a3, b3, b4, a4, getParams().getLightMap(), getParams().getColor().getRGB(), face);
            renderer.renderQuad(renderLayer, a4, b4, b1, a1, getParams().getLightMap(), getParams().getColor().getRGB(), face);
            return;
        }

        renderer.renderQuad(renderLayer, b4, b3, b2, b1, getParams().getLightMap(), getParams().getColor().getRGB(), face);
        renderer.renderQuad(renderLayer, a1, a2, a3, a4, getParams().getLightMap(), getParams().getColor().getRGB(), face.getOpposite());
        var vec = a1.sub(a4);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a1, b1, b2, a2, getParams().getLightMap(), getParams().getColor().getRGB(), face);
        vec = RenderUtils.rotate(vec, -90, axis);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a2, b2, b3, a3, getParams().getLightMap(), getParams().getColor().getRGB(), face);
        vec = RenderUtils.rotate(vec, -90, axis);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a3, b3, b4, a4, getParams().getLightMap(), getParams().getColor().getRGB(), face);
        vec = RenderUtils.rotate(vec, -90, axis);
        face = Direction.getNearest(vec.x(), vec.y(), vec.z());
        renderer.renderQuad(renderLayer, a4, b4, b1, a1, getParams().getLightMap(), getParams().getColor().getRGB(), face);
    }

    public static class OutlineParams {
        protected Optional<ResourceLocation> faceTexture;
        protected Optional<ResourceLocation> highlightedFaceTexture;
        protected Direction highlightedFace;
        protected boolean fadeLineWidth;
        protected boolean disableCull;
        protected boolean disableNormals;
        protected float alpha;
        protected int lightMap;
        protected Color rgb;
        private float lineWidth;

        public OutlineParams() {
            faceTexture = highlightedFaceTexture = Optional.empty();
            alpha = 1;
            lineWidth = 1 / 32f;
            fadeLineWidth = true;
            rgb = Color.WHITE;
            lightMap = LightTexture.FULL_BRIGHT;
        }

        // builder

        public OutlineParams colored(float r, float g, float b, float a) {
            rgb = new Color(r, g, b, a);
            return this;
        }

        public OutlineParams colored(int red, int green, int blue, int alpha) {
            rgb = new Color(red, green, blue, alpha);
            return this;
        }

        public OutlineParams colored(int color) {
            rgb = new Color(color, false);
            return this;
        }

        public OutlineParams colored(Color c) {
            rgb = c;
            return this;
        }

        public OutlineParams lightMap(int light) {
            lightMap = light;
            return this;
        }

        public OutlineParams stroke(float width) {
            this.lineWidth = width;
            return this;
        }

        public OutlineParams texture(ResourceLocation location) {
            this.faceTexture = Optional.ofNullable(location);
            return this;
        }

        public OutlineParams clearTextures() {
            return this.textures(null, null);
        }

        public OutlineParams textures(ResourceLocation texture, ResourceLocation highlightTexture) {
            this.faceTexture = Optional.ofNullable(texture);
            this.highlightedFaceTexture = Optional.ofNullable(highlightTexture);
            return this;
        }

        public OutlineParams highlightFace(@Nullable Direction face) {
            highlightedFace = face;
            return this;
        }

        public OutlineParams disableNormals() {
            disableNormals = true;
            return this;
        }

        public OutlineParams disableCull() {
            disableCull = true;
            return this;
        }

        // getter

        public float getLineWidth() {
            return fadeLineWidth ? alpha * lineWidth : lineWidth;
        }

        public Direction getHighlightedFace() {
            return highlightedFace;
        }

        public int getLightMap() {
            return lightMap;
        }

        public Color getColor() {
            return new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), (int) (rgb.getAlpha() * alpha));
        }

    }

}
