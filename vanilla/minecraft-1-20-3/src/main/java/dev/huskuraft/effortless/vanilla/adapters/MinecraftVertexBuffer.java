package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.huskuraft.effortless.renderer.VertexBuffer;

public class MinecraftVertexBuffer implements VertexBuffer {

    private final VertexConsumer consumer;

    MinecraftVertexBuffer(VertexConsumer consumer) {
        this.consumer = consumer;
    }

    public VertexConsumer getRef() {
        return consumer;
    }

    @Override
    public MinecraftVertexBuffer vertex(double x, double y, double z) {
        consumer.vertex(x, y, z);
        return this;
    }

    @Override
    public MinecraftVertexBuffer color(int red, int green, int blue, int alpha) {
        consumer.color(red, green, blue, alpha);
        return this;
    }

    @Override
    public MinecraftVertexBuffer uv(float u, float v) {
        consumer.uv(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer overlayCoords(int u, int v) {
        consumer.overlayCoords(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer uv2(int u, int v) {
        consumer.uv2(u, v);
        return this;
    }

    @Override
    public MinecraftVertexBuffer normal(float x, float y, float z) {
        consumer.normal(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        consumer.endVertex();
    }

    @Override
    public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA) {
        consumer.color(defaultR, defaultG, defaultB, defaultA);
    }

    @Override
    public void unsetDefaultColor() {
        consumer.unsetDefaultColor();
    }

}
