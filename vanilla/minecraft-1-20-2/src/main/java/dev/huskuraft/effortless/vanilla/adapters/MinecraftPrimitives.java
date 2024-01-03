package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.math.Matrix3f;
import dev.huskuraft.effortless.math.Matrix4f;
import dev.huskuraft.effortless.math.Quaternionf;

import java.nio.FloatBuffer;

public class MinecraftPrimitives {

    public static Quaternionf fromMinecraftQuaternion(org.joml.Quaternionf quaternion) {
        return new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static org.joml.Quaternionf toMinecraftQuaternion(Quaternionf quaternion) {
        return new org.joml.Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static Matrix3f fromMinecraftMatrix3f(org.joml.Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.get(buffer);
        return new Matrix3f(buffer);
    }

    public static org.joml.Matrix3f toMinecraftMatrix3f(Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.write(buffer);
        return new org.joml.Matrix3f(buffer);
    }

    public static Matrix4f fromMinecraftMatrix4f(org.joml.Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.get(buffer);
        return new Matrix4f(buffer);
    }

    public static org.joml.Matrix4f toMinecraftMatrix4f(Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.write(buffer);
        return new org.joml.Matrix4f(buffer);
    }

}
