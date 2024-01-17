package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskuraft.effortless.api.math.Matrix3f;
import dev.huskuraft.effortless.api.math.Matrix4f;
import dev.huskuraft.effortless.api.math.Quaternionf;
import dev.huskuraft.effortless.api.renderer.MatrixStack;

class MinecraftMatrixStack implements MatrixStack {

    private final PoseStack reference;

    MinecraftMatrixStack(PoseStack reference) {
        this.reference = reference;
    }

    @Override
    public PoseStack referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftMatrixStack matrixStack && reference.equals(matrixStack.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }


    @Override
    public void push() {
        reference.pushPose();
    }

    @Override
    public void pop() {
        reference.popPose();
    }

    @Override
    public Matrix last() {
        return new Matrix() {
            @Override
            public Matrix4f pose() {
                return MinecraftConvertor.fromPlatformMatrix4f(reference.last().pose());
            }

            @Override
            public Matrix3f normal() {
                return MinecraftConvertor.fromPlatformMatrix3f(reference.last().normal());
            }
        };
    }

    @Override
    public void translate(float x, float y, float z) {
        reference.translate(x, y, z);
    }

    @Override
    public void scale(float x, float y, float z) {
        reference.scale(x, y, z);
    }

    @Override
    public void rotate(Quaternionf quaternion) {
        reference.mulPose(MinecraftConvertor.toPlatformQuaternion(quaternion));
    }

    @Override
    public void rotate(Quaternionf quaternion, float x, float y, float z) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void multiply(Matrix4f matrix) {
        reference.mulPoseMatrix(MinecraftConvertor.toPlatformMatrix4f(matrix));
    }

    @Override
    public void identity() {
        reference.setIdentity();
    }
}
