package dev.huskuraft.effortless.api.math;

import java.nio.FloatBuffer;

public record Matrix3f(
        float m00, float m01, float m02,
        float m10, float m11, float m12,
        float m20, float m21, float m22
) {

    public Matrix3f(FloatBuffer buffer) {
        this(
                buffer.get(0), buffer.get(1), buffer.get(2),
                buffer.get(3), buffer.get(4), buffer.get(5),
                buffer.get(6), buffer.get(7), buffer.get(8)
        );
    }

    public Vector3f mul(Vector3f vector) {
        return new Vector3f(
                Math.fma(m00(), vector.x(), Math.fma(m10(), vector.y(), m20() * vector.z())),
                Math.fma(m01(), vector.x(), Math.fma(m11(), vector.y(), m21() * vector.z())),
                Math.fma(m02(), vector.x(), Math.fma(m12(), vector.y(), m22() * vector.z()))
        );
    }

    public void write(FloatBuffer buffer) {
        buffer.put(new float[] {
                m00(), m01(), m02(),
                m10(), m11(), m12(),
                m20(), m21(), m22()
        });
    }

}
