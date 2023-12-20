package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.Orientation;
import dev.huskuraft.effortless.math.Vector3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface VertexBuffer {

    VertexBuffer vertex(double x, double y, double z);

    VertexBuffer color(int red, int green, int blue, int alpha);

    VertexBuffer uv(float u, float v);

    VertexBuffer overlayCoords(int u, int v);

    VertexBuffer uv2(int u, int v);

    VertexBuffer normal(float x, float y, float z);

    void endVertex();

    void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA);

    void unsetDefaultColor();

    default VertexBuffer color(float red, float green, float blue, float alpha) {
        return this.color((int) (red * 255.0F), (int) (green * 255.0F), (int) (blue * 255.0F), (int) (alpha * 255.0F));
    }

    default VertexBuffer color(int color) {
        return this.color(RenderUtils.ARGB32.red(color), RenderUtils.ARGB32.green(color), RenderUtils.ARGB32.blue(color), RenderUtils.ARGB32.alpha(color));
    }

    default VertexBuffer uv2(int lightmapUV) {
        return this.uv2(lightmapUV & '\uffff', lightmapUV >> 16 & '\uffff');
    }

    default VertexBuffer overlayCoords(int overlayUV) {
        return this.overlayCoords(overlayUV & '\uffff', overlayUV >> 16 & '\uffff');
    }

    default VertexBuffer vertex(Matrix4f matrix, float x, float y, float z) {
        var vector4f = matrix.transform(new Vector4f(x, y, z, 1.0F));
        return this.vertex(vector4f.x(), vector4f.y(), vector4f.z());
    }

    default VertexBuffer vertex(Matrix4f matrix, Vector3d vector3d) {
        return vertex(matrix, (float) vector3d.getX(), (float) vector3d.getY(), (float) vector3d.getZ());
    }

    default VertexBuffer normal(Matrix3f matrix, float x, float y, float z) {
        var vector3f = matrix.transform(new Vector3f(x, y, z));
        return this.normal(vector3f.x(), vector3f.y(), vector3f.z());
    }

    default VertexBuffer normal(Matrix3f matrix, Orientation normal) {
        var xOffset = normal != null ? normal.getStepX() : 0;
        var yOffset = normal != null ? normal.getStepY() : 0;
        var zOffset = normal != null ? normal.getStepZ() : 0;
        return this.normal(matrix, xOffset, yOffset, zOffset);
    }


}
