package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.renderer.Uniform;

public interface MinecraftUniform extends Uniform {

    @Override
    com.mojang.blaze3d.shaders.Uniform referenceValue();

    @Override
    default void set(float x) {
        referenceValue().set(x);
    }

    @Override
    default void set(float x, float y) {
        referenceValue().set(x, y);
    }

    @Override
    default void set(float x, float y, float z) {
        referenceValue().set(x, y, z);
    }

    @Override
    default void set(float x, float y, float z, float w) {
        referenceValue().set(x, y, z, w);
    }

    @Override
    default void setSafe(float x, float y, float z, float w) {
        referenceValue().setSafe(x, y, z, w);
    }

    @Override
    default void setSafe(int x, int y, int z, int w) {
        referenceValue().setSafe(x, y, z, w);
    }

    @Override
    default void set(int x) {
        referenceValue().set(x);
    }

    @Override
    default void set(int x, int y) {
        referenceValue().set(x, y);
    }

    @Override
    default void set(int x, int y, int z) {
        referenceValue().set(x, y, z);
    }

    @Override
    default void set(int x, int y, int z, int w) {
        referenceValue().set(x, y, z, w);
    }

    @Override
    default void set(float[] values) {
        referenceValue().set(values);
    }

    @Override
    default void setMatrix22(float m00, float m01, float m10, float m11) {
        referenceValue().setMat2x2(m00, m01, m10, m11);
    }

    @Override
    default void setMatrix23(float m00, float m01, float m02, float m10, float m11, float m12) {
        referenceValue().setMat2x3(m00, m01, m02, m10, m11, m12);
    }

    @Override
    default void setMatrix24(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
        referenceValue().setMat2x4(m00, m01, m02, m03, m10, m11, m12, m13);
    }

    @Override
    default void setMatrix32(float m00, float m01, float m10, float m11, float m20, float m21) {
        referenceValue().setMat3x2(m00, m01, m10, m11, m20, m21);
    }

    @Override
    default void setMatrix33(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        referenceValue().setMat3x3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Override
    default void setMatrix34(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
        referenceValue().setMat3x4(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23);
    }

    @Override
    default void setMatrix42(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
        referenceValue().setMat4x2(m00, m01, m02, m03, m10, m11, m12, m13);
    }

    @Override
    default void setMatrix43(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
        referenceValue().setMat4x3(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23);
    }

    @Override
    default void setMatrix44(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        referenceValue().setMat4x4(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

}
