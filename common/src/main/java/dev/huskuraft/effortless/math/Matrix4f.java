package dev.huskuraft.effortless.math;

import java.nio.FloatBuffer;

public record Matrix4f(
        float m00, float m01, float m02, float m03,
        float m10, float m11, float m12, float m13,
        float m20, float m21, float m22, float m23,
        float m30, float m31, float m32, float m33
) {

    /**
     * Bit returned by {@link #properties()} to indicate that the matrix represents a perspective transformation.
     */
    public static byte PROPERTY_PERSPECTIVE = 1<<0;
    /**
     * Bit returned by {@link #properties()} to indicate that the matrix represents an affine transformation.
     */
    public static byte PROPERTY_AFFINE = 1<<1;
    /**
     * Bit returned by {@link #properties()} to indicate that the matrix represents the identity transformation.
     */
    public static byte PROPERTY_IDENTITY = 1<<2;
    /**
     * Bit returned by {@link #properties()} to indicate that the matrix represents a pure translation transformation.
     */
    public static byte PROPERTY_TRANSLATION = 1<<3;
    /**
     * Bit returned by {@link #properties()} to indicate that the upper-left 3x3 submatrix represents an orthogonal
     * matrix (i.e. orthonormal basis). For practical reasons, this property also always implies
     * {@link #PROPERTY_AFFINE} in this implementation.
     */
    public static byte PROPERTY_ORTHONORMAL = 1<<4;

    public Matrix4f(FloatBuffer buffer) {
        this(
                buffer.get(0), buffer.get(1), buffer.get(2), buffer.get(3),
                buffer.get(4), buffer.get(5), buffer.get(6), buffer.get(7),
                buffer.get(8), buffer.get(9), buffer.get(10), buffer.get(11),
                buffer.get(12), buffer.get(13), buffer.get(14), buffer.get(15)
        );
    }

    public int properties() {
        int properties = 0;
        if (m03() == 0.0f && m13() == 0.0f) {
            if (m23() == 0.0f && m33() == 1.0f) {
                properties |= PROPERTY_AFFINE;
                if (m00() == 1.0f && m01() == 0.0f && m02() == 0.0f && m10() == 0.0f && m11() == 1.0f && m12() == 0.0f
                        && m20() == 0.0f && m21() == 0.0f && m22() == 1.0f) {
                    properties |= PROPERTY_TRANSLATION | PROPERTY_ORTHONORMAL;
                    if (m30() == 0.0f && m31() == 0.0f && m32() == 0.0f)
                        properties |= PROPERTY_IDENTITY;
                }
                /*
                 * We do not determine orthogonality, since it would require arbitrary epsilons
                 * and is rather expensive (6 dot products) in the worst case.
                 */
            } else if (m01() == 0.0f && m02() == 0.0f && m10() == 0.0f && m12() == 0.0f && m20() == 0.0f && m21() == 0.0f
                    && m30() == 0.0f && m31() == 0.0f && m33() == 0.0f) {
                properties |= PROPERTY_PERSPECTIVE;
            }
        }
        return properties;
    }

    public Vector4f mul(Vector4f vector) {
        return (properties() & PROPERTY_AFFINE) != 0 ? mulAffine(vector) : mulGeneric(vector);
    }

    private Vector4f mulAffine(Vector4f vector) {
        return new Vector4f(
                Math.fma(m00(), vector.x(), Math.fma(m10(), vector.y(), Math.fma(m20(), vector.z(), m30() * vector.w()))),
                Math.fma(m01(), vector.x(), Math.fma(m11(), vector.y(), Math.fma(m21(), vector.z(), m31() * vector.w()))),
                Math.fma(m02(), vector.x(), Math.fma(m12(), vector.y(), Math.fma(m22(), vector.z(), m32() * vector.w()))),
                vector.w()
        );
    }

    private Vector4f mulGeneric(Vector4f vector) {
        return new Vector4f(
                Math.fma(m00(), vector.x(), Math.fma(m10(), vector.y(), Math.fma(m20(), vector.z(), m30() * vector.w()))),
                Math.fma(m01(), vector.x(), Math.fma(m11(), vector.y(), Math.fma(m21(), vector.z(), m31() * vector.w()))),
                Math.fma(m02(), vector.x(), Math.fma(m12(), vector.y(), Math.fma(m22(), vector.z(), m32() * vector.w()))),
                Math.fma(m03(), vector.x(), Math.fma(m13(), vector.y(), Math.fma(m23(), vector.z(), m33() * vector.w())))
        );
    }

    public void write(FloatBuffer buffer) {
        buffer.put(new float[] {
                m00(), m01(), m02(), m03(),
                m10(), m11(), m12(), m13(),
                m20(), m21(), m22(), m23(),
                m30(), m31(), m32(), m33()
        });
    }


}
