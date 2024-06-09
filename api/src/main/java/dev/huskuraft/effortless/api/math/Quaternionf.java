package dev.huskuraft.effortless.api.math;

public record Quaternionf(float x, float y, float z, float w) {

    /**
     * Create a new {@link Quaternionf} and initialize its components to the given values.
     *
     * @param x the first component of the imaginary part
     * @param y the second component of the imaginary part
     * @param z the third component of the imaginary part
     * @param w the real part
     */
    public Quaternionf {
    }

    public static Quaternionf rotate(Vector3f vector3f, float angle, boolean isAngle) {
        if (isAngle) {
            angle *= Math.PI / 180;
        }
        return new Quaternionf(
                (float) (vector3f.x() * Math.sin(angle / 2.0F)),
                (float) (vector3f.y() * Math.sin(angle / 2.0F)),
                (float) (vector3f.z() * Math.sin(angle / 2.0F)),
                (float) Math.cos(angle / 2.0F)
        );
    }


}
