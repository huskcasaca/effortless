package dev.huskuraft.effortless.api.math;

import java.util.stream.IntStream;

public record Vector2i(int x, int y) {

    public static final Vector2i ZERO = new Vector2i(0, 0);
    public static final Vector2i ONE = new Vector2i(1, 1);

    public static final Vector2i UNIT_X = new Vector2i(1, 0);
    public static final Vector2i UNIT_Y = new Vector2i(0, 1);
    public static final Vector2i UNIT_MINUS_X = new Vector2i(-1, 0);
    public static final Vector2i UNIT_MINUS_Y = new Vector2i(0, -1);

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Vector2i {
    }

    public static Vector2i at(double x, double y) {
        return at((int) MathUtils.floor(x), (int) MathUtils.floor(y));
    }

    public static Vector2i at(int x, int y) {
        return new Vector2i(x, y);
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector2i withX(int x) {
        return Vector2i.at(x, y);
    }

    /**
     * Set the Z coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public Vector2i withY(int y) {
        return Vector2i.at(x, y);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2i add(Vector2i other) {
        return add(other.x, other.y);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param y the value to add
     * @return a new vector
     */
    public Vector2i add(int x, int y) {
        return Vector2i.at(this.x + x, this.y + y);
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
        int newY = y;

        for (Vector2i other : others) {
            newX += other.x;
            newY += other.y;
        }

        return Vector2i.at(newX, newY);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2i sub(Vector2i other) {
        return sub(other.x, other.y);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param x the value to subtract
     * @param y the value to subtract
     * @return a new vector
     */
    public Vector2i sub(int x, int y) {
        return Vector2i.at(this.x - x, this.y - y);
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
        int newY = y;

        for (Vector2i other : others) {
            newX -= other.x;
            newY -= other.y;
        }

        return Vector2i.at(newX, newY);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2i mul(Vector2i other) {
        return mul(other.x, other.y);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param y the value to multiply
     * @return a new vector
     */
    public Vector2i mul(int x, int y) {
        return Vector2i.at(this.x * x, this.y * y);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2i mul(Vector2i... others) {
        int newX = x;
        int newY = y;

        for (Vector2i other : others) {
            newX *= other.x;
            newY *= other.y;
        }

        return Vector2i.at(newX, newY);
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
        return div(other.x, other.y);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param y the value to divide by
     * @return a new vector
     */
    public Vector2i div(int x, int y) {
        return Vector2i.at(this.x / x, this.y / y);
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
     * @param x the value to shift X by
     * @param y the value to shift Y by
     * @return a new vector
     */
    public Vector2i shr(int x, int y) {
        return at(this.x >> x, this.y >> y);
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
        return x * x + y * y;
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
        int dy = other.y - y;
        return dx * dx + dy * dy;
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
        double y = this.y / len;
        return Vector2i.at(x, y);
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public int dot(Vector2i other) {
        return x * other.x + y * other.y;
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
                && y >= min.y && y <= max.y;
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
        return at(MathUtils.abs(x), MathUtils.abs(y));
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
                MathUtils.min(y, v2.y)
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
                MathUtils.max(y, v2.y)
        );
    }

    public Vector2d toVector2d() {
        return Vector2d.at(x, y);
    }

    public IntStream stream() {
        return IntStream.of(x, y);
    }

    public int volume() {
        return x * y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Returns a string representation that is supported by the parser.
     *
     * @return string
     */
    public String toParserString() {
        return x + "," + y;
    }
}
