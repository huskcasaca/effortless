package dev.huskuraft.effortless.math;

public class Vector2d {

    public static final Vector2d ZERO = new Vector2d(0, 0);
    public static final Vector2d ONE = new Vector2d(1, 1);

    public static final Vector2d UNIT_X = new Vector2d(1, 0);
    public static final Vector2d UNIT_Z = new Vector2d(0, 1);
    public static final Vector2d UNIT_MINUS_X = new Vector2d(-1, 0);
    public static final Vector2d UNIT_MINUS_Z = new Vector2d(0, -1);

    private final double x;
    private final double z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param z the Z coordinate
     */
    public Vector2d(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public static Vector2d at(double x, double z) {
        return new Vector2d(x, z);
    }

    /**
     * Get the X coordinate.
     *
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector2d withX(double x) {
        return Vector2d.at(x, z);
    }

    /**
     * Get the Z coordinate.
     *
     * @return the z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector2d withZ(double z) {
        return Vector2d.at(x, z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d add(Vector2d other) {
        return add(other.x, other.z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param z the value to add
     * @return a new vector
     */
    public Vector2d add(double x, double z) {
        return Vector2d.at(this.x + x, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d add(Vector2d... others) {
        double newX = x;
        double newZ = z;

        for (Vector2d other : others) {
            newX += other.x;
            newZ += other.z;
        }

        return Vector2d.at(newX, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d subtract(Vector2d other) {
        return subtract(other.x, other.z);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param x the value to subtract
     * @param z the value to subtract
     * @return a new vector
     */
    public Vector2d subtract(double x, double z) {
        return Vector2d.at(this.x - x, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d subtract(Vector2d... others) {
        double newX = x;
        double newZ = z;

        for (Vector2d other : others) {
            newX -= other.x;
            newZ -= other.z;
        }

        return Vector2d.at(newX, newZ);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d multiply(Vector2d other) {
        return multiply(other.x, other.z);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param z the value to multiply
     * @return a new vector
     */
    public Vector2d multiply(double x, double z) {
        return Vector2d.at(this.x * x, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d multiply(Vector2d... others) {
        double newX = x;
        double newZ = z;

        for (Vector2d other : others) {
            newX *= other.x;
            newZ *= other.z;
        }

        return Vector2d.at(newX, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector2d multiply(double n) {
        return multiply(n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d divide(Vector2d other) {
        return divide(other.x, other.z);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param z the value to divide by
     * @return a new vector
     */
    public Vector2d divide(double x, double z) {
        return Vector2d.at(this.x / x, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector2d divide(double n) {
        return divide(n, n);
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
    public double lengthSq() {
        return x * x + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(Vector2d other) {
        return MathUtils.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public double distanceSq(Vector2d other) {
        double dx = other.x - x;
        double dz = other.z - z;
        return dx * dx + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector2d normalize() {
        return divide(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public double dot(Vector2d other) {
        return x * other.x + z * other.z;
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, and Z are the lowest)
     * @param max the maximum point (X, Y, and Z are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(Vector2d min, Vector2d max) {
        return x >= min.x && x <= max.x
                && z >= min.z && z <= max.z;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector2d floor() {
        return Vector2d.at(MathUtils.floor(x), MathUtils.floor(z));
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector2d ceil() {
        return Vector2d.at(MathUtils.ceil(x), MathUtils.ceil(z));
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector2d round() {
        return Vector2d.at(MathUtils.floor(x + 0.5), MathUtils.floor(z + 0.5));
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector2d abs() {
        return Vector2d.at(MathUtils.abs(x), MathUtils.abs(z));
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
    public Vector2d transform2D(double angle, double aboutX, double aboutZ, double translateX, double translateZ) {
        angle = MathUtils.rad(angle);
        double x = this.x - aboutX;
        double z = this.z - aboutZ;
        double cos = MathUtils.cos(angle);
        double sin = MathUtils.sin(angle);
        double x2 = x * cos - z * sin;
        double z2 = x * sin + z * cos;
        return new Vector2d(
                x2 + aboutX + translateX,
                z2 + aboutZ + translateZ);
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v2 the second vector
     * @return minimum
     */
    public Vector2d getMinimum(Vector2d v2) {
        return new Vector2d(
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
    public Vector2d getMaximum(Vector2d v2) {
        return new Vector2d(
                MathUtils.max(x, v2.x),
                MathUtils.max(z, v2.z)
        );
    }

    /**
     * Create a new {@link Vector2i} from this vector.
     *
     * @return a new {@link Vector2i}
     */
    public Vector2i toVector2i() {
        return Vector2i.at(x, z);
    }

    /**
     * Creates a 3D vector by adding a zero Y component to this vector.
     *
     * @return a new vector
     */
    public Vector3d toVector3() {
        return toVector3(0);
    }

    /**
     * Creates a 3D vector by adding the specified Y component to this vector.
     *
     * @param y the Y component
     * @return a new vector
     */
    public Vector3d toVector3(double y) {
        return Vector3d.at(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2d other)) {
            return false;
        }

        return other.x == this.x && other.z == this.z;

    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Double.hashCode(x);
        hash = 31 * hash + Double.hashCode(z);
        return hash;
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
