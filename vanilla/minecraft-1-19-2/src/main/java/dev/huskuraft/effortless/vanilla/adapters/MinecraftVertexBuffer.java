package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.huskuraft.effortless.renderer.VertexBuffer;

public class MinecraftVertexBuffer implements VertexBuffer {

    private final VertexConsumer reference;

    MinecraftVertexBuffer(VertexConsumer reference) {
        this.reference = reference;
    }

    @Override
    public MinecraftVertexBuffer vertex(double x, double y, double z) {
        reference.vertex(x, y, z);
        return this;
    }

    @Override
    public MinecraftVertexBuffer color(int red, int green, int blue, int alpha) {
        reference.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public MinecraftVertexBuffer uv(float u, float v) {
        reference.uv(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer overlayCoords(int u, int v) {
        reference.overlayCoords(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer uv2(int u, int v) {
        reference.uv2(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer normal(float x, float y, float z) {
        reference.normal(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        reference.endVertex();
    }

    @Override
    public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA) {
        reference.color(defaultR, defaultG, defaultB, defaultA);
    }

    @Override
    public void unsetDefaultColor() {
        reference.unsetDefaultColor();
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftVertexBuffer vertexBuffer && reference.equals(vertexBuffer.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
