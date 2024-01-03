package dev.huskuraft.effortless.math;

public class Vector2d {

    public static final Vector2d ZERO = new Vector2d(0, 0);
    public static final Vector2d ONE = new Vector2d(1, 1);

    public static final Vector2d UNIT_X = new Vector2d(1, 0);
    public static final Vector2d UNIT_Y = new Vector2d(0, 1);
    public static final Vector2d UNIT_MINUS_X = new Vector2d(-1, 0);
    public static final Vector2d UNIT_MINUS_Y = new Vector2d(0, -1);

    private final double x;
    private final double y;

    /**
     * Construct an instance.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2d at(double x, double y) {
        return new Vector2d(x, y);
    }

    /**
     * Set the X coordinate.
     *
     * @param x the new X
     * @return a new vector
     */
    public Vector2d withX(double x) {
        return Vector2d.at(x, y);
    }

    /**
     * Set the Y coordinate.
     *
     * @param y the new Y
     * @return a new vector
     */
    public Vector2d withY(double y) {
        return Vector2d.at(x, y);
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
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d add(Vector2d other) {
        return add(other.x, other.y);
    }

    /**
     * Add another vector to this vector and return the result as a new vector.
     *
     * @param x the value to add
     * @param y the value to add
     * @return a new vector
     */
    public Vector2d add(double x, double y) {
        return Vector2d.at(this.x + x, this.y + y);
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
        double newY = y;

        for (Vector2d other : others) {
            newX += other.x;
            newY += other.y;
        }

        return Vector2d.at(newX, newY);
    }

    /**
     * Subtract another vector from this vector and return the result
     * as a new vector.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d sub(Vector2d other) {
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
    public Vector2d sub(double x, double y) {
        return Vector2d.at(this.x - x, this.y - y);
    }

    /**
     * Subtract a list of vectors from this vector and return the result
     * as a new vector.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d sub(Vector2d... others) {
        double newX = x;
        double newY = y;

        for (Vector2d other : others) {
            newX -= other.x;
            newY -= other.y;
        }

        return Vector2d.at(newX, newY);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d mul(Vector2d other) {
        return mul(other.x, other.y);
    }

    /**
     * Multiply this vector by another vector on each component.
     *
     * @param x the value to multiply
     * @param y the value to multiply
     * @return a new vector
     */
    public Vector2d mul(double x, double y) {
        return Vector2d.at(this.x * x, this.y * y);
    }

    /**
     * Multiply this vector by zero or more vectors on each component.
     *
     * @param others an array of vectors
     * @return a new vector
     */
    public Vector2d mul(Vector2d... others) {
        double newX = x;
        double newY = y;

        for (Vector2d other : others) {
            newX *= other.x;
            newY *= other.y;
        }

        return Vector2d.at(newX, newY);
    }

    /**
     * Perform scalar multiplication and return a new vector.
     *
     * @param n the value to multiply
     * @return a new vector
     */
    public Vector2d mul(double n) {
        return mul(n, n);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param other the other vector
     * @return a new vector
     */
    public Vector2d div(Vector2d other) {
        return div(other.x, other.y);
    }

    /**
     * Divide this vector by another vector on each component.
     *
     * @param x the value to divide by
     * @param y the value to divide by
     * @return a new vector
     */
    public Vector2d div(double x, double y) {
        return Vector2d.at(this.x / x, this.y / y);
    }

    /**
     * Perform scalar division and return a new vector.
     *
     * @param n the value to divide by
     * @return a new vector
     */
    public Vector2d div(double n) {
        return div(n, n);
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
        return x * x + y * y;
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
        double dy = other.y - y;
        return dx * dx + dy * dy;
    }

    /**
     * Get the normalized vector, which is the vector divided by its
     * length, as a new vector.
     *
     * @return a new vector
     */
    public Vector2d normalize() {
        return div(length());
    }

    /**
     * Gets the dot product of this and another vector.
     *
     * @param other the other vector
     * @return the dot product of this and the other vector
     */
    public double dot(Vector2d other) {
        return x * other.x + y * other.y;
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
                && y >= min.y && y <= max.y;
    }

    /**
     * Floors the values of all components.
     *
     * @return a new vector
     */
    public Vector2d floor() {
        return Vector2d.at(MathUtils.floor(x), MathUtils.floor(y));
    }

    /**
     * Rounds all components up.
     *
     * @return a new vector
     */
    public Vector2d ceil() {
        return Vector2d.at(MathUtils.ceil(x), MathUtils.ceil(y));
    }

    /**
     * Rounds all components to the closest integer.
     *
     * <p>Components &lt; 0.5 are rounded down, otherwise up.</p>
     *
     * @return a new vector
     */
    public Vector2d round() {
        return Vector2d.at(MathUtils.floor(x + 0.5), MathUtils.floor(y + 0.5));
    }

    /**
     * Returns a vector with the absolute values of the components of
     * this vector.
     *
     * @return a new vector
     */
    public Vector2d abs() {
        return Vector2d.at(MathUtils.abs(x), MathUtils.abs(y));
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
                MathUtils.min(y, v2.y)
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
                MathUtils.max(y, v2.y)
        );
    }

    /**
     * Create a new {@link Vector2i} from this vector.
     *
     * @return a new {@link Vector2i}
     */
    public Vector2i toVector2i() {
        return Vector2i.at(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2d other)) {
            return false;
        }

        return other.x == this.x && other.y == this.y;

    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Double.hashCode(x);
        hash = 31 * hash + Double.hashCode(y);
        return hash;
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
