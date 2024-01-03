package dev.huskuraft.effortless.math;

public class Vector4i {

    public static final Vector4i ZERO = new Vector4i(0, 0, 0, 0);
    public static final Vector4i ONE = new Vector4i(1, 1, 1, 0);

    public static final Vector4i UNIT_X = new Vector4i(1, 0, 0, 0);
    public static final Vector4i UNIT_Y = new Vector4i(0, 1, 0, 0);
    public static final Vector4i UNIT_Z = new Vector4i(0, 0, 1, 0);
    public static final Vector4i UNIT_W = new Vector4i(0, 0, 0, 1);
    public static final Vector4i UNIT_MINUS_X = new Vector4i(-1, 0, 0, 0);
    public static final Vector4i UNIT_MINUS_Y = new Vector4i(0, -1, 0, 0);
    public static final Vector4i UNIT_MINUS_Z = new Vector4i(0, 0, -1, 0);
    public static final Vector4i UNIT_MINUS_W = new Vector4i(0, 0, 0, -1);

    protected final int x;
    protected final int y;
    protected final int z;
    protected final int w;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param w the W coordinate
     */
    public Vector4i(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vector4i at(double x, double y, double z, double w) {
        return at((int) MathUtils.floor(x), (int) MathUtils.floor(y), (int) MathUtils.floor(z), (int) MathUtils.floor(w));
    }

    public static Vector4i at(int x, int y, int z, int w) {
        return new Vector4i(x, y, z, w);
    }

    public static Vector4i at(Vector4d vector) {
        return at(vector.x(), vector.y(), vector.z(), vector.w());
    }

    public static Vector4i at(Vector4i vector) {
        return at(vector.x(), vector.y(), vector.z(), vector.w());
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector4i withX(int x) {
        return Vector4i.at(x, y, z, w);
    }

    /**
     * Set the Y coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public Vector4i withY(int y) {
        return Vector4i.at(x, y, z, w);
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector4i withZ(int z) {
        return Vector4i.at(x, y, z, w);
    }

    /**
     * Set the W coordinate.
     *
     * @param w the new W
     * @return a new vector
     */
    public Vector4i withW(int w) {
        return Vector4i.at(x, y, z, w);
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
     * Get the W coordinate.
     *
     * @return the w coordinate
     */
    public int w() {
        return w;
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4i add(Vector4i other) {
        return add(other.x, other.y, other.z, other.w);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param y the value to add
     * @param z the value to add
     * @param w the value to add
     * @return a new vector
     */
    public Vector4i add(int x, int y, int z, int w) {
        return Vector4i.at(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector4i add(Vector4i... others) {
        int newX = x;
        int newY = y;
        int newZ = z;
        int newW = w;

        for (Vector4i other : others) {
            newX += other.x;
            newY += other.y;
            newZ += other.z;
            newW += other.w;
        }

        return Vector4i.at(newX, newY, newZ, newW);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4i sub(Vector4i other) {
        return sub(other.x, other.y, other.z, other.w);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param x the value to subtract
     * @param y the value to subtract
     * @param z the value to subtract
     * @param w the value to subtract
     * @return a new vector
     */
    public Vector4i sub(int x, int y, int z, int w) {
        return Vector4i.at(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector4i sub(Vector4i... others) {
        int newX = x;
        int newY = y;
        int newZ = z;
        int newW = w;

        for (Vector4i other : others) {
            newX -= other.x;
            newY -= other.y;
            newZ -= other.z;
            newW -= other.w;
        }

        return Vector4i.at(newX, newY, newZ, newW);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4i mul(Vector4i other) {
        return mul(other.x, other.y, other.z, other.w);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param y the value to multiply
     * @param z the value to multiply
     * @param w the value to multiply
     * @return a new vector
     */
    public Vector4i mul(int x, int y, int z, int w) {
        return Vector4i.at(this.x * x, this.y * y, this.z * z, this.w * w);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector4i mul(Vector4i... others) {
        int newX = x;
        int newY = y;
        int newZ = z;
        int newW = w;

        for (Vector4i other : others) {
            newX *= other.x;
            newY *= other.y;
            newZ *= other.z;
            newW *= other.w;
        }

        return Vector4i.at(newX, newY, newZ, newW);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector4i mul(int n) {
        return mul(n, n, n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4i div(Vector4i other) {
        return div(other.x, other.y, other.z, other.w);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param y the value to divide by
     * @param z the value to divide by
     * @param w the value to divide by
     * @return a new vector
     */
    public Vector4i div(int x, int y, int z, int w) {
        return Vector4i.at(this.x / x, this.y / y, this.z / z, this.w / w);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector4i div(int n) {
        return div(n, n, n, n);
    }

    /**
     * Shift all components right.
     *
     * @param x the value to shift x by
     * @param y the value to shift y by
     * @param z the value to shift z by
     * @param w the value to shift z by
     * @return a new vector
     */
    public Vector4i shr(int x, int y, int z, int w) {
        return at(this.x >> x, this.y >> y, this.z >> z, this.w >> w);
    }

    /**
     * Shift all components right by {@code n}.
     *
     * @param n the value to shift by
     * @return a new vector
     */
    public Vector4i shr(int n) {
        return shr(n, n, n, n);
    }

    /**
     * Shift all components left.
     *
     * @param x the value to shift x by
     * @param y the value to shift y by
     * @param z the value to shift z by
     * @param w the value to shift w by
     * @return a new vector
     */
    public Vector4i shl(int x, int y, int z, int w) {
        return at(this.x << x, this.y << y, this.z << z, this.w << w);
    }

    /**
     * Shift all components left by {@code n}.
     *
     * @param n the value to shift by
     * @return a new vector
     */
    public Vector4i shl(int n) {
        return shl(n, n, n, n);
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
        return x * x + y * y + z * z + w * w;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public double distance(Vector4i other) {
        return MathUtils.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public int distanceSq(Vector4i other) {
        int dx = other.x - x;
        int dy = other.y - y;
        int dz = other.z - z;
        int dw = other.w - w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector4i normalize() {
        double len = length();
        double x = this.x / len;
        double y = this.y / len;
        double z = this.z / len;
        double w = this.w / len;
        return Vector4i.at(x, y, z, w);
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public double dot(Vector4i other) {
        return x * other.x + y * other.y + z * other.z + w * other.w;
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, and Z are the lowest)
     * @param max the maximum point (X, Y, and Z are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(Vector4i min, Vector4i max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z && w >= min.w;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector4i floor() {
        // already floored, kept for feature parity with Vector3
        return this;
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector4i ceil() {
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
    public Vector4i round() {
        // already rounded, kept for feature parity with Vector3
        return this;
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector4i abs() {
        return at(MathUtils.abs(x), MathUtils.abs(y), MathUtils.abs(z), MathUtils.abs(w));
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v2 the second vector
     * @return minimum
     */
    public Vector4i getMinimum(Vector4i v2) {
        return new Vector4i(
                MathUtils.min(x, v2.x),
                MathUtils.min(y, v2.y),
                MathUtils.min(z, v2.z),
                MathUtils.min(w, v2.w));
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v2 the second vector
     * @return maximum
     */
    public Vector4i getMaximum(Vector4i v2) {
        return new Vector4i(
                MathUtils.max(x, v2.x),
                MathUtils.max(y, v2.y),
                MathUtils.max(z, v2.z),
                MathUtils.max(w, v2.w));
    }

    /**
     * Create a new {@code Vector4d} from this vector.
     *
     * @return a new {@code Vector4d}
     */
    public Vector4d toVector4d() {
        return Vector4d.at(x, y, z, w);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector4i other)) {
            return false;
        }

        return other.x == this.x && other.y == this.y && other.z == this.z && other.w == this.w;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + w;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    /**
     * Returns a string representation that is supported by the parser.
     *
     * @return string
     */
    public String toParserString() {
        return x + "," + y + "," + z + "," + w;
    }
}
