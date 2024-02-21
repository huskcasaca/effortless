package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.math.Matrix3f;
import dev.huskuraft.effortless.api.math.Matrix4f;
import dev.huskuraft.effortless.api.math.Vector3f;
import dev.huskuraft.effortless.api.math.Vector4f;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Uniform extends PlatformReference {

    void set(float x);

    void set(float x, float y);

    void set(float x, float y, float z);

    void set(float x, float y, float z, float w);

    void setSafe(float x, float y, float z, float w);

    void setSafe(int x, int y, int z, int w);

    void set(int x);

    void set(int x, int y);

    void set(int x, int y, int z);

    void set(int x, int y, int z, int w);

    void set(float[] values);

    default void set(Vector3f vector) {
        set(vector.x(), vector.y(), vector.z());
    }

    default void set(Vector4f vector) {
        set(vector.x(), vector.y(), vector.z(), vector.w());
    }

    void setMatrix22(float m00, float m01, float m10, float m11);

    void setMatrix23(float m00, float m01, float m02, float m10, float m11, float m12);

    void setMatrix24(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13);

    void setMatrix32(float m00, float m01, float m10, float m11, float m20, float m21);

    void setMatrix33(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22);

    void setMatrix34(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23);

    void setMatrix42(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13);

    void setMatrix43(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23);

    void setMatrix44(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33);

    default void set(Matrix4f matrix) {
        setMatrix44(matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(),
                matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(),
                matrix.m20(), matrix.m21(), matrix.m22(), matrix.m23(),
                matrix.m30(), matrix.m31(), matrix.m32(), matrix.m33());
    }

    default void set(Matrix3f matrix) {
        setMatrix33(matrix.m00(), matrix.m01(), matrix.m02(),
                matrix.m10(), matrix.m11(), matrix.m12(),
                matrix.m20(), matrix.m21(), matrix.m22());
    }

}
