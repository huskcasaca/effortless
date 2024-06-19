package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.api.renderer.Uniform;

public record MinecraftUniform(
        com.mojang.blaze3d.shaders.Uniform refs
) implements Uniform {

    @Override
    public void set(float x) {
        refs.set(x);
    }

    @Override
    public void set(float x, float y) {
        refs.set(x, y);
    }

    @Override
    public void set(float x, float y, float z) {
        refs.set(x, y, z);
    }

    @Override
    public void set(float x, float y, float z, float w) {
        refs.set(x, y, z, w);
    }

    @Override
    public void setSafe(float x, float y, float z, float w) {
        refs.setSafe(x, y, z, w);
    }

    @Override
    public void setSafe(int x, int y, int z, int w) {
        refs.setSafe(x, y, z, w);
    }

    @Override
    public void set(int x) {
        refs.set(x);
    }

    @Override
    public void set(int x, int y) {
        refs.set(x, y);
    }

    @Override
    public void set(int x, int y, int z) {
        refs.set(x, y, z);
    }

    @Override
    public void set(int x, int y, int z, int w) {
        refs.set(x, y, z, w);
    }

    @Override
    public void set(float[] values) {
        refs.set(values);
    }

    @Override
    public void setMatrix22(float m00, float m01, float m10, float m11) {
        refs.setMat2x2(m00, m01, m10, m11);
    }

    @Override
    public void setMatrix23(float m00, float m01, float m02, float m10, float m11, float m12) {
        refs.setMat2x3(m00, m01, m02, m10, m11, m12);
    }

    @Override
    public void setMatrix24(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
        refs.setMat2x4(m00, m01, m02, m03, m10, m11, m12, m13);
    }

    @Override
    public void setMatrix32(float m00, float m01, float m10, float m11, float m20, float m21) {
        refs.setMat3x2(m00, m01, m10, m11, m20, m21);
    }

    @Override
    public void setMatrix33(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        refs.setMat3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Override
    public void setMatrix34(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
        refs.setMat3x4(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23);
    }

    @Override
    public void setMatrix42(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
        refs.setMat4x2(m00, m01, m02, m03, m10, m11, m12, m13);
    }

    @Override
    public void setMatrix43(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
        refs.setMat4x3(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23);
    }

    @Override
    public void setMatrix44(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        refs.setMat4x4(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

}
