package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.math.Quaternion;
import dev.huskuraft.effortless.api.math.Matrix3f;
import dev.huskuraft.effortless.api.math.Matrix4f;
import dev.huskuraft.effortless.api.math.Quaternionf;

import java.nio.FloatBuffer;

public class MinecraftPrimitives {

    public static Quaternionf fromMinecraftQuaternion(Quaternion quaternion) {
        return new Quaternionf(quaternion.i(), quaternion.j(), quaternion.k(), quaternion.r());
    }

    public static Quaternion toMinecraftQuaternion(Quaternionf quaternion) {
        return new Quaternion(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    }

    public static Matrix3f fromMinecraftMatrix3f(com.mojang.math.Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.store(buffer);
        return new Matrix3f(buffer);
    }

    public static com.mojang.math.Matrix3f toMinecraftMatrix3f(Matrix3f matrix) {
        var buffer = FloatBuffer.allocate(9);
        matrix.write(buffer);
        var minecraftMatrix = new com.mojang.math.Matrix3f();
        minecraftMatrix.load(buffer);
        return minecraftMatrix;
    }

    public static Matrix4f fromMinecraftMatrix4f(com.mojang.math.Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.store(buffer);
        return new Matrix4f(buffer);
    }

    public static com.mojang.math.Matrix4f toMinecraftMatrix4f(Matrix4f matrix) {
        var buffer = FloatBuffer.allocate(16);
        matrix.write(buffer);
        var minecraftMatrix = new com.mojang.math.Matrix4f();
        minecraftMatrix.load(buffer);
        return minecraftMatrix;
    }

}
