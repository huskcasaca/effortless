package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;

public final class RenderUtils {

    private RenderUtils() {
    }

    public static Vector3d rotate(Vector3d vector, double degree, Axis axis) {
        if (degree == 0)
            return vector;
        if (vector == Vector3d.ZERO)
            return vector;

        float angle = (float) (degree / 180f * MathUtils.PI);
        double sin = MathUtils.sin(angle);
        double cos = MathUtils.cos(angle);
        double x = vector.x();
        double y = vector.y();
        double z = vector.z();

        if (axis == Axis.X)
            return new Vector3d(x, y * cos - z * sin, z * cos + y * sin);
        if (axis == Axis.Y)
            return new Vector3d(x * cos + z * sin, y, z * cos - x * sin);
        if (axis == Axis.Z)
            return new Vector3d(x * cos - y * sin, y * cos + x * sin, z);
        return vector;
    }

    public static Vector3d calculateAxisAlignedPlane(Vector3d vector) {
        vector = vector.normalize();
        return new Vector3d(1, 1, 1).sub(MathUtils.abs(vector.x()), MathUtils.abs(vector.y()), MathUtils.abs(vector.z()));
    }

    public static float fastInvCubeRoot(float number) {
        int i = Float.floatToIntBits(number);
        i = 1419967116 - i / 3;
        float f = Float.intBitsToFloat(i);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * number);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * number);
        return f;
    }

    public class ARGB32 {
        public ARGB32() {
        }

        public static int alpha(int packedColor) {
            return packedColor >>> 24;
        }

        public static int red(int packedColor) {
            return packedColor >> 16 & 255;
        }

        public static int green(int packedColor) {
            return packedColor >> 8 & 255;
        }

        public static int blue(int packedColor) {
            return packedColor & 255;
        }

        public static int color(int alpha, int red, int green, int blue) {
            return alpha << 24 | red << 16 | green << 8 | blue;
        }
    }

}
