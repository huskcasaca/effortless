package dev.huskuraft.effortless.api.math;

import java.util.Comparator;
import java.util.stream.DoubleStream;

public class Vector3f {

    public static final Vector3f ZERO = new Vector3f(0, 0, 0);
    public static final Vector3f ONE = new Vector3f(1, 1, 1);

    public static final Vector3f UNIT_X = new Vector3f(1, 0, 0);
    public static final Vector3f UNIT_Y = new Vector3f(0, 1, 0);
    public static final Vector3f UNIT_Z = new Vector3f(0, 0, 1);
    public static final Vector3f UNIT_MINUS_X = new Vector3f(-1, 0, 0);
    public static final Vector3f UNIT_MINUS_Y = new Vector3f(0, -1, 0);
    public static final Vector3f UNIT_MINUS_Z = new Vector3f(0, 0, -1);

    protected final float x;
    protected final float y;
    protected final float z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Comparator<Vector3f> sortByCoordsYzx() {
        return YzxOrderComparator.YZX_ORDER;
    }

    public static Vector3f at(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

    public static Vector3f at(double x, double y, double z) {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector3f withX(float x) {
        return Vector3f.at(x, y, z);
    }

    /**
     * Set the Y coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public Vector3f withY(float y) {
        return Vector3f.at(x, y, z);
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector3f withZ(float z) {
        return Vector3f.at(x, y, z);
    }

    /**
     * Get the X coordinate.
     *
     * @return the x coordinate
     */
    public float x() {
        return x;
    }

    /**
     * Get the Y coordinate.
     *
     * @return the y coordinate
     */
    public float y() {
        return y;
    }

    /**
     * Get the Z coordinate.
     *
     * @return the z coordinate
     */
    public float z() {
        return z;
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3f add(Vector3f other) {
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
    public Vector3f add(float x, float y, float z) {
        return Vector3f.at(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3f add(Vector3f... others) {
        float newX = x;
        float newY = y;
        float newZ = z;

        for (Vector3f other : others) {
            newX += other.x;
            newY += other.y;
            newZ += other.z;
        }

        return Vector3f.at(newX, newY, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3f sub(Vector3f other) {
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
    public Vector3f sub(float x, float y, float z) {
        return Vector3f.at(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3f sub(Vector3f... others) {
        float newX = x;
        float newY = y;
        float newZ = z;

        for (Vector3f other : others) {
            newX -= other.x;
            newY -= other.y;
            newZ -= other.z;
        }

        return Vector3f.at(newX, newY, newZ);
    }


    /**
     * Reverse this vector.
     *
     * @return a new vector
     */
    public Vector3f reverse() {
        return mul(-1f);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3f mul(Vector3f other) {
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
    public Vector3f mul(float x, float y, float z) {
        return Vector3f.at(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3f mul(Vector3f... others) {
        float newX = x;
        float newY = y;
        float newZ = z;

        for (Vector3f other : others) {
            newX *= other.x;
            newY *= other.y;
            newZ *= other.z;
        }

        return Vector3f.at(newX, newY, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector3f mul(float n) {
        return mul(n, n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3f div(Vector3f other) {
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
    public Vector3f div(float x, float y, float z) {
        return Vector3f.at(this.x / x, this.y / y, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector3f div(float n) {
        return div(n, n, n);
    }

    /**
     * Get the length of the vector.
     *
     * @return length
     */
    public float length() {
        return MathUtils.sqrt(lengthSq());
    }

    /**
     * Get the length, squared, of the vector.
     *
     * @return length, squared
     */
    public float lengthSq() {
        return x * x + y * y + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public float distance(Vector3f other) {
        return MathUtils.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public float distanceSq(Vector3f other) {
        float dx = other.x - x;
        float dy = other.y - y;
        float dz = other.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector3f normalize() {
        return div(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public float dot(Vector3f other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Gets the cross product of this and another vector.
     *
     * @param other the other vector
     * @return the cross product of this and the other vector
     */
    public Vector3f cross(Vector3f other) {
        return new Vector3f(
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
    public boolean containedWithin(Vector3f min, Vector3f max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
    }

    /**
     * Clamp the Y component.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a new vector
     */
    public Vector3f clampY(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("minimum cannot be greater than maximum");
        }
        if (y < min) {
            return Vector3f.at(x, min, z);
        }
        if (y > max) {
            return Vector3f.at(x, max, z);
        }
        return this;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector3f floor() {
        return at(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z));
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector3f ceil() {
        return at(MathUtils.ceil(x), MathUtils.ceil(y), MathUtils.ceil(z));
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector3f round() {
        return at(MathUtils.floor(x + 0.5), MathUtils.floor(y + 0.5), MathUtils.floor(z + 0.5));
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector3f abs() {
        return at(MathUtils.abs(x), MathUtils.abs(y), MathUtils.abs(z));
    }

    /**
     * Get this vector's pitch as used within the game.
     *
     * @return pitch in radians
     */
    public double toPitch() {
        float x = x();
        float z = z();

        if (x == 0 && z == 0) {
            return y() > 0 ? -90 : 90;
        } else {
            float x2 = x * x;
            float z2 = z * z;
            float xz = MathUtils.sqrt(x2 + z2);
            return MathUtils.deg(MathUtils.atan(-y() / xz));
        }
    }

    /**
     * Get this vector's yaw as used within the game.
     *
     * @return yaw in radians
     */
    public double toYaw() {
        float x = x();
        float z = z();

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
    public Vector3f getMinimum(Vector3f v2) {
        return new Vector3f(
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
    public Vector3f getMaximum(Vector3f v2) {
        return new Vector3f(
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

    public DoubleStream stream() {
        return DoubleStream.of(x, y, z);
    }

    public double volume() {
        return x * y * z;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3f other)) {
            return false;
        }

        return other.x == this.x && other.y == this.y && other.z == this.z;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Float.hashCode(x);
        hash = 31 * hash + Float.hashCode(y);
        hash = 31 * hash + Float.hashCode(z);
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
        private static final Comparator<Vector3f> YZX_ORDER = Comparator
                .comparingDouble(Vector3f::y)
                .thenComparingDouble(Vector3f::z)
                .thenComparingDouble(Vector3f::x);
    }
}
