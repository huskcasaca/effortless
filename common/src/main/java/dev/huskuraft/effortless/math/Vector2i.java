package dev.huskuraft.effortless.math;

public class Vector2i {

    public static final Vector2i ZERO = new Vector2i(0, 0);
    public static final Vector2i ONE = new Vector2i(1, 1);

    public static final Vector2i UNIT_X = new Vector2i(1, 0);
    public static final Vector2i UNIT_Z = new Vector2i(0, 1);
    public static final Vector2i UNIT_MINUS_X = new Vector2i(-1, 0);
    public static final Vector2i UNIT_MINUS_Z = new Vector2i(0, -1);

    private final int x;
    private final int z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public Vector2i(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static Vector2i at(double x, double z) {
        return at((int) MathUtils.floor(x), (int) MathUtils.floor(z));
    }

    public static Vector2i at(int x, int z) {
        return new Vector2i(x, z);
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector2i withX(int x) {
        return Vector2i.at(x, z);
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector2i withZ(int z) {
        return Vector2i.at(x, z);
    }

    /**
     * Get the X coordinate.
     *
     * @return the x coordinate
     */
    public int x() {
        return x;
    }

    /**
     * Get the Z coordinate.
     *
     * @return the z coordinate
     */
    public int z() {
        return z;
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2i add(Vector2i other) {
        return add(other.x, other.z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param z the value to add
     * @return a new vector
     */
    public Vector2i add(int x, int z) {
        return Vector2i.at(this.x + x, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2i add(Vector2i... others) {
        int newX = x;
        int newZ = z;

        for (Vector2i other : others) {
            newX += other.x;
            newZ += other.z;
        }

        return Vector2i.at(newX, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2i sub(Vector2i other) {
        return sub(other.x, other.z);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param x the value to subtract
     * @param z the value to subtract
     * @return a new vector
     */
    public Vector2i sub(int x, int z) {
        return Vector2i.at(this.x - x, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2i sub(Vector2i... others) {
        int newX = x;
        int newZ = z;

        for (Vector2i other : others) {
            newX -= other.x;
            newZ -= other.z;
        }

        return Vector2i.at(newX, newZ);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2i mul(Vector2i other) {
        return mul(other.x, other.z);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param z the value to multiply
     * @return a new vector
     */
    public Vector2i mul(int x, int z) {
        return Vector2i.at(this.x * x, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2i mul(Vector2i... others) {
        int newX = x;
        int newZ = z;

        for (Vector2i other : others) {
            newX *= other.x;
            newZ *= other.z;
        }

        return Vector2i.at(newX, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector2i mul(int n) {
        return mul(n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2i div(Vector2i other) {
        return div(other.x, other.z);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param z the value to divide by
     * @return a new vector
     */
    public Vector2i div(int x, int z) {
        return Vector2i.at(this.x / x, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector2i div(int n) {
        return div(n, n);
    }

    /**
     * Shift all components right.
     *
     * @param x the value to shift x by
     * @param z the value to shift z by
     * @return a new vector
     */
    public Vector2i shr(int x, int z) {
        return at(this.x >> x, this.z >> z);
    }

    /**
     * Shift all components right by {@code n}.
     *
     * @param n the value to shift by
     * @return a new vector
     */
    public Vector2i shr(int n) {
        return shr(n, n);
    }

    /**
     * Get the length of the vector.
     *
     * @return length
     */
    public double length() {
        return MathUtils.sqrt(lengthSq());
    }

    /**
     * Get the length, squared, of the vector.
     *
     * @return length, squared
     */
    public int lengthSq() {
        return x * x + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(Vector2i other) {
        return MathUtils.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public int distanceSq(Vector2i other) {
        int dx = other.x - x;
        int dz = other.z - z;
        return dx * dx + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector2i normalize() {
        double len = length();
        double x = this.x / len;
        double z = this.z / len;
        return Vector2i.at(x, z);
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public int dot(Vector2i other) {
        return x * other.x + z * other.z;
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, and Z are the lowest)
     * @param max the maximum point (X, Y, and Z are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(Vector2i min, Vector2i max) {
        return x >= min.x && x <= max.x
                && z >= min.z && z <= max.z;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector2i floor() {
        // already floored, kept for feature parity with Vector2
        return this;
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector2i ceil() {
        // already raised, kept for feature parity with Vector2
        return this;
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector2i round() {
        // already rounded, kept for feature parity with Vector2
        return this;
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector2i abs() {
        return Vector2i.at(MathUtils.abs(x), MathUtils.abs(z));
    }

    /**
     * Perform a 2D transformation on this vector and return a new one.
     *
     * @param angle      in degrees
     * @param aboutX     about which x coordinate to rotate
     * @param aboutZ     about which z coordinate to rotate
     * @param translateX what to add after rotation
     * @param translateZ what to add after rotation
     * @return a new vector
     */
    public Vector2i transform2D(double angle, double aboutX, double aboutZ, double translateX, double translateZ) {
        angle = MathUtils.rad(angle);
        double x = this.x - aboutX;
        double z = this.z - aboutZ;
        double cos = MathUtils.cos(angle);
        double sin = MathUtils.sin(angle);
        double x2 = x * cos - z * sin;
        double z2 = x * sin + z * cos;
        return Vector2i.at(
                x2 + aboutX + translateX,
                z2 + aboutZ + translateZ);
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v2 the second vector
     * @return minimum
     */
    public Vector2i getMinimum(Vector2i v2) {
        return new Vector2i(
                MathUtils.min(x, v2.x),
                MathUtils.min(z, v2.z)
        );
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v2 the second vector
     * @return maximum
     */
    public Vector2i getMaximum(Vector2i v2) {
        return new Vector2i(
                MathUtils.max(x, v2.x),
                MathUtils.max(z, v2.z)
        );
    }

    public Vector2d toVector2d() {
        return Vector2d.at(x, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2i other)) {
            return false;
        }

        return other.x == this.x && other.z == this.z;

    }

    @Override
    public int hashCode() {
        return (x << 16) ^ z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }

    /**
     * Returns a string representation that is supported by the parser.
     *
     * @return string
     */
    public String toParserString() {
        return x + "," + z;
    }
}
