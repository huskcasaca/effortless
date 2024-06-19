package dev.huskuraft.effortless.vanilla.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.huskuraft.effortless.api.renderer.VertexBuffer;

public record MinecraftVertexBuffer(
        VertexConsumer refs
) implements VertexBuffer {

    @Override
    public MinecraftVertexBuffer vertex(double x, double y, double z) {
        refs.vertex(x, y, z);
        return this;
    }

    @Override
    public MinecraftVertexBuffer color(int red, int green, int blue, int alpha) {
        refs.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public MinecraftVertexBuffer uv(float u, float v) {
        refs.uv(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer overlayCoords(int u, int v) {
        refs.overlayCoords(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer uv2(int u, int v) {
        refs.uv2(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer normal(float x, float y, float z) {
        refs.normal(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        refs.endVertex();
    }

    @Override
    public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA) {
        refs.color(defaultR, defaultG, defaultB, defaultA);
    }

    @Override
    public void unsetDefaultColor() {
        refs.unsetDefaultColor();
    }

}
