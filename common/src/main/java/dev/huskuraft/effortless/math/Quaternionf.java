package dev.huskuraft.effortless.math;

public class Quaternionf {

    /**
     * The first component of the vector part.
     */
    public final float x;
    /**
     * The second component of the vector part.
     */
    public final float y;
    /**
     * The third component of the vector part.
     */
    public final float z;
    /**
     * The real/scalar part of the quaternion.
     */
    public final float w;

    /**
     * Create a new {@link Quaternionf} and initialize its components to the given values.
     *
     * @param x the first component of the imaginary part
     * @param y the second component of the imaginary part
     * @param z the third component of the imaginary part
     * @param w the real part
     */
    public Quaternionf(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * @return the first component of the vector part
     */
    public float x() {
        return this.x;
    }

    /**
     * @return the second component of the vector part
     */
    public float y() {
        return this.y;
    }

    /**
     * @return the third component of the vector part
     */
    public float z() {
        return this.z;
    }

    /**
     * @return the real/scalar part of the quaternion
     */
    public float w() {
        return this.w;
    }

}
