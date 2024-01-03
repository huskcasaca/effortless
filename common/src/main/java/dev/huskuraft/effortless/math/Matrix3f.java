package dev.huskuraft.effortless.math;

public record Matrix3f(
        float m00, float m01, float m02,
        float m10, float m11, float m12,
        float m20, float m21, float m22
) {

    public Vector3f mul(Vector3f vector) {
        return new Vector3f(
                Math.fma(m00(), vector.x(), Math.fma(m10(), vector.y(), m20() * vector.z())),
                Math.fma(m01(), vector.x(), Math.fma(m11(), vector.y(), m21() * vector.z())),
                Math.fma(m02(), vector.x(), Math.fma(m12(), vector.y(), m22() * vector.z()))
        );
    }

}
