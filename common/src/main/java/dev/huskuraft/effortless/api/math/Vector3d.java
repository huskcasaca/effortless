package dev.huskuraft.effortless.api.math;

import java.util.Comparator;

public class Vector3d {

    public static final Vector3d ZERO = new Vector3d(0, 0, 0);
    public static final Vector3d ONE = new Vector3d(1, 1, 1);

    public static final Vector3d UNIT_X = new Vector3d(1, 0, 0);
    public static final Vector3d UNIT_Y = new Vector3d(0, 1, 0);
    public static final Vector3d UNIT_Z = new Vector3d(0, 0, 1);
    public static final Vector3d UNIT_MINUS_X = new Vector3d(-1, 0, 0);
    public static final Vector3d UNIT_MINUS_Y = new Vector3d(0, -1, 0);
    public static final Vector3d UNIT_MINUS_Z = new Vector3d(0, 0, -1);

    protected final double x;
    protected final double y;
    protected final double z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Comparator<Vector3d> sortByCoordsYzx() {
        return YzxOrderComparator.YZX_ORDER;
    }

    public static Vector3d at(double x, double y, double z) {
        return new Vector3d(x, y, z);
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector3d withX(double x) {
        return Vector3d.at(x, y, z);
    }

    /**
     * Set the Y coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public Vector3d withY(double y) {
        return Vector3d.at(x, y, z);
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector3d withZ(double z) {
        return Vector3d.at(x, y, z);
    }

    /**
     * Get the X coordinate.
     *
     * @return the x coordinate
     */
    public double x() {
        return x;
    }

    /**
     * Get the Y coordinate.
     *
     * @return the y coordinate
     */
    public double y() {
        return y;
    }

    /**
     * Get the Z coordinate.
     *
     * @return the z coordinate
     */
    public double z() {
        return z;
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3d add(Vector3d other) {
        return add(other.x, other.y, other.z);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param y the value to add
     * @param z the value to add
     * @return a new vector
     */
    public Vector3d add(double x, double y, double z) {
        return Vector3d.at(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3d add(Vector3d... others) {
        double newX = x;
        double newY = y;
        double newZ = z;

        for (Vector3d other : others) {
            newX += other.x;
            newY += other.y;
            newZ += other.z;
        }

        return Vector3d.at(newX, newY, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3d sub(Vector3d other) {
        return sub(other.x, other.y, other.z);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param x the value to subtract
     * @param y the value to subtract
     * @param z the value to subtract
     * @return a new vector
     */
    public Vector3d sub(double x, double y, double z) {
        return Vector3d.at(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3d sub(Vector3d... others) {
        double newX = x;
        double newY = y;
        double newZ = z;

        for (Vector3d other : others) {
            newX -= other.x;
            newY -= other.y;
            newZ -= other.z;
        }

        return Vector3d.at(newX, newY, newZ);
    }


    /**
     * Reverse this vector.
     *
     * @return a new vector
     */
    public Vector3d reverse() {
        return mul(-1.0);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3d mul(Vector3d other) {
        return mul(other.x, other.y, other.z);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param y the value to multiply
     * @param z the value to multiply
     * @return a new vector
     */
    public Vector3d mul(double x, double y, double z) {
        return Vector3d.at(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3d mul(Vector3d... others) {
        double newX = x;
        double newY = y;
        double newZ = z;

        for (Vector3d other : others) {
            newX *= other.x;
            newY *= other.y;
            newZ *= other.z;
        }

        return Vector3d.at(newX, newY, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector3d mul(double n) {
        return mul(n, n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3d div(Vector3d other) {
        return div(other.x, other.y, other.z);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param y the value to divide by
     * @param z the value to divide by
     * @return a new vector
     */
    public Vector3d div(double x, double y, double z) {
        return Vector3d.at(this.x / x, this.y / y, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector3d div(double n) {
        return div(n, n, n);
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
        return x * x + y * y + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(Vector3d other) {
        return MathUtils.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public double distanceSq(Vector3d other) {
        double dx = other.x - x;
        double dy = other.y - y;
        double dz = other.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector3d normalize() {
        return div(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public double dot(Vector3d other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Gets the cross product of this and another vector.
     *
     * @param other the other vector
     * @return the cross product of this and the other vector
     */
    public Vector3d cross(Vector3d other) {
        return new Vector3d(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, and Z are the lowest)
     * @param max the maximum point (X, Y, and Z are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(Vector3d min, Vector3d max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
    }

    /**
     * Clamp the Y component.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a new vector
     */
    public Vector3d clampY(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("minimum cannot be greater than maximum");
        }
        if (y < min) {
            return Vector3d.at(x, min, z);
        }
        if (y > max) {
            return Vector3d.at(x, max, z);
        }
        return this;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector3d floor() {
        return Vector3d.at(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector3d ceil() {
        return Vector3d.at(MathUtils.ceil(x), MathUtils.ceil(y), MathUtils.ceil(z));
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector3d round() {
        return Vector3d.at(MathUtils.floor(x + 0.5), MathUtils.floor(y + 0.5), MathUtils.floor(z + 0.5));
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector3d abs() {
        return Vector3d.at(MathUtils.abs(x), MathUtils.abs(y), MathUtils.abs(z));
    }

    /**
     * Get this vector's pitch as used within the game.
     *
     * @return pitch in radians
     */
    public double toPitch() {
        double x = x();
        double z = z();

        if (x == 0 && z == 0) {
            return y() > 0 ? -90 : 90;
        } else {
            double x2 = x * x;
            double z2 = z * z;
            double xz = MathUtils.sqrt(x2 + z2);
            return MathUtils.deg(MathUtils.atan(-y() / xz));
        }
    }

    /**
     * Get this vector's yaw as used within the game.
     *
     * @return yaw in radians
     */
    public double toYaw() {
        double x = x();
        double z = z();

        double t = MathUtils.atan2(-x, z);
        double tau = 2 * MathUtils.PI;

        return MathUtils.deg(((t + tau) % tau));
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v2 the second vector
     * @return minimum
     */
    public Vector3d getMinimum(Vector3d v2) {
        return new Vector3d(
                MathUtils.min(x, v2.x),
                MathUtils.min(y, v2.y),
                MathUtils.min(z, v2.z)
        );
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v2 the second vector
     * @return maximum
     */
    public Vector3d getMaximum(Vector3d v2) {
        return new Vector3d(
                MathUtils.max(x, v2.x),
                MathUtils.max(y, v2.y),
                MathUtils.max(z, v2.z)
        );
    }

    /**
     * Create a new {@code Vector3i} from this vector.
     *
     * @return a new {@code Vector3i}
     */
    public Vector3i toVector3i() {
        return Vector3i.at(x, y, z);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3d other)) {
            return false;
        }

        return other.x == this.x && other.y == this.y && other.z == this.z;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Double.hashCode(x);
        hash = 31 * hash + Double.hashCode(y);
        hash = 31 * hash + Double.hashCode(z);
        return hash;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    /**
     * Returns a string representation that is supported by the parser.
     *
     * @return string
     */
    public String toParserString() {
        return x + "," + y + "," + z;
    }

    private static final class YzxOrderComparator {
        private static final Comparator<Vector3d> YZX_ORDER = Comparator
                .comparingDouble(Vector3d::y)
                .thenComparingDouble(Vector3d::z)
                .thenComparingDouble(Vector3d::x);
    }
}
