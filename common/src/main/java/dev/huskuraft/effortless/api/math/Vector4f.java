package dev.huskuraft.effortless.api.math;

public class Vector4f {

    public static final Vector4f ZERO = new Vector4f(0, 0, 0, 0);
    public static final Vector4f ONE = new Vector4f(1, 1, 1, 1);

    public static final Vector4f UNIT_X = new Vector4f(1, 0, 0, 0);
    public static final Vector4f UNIT_Y = new Vector4f(0, 1, 0, 0);
    public static final Vector4f UNIT_Z = new Vector4f(0, 0, 1, 0);
    public static final Vector4f UNIT_W = new Vector4f(0, 0, 0, 1);
    public static final Vector4f UNIT_MINUS_X = new Vector4f(-1, 0, 0, 0);
    public static final Vector4f UNIT_MINUS_Y = new Vector4f(0, -1, 0, 0);
    public static final Vector4f UNIT_MINUS_Z = new Vector4f(0, 0, -1, 0);
    public static final Vector4f UNIT_MINUS_W = new Vector4f(0, 0, 0, -1);

    protected final float x;
    protected final float y;
    protected final float z;
    protected final float w;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @param w the W coordinate
     */
    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vector4f at(double x, double y, double z, double w) {
        return new Vector4f((float) x, (float) y, (float) z, (float) w);
    }

    public static Vector4f at(float x, float y, float z, float w) {
        return new Vector4f(x, y, z, w);
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector4f withX(float x) {
        return Vector4f.at(x, y, z, w);
    }

    /**
     * Set the Y coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public Vector4f withY(float y) {
        return Vector4f.at(x, y, z, w);
    }

    /**
     * Set the Z coordinate.
     *
     * @param z the new Z
     * @return a new vector
     */
    public Vector4f withZ(float z) {
        return Vector4f.at(x, y, z, w);
    }

    /**
     * Set the W coordinate.
     *
     * @param w the new W
     * @return a new vector
     */
    public Vector4f withW(float w) {
        return Vector4f.at(x, y, z, w);
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
     * Get the W coordinate.
     *
     * @return the w coordinate
     */
    public float w() {
        return w;
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4f add(Vector4f other) {
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
    public Vector4f add(float x, float y, float z, float w)  {
        return Vector4f.at(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    /**
     * Add a list of vectors to this vector and return the
     * result as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector4f add(Vector4f... others) {
        float newX = x;
        float newY = y;
        float newZ = z;
        float newW = w;

        for (Vector4f other : others) {
            newX += other.x;
            newY += other.y;
            newZ += other.z;
            newW += other.w;
        }

        return Vector4f.at(newX, newY, newZ, newW);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4f sub(Vector4f other) {
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
    public Vector4f sub(float x, float y, float z, float w)  {
        return Vector4f.at(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector4f sub(Vector4f... others) {
        float newX = x;
        float newY = y;
        float newZ = z;
        float newW = w;

        for (Vector4f other : others) {
            newX -= other.x;
            newY -= other.y;
            newZ -= other.z;
            newW -= other.w;
        }

        return Vector4f.at(newX, newY, newZ, newW);
    }


    /**
     * Reverse this vector.
     *
     * @return a new vector
     */
    public Vector4f reverse() {
        return mul(-1f);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4f mul(Vector4f other) {
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
    public Vector4f mul(float x, float y, float z, float w)  {
        return Vector4f.at(this.x * x, this.y * y, this.z * z, this.w * w);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector4f mul(Vector4f... others) {
        float newX = x;
        float newY = y;
        float newZ = z;
        float newW = w;

        for (Vector4f other : others) {
            newX *= other.x;
            newY *= other.y;
            newZ *= other.z;
            newW *= other.w;
        }

        return Vector4f.at(newX, newY, newZ, newW);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector4f mul(float n) {
        return mul(n, n, n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector4f div(Vector4f other) {
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
    public Vector4f div(float x, float y, float z, float w)  {
        return Vector4f.at(this.x / x, this.y / y, this.z / z, this.w / w);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector4f div(float n) {
        return div(n, n, n, n);
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
        return x * x + y * y + z * z + w * w;
    }

    /**
     * Get the distance between this vector and another vector.
     *
     * @param other the other vector
     * @return distance
     */
    public float distance(Vector4f other) {
        return MathUtils.sqrt(distanceSq(other));
    }

    /**
     * Get the distance between this vector and another vector, squared.
     *
     * @param other the other vector
     * @return distance
     */
    public float distanceSq(Vector4f other) {
        float dx = other.x - x;
        float dy = other.y - y;
        float dz = other.z - z;
        float dw = other.w - w;
        return dx * dx + dy * dy + dz * dz + dw * dw;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector4f normalize() {
        return div(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public float dot(Vector4f other) {
        return x * other.x + y * other.y + z * other.z + w * other.w;
    }

    /**
     * Checks to see if a vector is contained with another.
     *
     * @param min the minimum point (X, Y, X, and W are the lowest)
     * @param max the maximum point (X, Y, X, and W are the lowest)
     * @return true if the vector is contained
     */
    public boolean containedWithin(Vector4f min, Vector4f max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z && w >= min.w && w <= max.w;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector4f floor() {
        return at(MathUtils.floor(x), MathUtils.floor(y), MathUtils.floor(z), MathUtils.floor(w));
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector4f ceil() {
        return at(MathUtils.ceil(x), MathUtils.ceil(y), MathUtils.ceil(z), MathUtils.ceil(w));
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector4f round() {
        return at(MathUtils.floor(x + 0.5), MathUtils.floor(y + 0.5), MathUtils.floor(z + 0.5), MathUtils.floor(w + 0.5));
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector4f abs() {
        return at(MathUtils.abs(x), MathUtils.abs(y), MathUtils.abs(z), MathUtils.abs(w));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector4f other)) {
            return false;
        }

        return other.x == this.x && other.y == this.y && other.z == this.z && other.w == this.w;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Float.hashCode(x);
        hash = 31 * hash + Float.hashCode(y);
        hash = 31 * hash + Float.hashCode(z);
        hash = 31 * hash + Float.hashCode(w);
        return hash;
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
