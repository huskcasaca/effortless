package dev.huskuraft.effortless.api.math;

import java.util.Comparator;
import java.util.stream.IntStream;

public class Vector3i {

    public static final Vector3i ZERO = new Vector3i(0, 0, 0);
    public static final Vector3i ONE = new Vector3i(1, 1, 1);

    public static final Vector3i UNIT_X = new Vector3i(1, 0, 0);
    public static final Vector3i UNIT_Y = new Vector3i(0, 1, 0);
    public static final Vector3i UNIT_Z = new Vector3i(0, 0, 1);
    public static final Vector3i UNIT_MINUS_X = new Vector3i(-1, 0, 0);
    public static final Vector3i UNIT_MINUS_Y = new Vector3i(0, -1, 0);
    public static final Vector3i UNIT_MINUS_Z = new Vector3i(0, 0, -1);

    protected final int x;
    protected final int y;
    protected final int z;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Comparator<Vector3i> sortByCoordsYzx() {
        return YzxOrderComparator.YZX_ORDER;
    }

    public static Vector3i at(double x, double y, double z) {
        return at((int) MathUtils.floor(x), (int) MathUtils.floor(y), (int) MathUtils.floor(z));
    }

    public static Vector3i at(int x, int y, int z) {
        return new Vector3i(x, y, z);
    }

    public static Vector3i at(Vector3d vector) {
        return at(vector.x(), vector.y(), vector.z());
    }

    public static Vector3i at(Vector3i vector) {
        return at(vector.x(), vector.y(), vector.z());
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector3i withX(int x) {
        return Vector3i.at(x, y, z);
    }

    /**
     * Set the Y coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public Vector3i withY(int y) {
        return Vector3i.at(x, y, z);
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector3i withZ(int z) {
        return Vector3i.at(x, y, z);
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
     * Get the Y coordinate.
     *
     * @return the y coordinate
     */
    public int y() {
        return y;
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
    public Vector3i add(Vector3i other) {
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
    public Vector3i add(int x, int y, int z) {
        return Vector3i.at(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3i add(Vector3i... others) {
        int newX = x;
        int newY = y;
        int newZ = z;

        for (Vector3i other : others) {
            newX += other.x;
            newY += other.y;
            newZ += other.z;
        }

        return Vector3i.at(newX, newY, newZ);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3i sub(Vector3i other) {
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
    public Vector3i sub(int x, int y, int z) {
        return Vector3i.at(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3i sub(Vector3i... others) {
        int newX = x;
        int newY = y;
        int newZ = z;

        for (Vector3i other : others) {
            newX -= other.x;
            newY -= other.y;
            newZ -= other.z;
        }

        return Vector3i.at(newX, newY, newZ);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3i mul(Vector3i other) {
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
    public Vector3i mul(int x, int y, int z) {
        return Vector3i.at(this.x * x, this.y * y, this.z * z);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector3i mul(Vector3i... others) {
        int newX = x;
        int newY = y;
        int newZ = z;

        for (Vector3i other : others) {
            newX *= other.x;
            newY *= other.y;
            newZ *= other.z;
        }

        return Vector3i.at(newX, newY, newZ);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector3i mul(int n) {
        return mul(n, n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector3i div(Vector3i other) {
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
    public Vector3i div(int x, int y, int z) {
        return Vector3i.at(this.x / x, this.y / y, this.z / z);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector3i div(int n) {
        return div(n, n, n);
    }

    /**
     * Shift all components right.
     *
     * @param x the value to shift x by
     * @param y the value to shift y by
     * @param z the value to shift z by
     * @return a new vector
     */
    public Vector3i shr(int x, int y, int z) {
        return at(this.x >> x, this.y >> y, this.z >> z);
    }

    /**
     * Shift all components right by {@code n}.
     *
     * @param n the value to shift by
     * @return a new vector
     */
    public Vector3i shr(int n) {
        return shr(n, n, n);
    }

    /**
     * Shift all components left.
     *
     * @param x the value to shift x by
     * @param y the value to shift y by
     * @param z the value to shift z by
     * @return a new vector
     */
    public Vector3i shl(int x, int y, int z) {
        return at(this.x << x, this.y << y, this.z << z);
    }

    /**
     * Shift all components left by {@code n}.
     *
     * @param n the value to shift by
     * @return a new vector
     */
    public Vector3i shl(int n) {
        return shl(n, n, n);
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
        return x * x + y * y + z * z;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(Vector3i other) {
        return MathUtils.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public int distanceSq(Vector3i other) {
        int dx = other.x - x;
        int dy = other.y - y;
        int dz = other.z - z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector3i normalize() {
        double len = length();
        double x = this.x / len;
        double y = this.y / len;
        double z = this.z / len;
        return Vector3i.at(x, y, z);
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public double dot(Vector3i other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Gets the cross product of this and another vector.
     *
     * @param other the other vector
     * @return the cross product of this and the other vector
     */
    public Vector3i cross(Vector3i other) {
        return new Vector3i(
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
    public boolean containedWithin(Vector3i min, Vector3i max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
    }

    /**
     * Clamp the Y component.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a new vector
     */
    public Vector3i clampY(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("minimum cannot be greater than maximum");
        }
        if (y < min) {
            return Vector3i.at(x, min, z);
        }
        if (y > max) {
            return Vector3i.at(x, max, z);
        }
        return this;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector3i floor() {
        // already floored, kept for feature parity with Vector3
        return this;
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector3i ceil() {
        // already raised, kept for feature parity with Vector3
        return this;
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector3i round() {
        // already rounded, kept for feature parity with Vector3
        return this;
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector3i abs() {
        return at(MathUtils.abs(x), MathUtils.abs(y), MathUtils.abs(z));
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
    public Vector3i getMinimum(Vector3i v2) {
        return new Vector3i(
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
    public Vector3i getMaximum(Vector3i v2) {
        return new Vector3i(
                MathUtils.max(x, v2.x),
                MathUtils.max(y, v2.y),
                MathUtils.max(z, v2.z)
        );
    }

    /**
     * Create a new {@code Vector3d} from this vector.
     *
     * @return a new {@code Vector3d}
     */
    public Vector3d toVector3d() {
        return Vector3d.at(x, y, z);
    }

    public IntStream stream() {
        return IntStream.of(x, y, z);
    }

    public int volume() {
        return x * y * z;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3i other)) {
            return false;
        }

        return other.x == this.x && other.y == this.y && other.z == this.z;
    }

    @Override
    public int hashCode() {
        return (x ^ (z << 12)) ^ (y << 24);
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
        private static final Comparator<Vector3i> YZX_ORDER =
                Comparator.comparingInt(Vector3i::y)
                        .thenComparingInt(Vector3i::z)
                        .thenComparingInt(Vector3i::x);
    }
}
