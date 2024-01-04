package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskuraft.effortless.api.math.Matrix3f;
import dev.huskuraft.effortless.api.math.Matrix4f;
import dev.huskuraft.effortless.api.math.Quaternionf;
import dev.huskuraft.effortless.api.renderer.MatrixStack;

public class MinecraftMatrixStack implements MatrixStack {

    private final PoseStack reference;

    MinecraftMatrixStack(PoseStack reference) {
        this.reference = reference;
    }

    public static MatrixStack fromMinecraftMatrixStack(PoseStack matrixStack) {
        return new MinecraftMatrixStack(matrixStack);
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
                return MinecraftPrimitives.fromMinecraftMatrix4f(reference.last().pose());
            }

            @Override
            public Matrix3f normal() {
                return MinecraftPrimitives.fromMinecraftMatrix3f(reference.last().normal());
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
        reference.mulPose(MinecraftPrimitives.toMinecraftQuaternion(quaternion));
    }

    @Override
    public void rotate(Quaternionf quaternion, float x, float y, float z) {
        reference.rotateAround(MinecraftPrimitives.toMinecraftQuaternion(quaternion), x, y, z);
    }

    @Override
    public void multiply(Matrix4f matrix) {
        reference.mulPoseMatrix(MinecraftPrimitives.toMinecraftMatrix4f(matrix));
    }

    @Override
    public void identity() {
        reference.setIdentity();
    }
}
