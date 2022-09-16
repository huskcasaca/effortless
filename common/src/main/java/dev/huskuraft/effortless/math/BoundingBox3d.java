package dev.huskuraft.effortless.math;

import java.util.Objects;
import java.util.function.Consumer;

public class BoundingBox3d {

    private static final double EPSILON = 1.0E-7;

    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public BoundingBox3d(double x0, double y0, double z0, double x1, double y1, double z1) {
        this.minX = MathUtils.min(x0, x1);
        this.minY = MathUtils.min(y0, y1);
        this.minZ = MathUtils.min(z0, z1);
        this.maxX = MathUtils.max(x0, x1);
        this.maxY = MathUtils.max(y0, y1);
        this.maxZ = MathUtils.max(z0, z1);
    }

    public BoundingBox3d(Vector3i vector) {
        this(vector.getX(), vector.getY(), vector.getZ(), vector.getX() + 1, vector.getY() + 1, vector.getZ() + 1);
    }

    public BoundingBox3d(Vector3i start, Vector3i end) {
        this(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
    }

    public BoundingBox3d(Vector3d start, Vector3d end) {
        this(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
    }

    public static BoundingBox3d of(BoundingBox3i boundingBox) {
        return new BoundingBox3d(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX() + 1, boundingBox.maxY() + 1, boundingBox.maxZ() + 1);
    }

    public static BoundingBox3d unitCubeFromLowerCorner(Vector3d vector) {
        return new BoundingBox3d(vector.getX(), vector.getY(), vector.getZ(), vector.getX() + 1.0, vector.getY() + 1.0, vector.getZ() + 1.0);
    }

    public static BoundingBox3d ofSize(Vector3d vector, double x, double y, double z) {
        return new BoundingBox3d(vector.getX() - x / 2.0, vector.getY() - y / 2.0, vector.getZ() - z / 2.0, vector.getX() + x / 2.0, vector.getY() + y / 2.0, vector.getZ() + z / 2.0);
    }

    public BoundingBox3d withMinX(double a) {
        return new BoundingBox3d(a, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public BoundingBox3d withMinY(double a) {
        return new BoundingBox3d(this.minX, a, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public BoundingBox3d withMinZ(double a) {
        return new BoundingBox3d(this.minX, this.minY, a, this.maxX, this.maxY, this.maxZ);
    }

    public BoundingBox3d withMaxX(double a) {
        return new BoundingBox3d(this.minX, this.minY, this.minZ, a, this.maxY, this.maxZ);
    }

    public BoundingBox3d withMaxY(double a) {
        return new BoundingBox3d(this.minX, this.minY, this.minZ, this.maxX, a, this.maxZ);
    }

    public BoundingBox3d withMaxZ(double a) {
        return new BoundingBox3d(this.minX, this.minY, this.minZ, this.maxX, this.maxY, a);
    }

    public BoundingBox3d move(double x, double y, double z) {
        return new BoundingBox3d(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public BoundingBox3d move(Vector3i vector) {
        return new BoundingBox3d(this.minX + (double) vector.getX(), this.minY + (double) vector.getY(), this.minZ + (double) vector.getZ(), this.maxX + (double) vector.getX(), this.maxY + (double) vector.getY(), this.maxZ + (double) vector.getZ());
    }

    public BoundingBox3d move(Vector3d vector) {
        return this.move(vector.getX(), vector.getY(), vector.getZ());
    }

    public BoundingBox3d contract(double x, double y, double z) {
        double g = this.minX;
        double h = this.minY;
        double i = this.minZ;
        double j = this.maxX;
        double k = this.maxY;
        double l = this.maxZ;
        if (x < 0.0) {
            g -= x;
        } else if (x > 0.0) {
            j -= x;
        }

        if (y < 0.0) {
            h -= y;
        } else if (y > 0.0) {
            k -= y;
        }

        if (z < 0.0) {
            i -= z;
        } else if (z > 0.0) {
            l -= z;
        }

        return new BoundingBox3d(g, h, i, j, k, l);
    }

    public BoundingBox3d expandTowards(Vector3d vector) {
        return this.expandTowards(vector.getX(), vector.getY(), vector.getZ());
    }

    public BoundingBox3d expandTowards(double x, double y, double z) {
        double g = this.minX;
        double h = this.minY;
        double i = this.minZ;
        double j = this.maxX;
        double k = this.maxY;
        double l = this.maxZ;
        if (x < 0.0) {
            g += x;
        } else if (x > 0.0) {
            j += x;
        }

        if (y < 0.0) {
            h += y;
        } else if (y > 0.0) {
            k += y;
        }

        if (z < 0.0) {
            i += z;
        } else if (z > 0.0) {
            l += z;
        }

        return new BoundingBox3d(g, h, i, j, k, l);
    }

    public BoundingBox3d intersect(BoundingBox3d boundingBox) {
        double d = MathUtils.max(this.minX, boundingBox.minX);
        double e = MathUtils.max(this.minY, boundingBox.minY);
        double f = MathUtils.max(this.minZ, boundingBox.minZ);
        double g = MathUtils.min(this.maxX, boundingBox.maxX);
        double h = MathUtils.min(this.maxY, boundingBox.maxY);
        double i = MathUtils.min(this.maxZ, boundingBox.maxZ);
        return new BoundingBox3d(d, e, f, g, h, i);
    }

    public BoundingBox3d minmax(BoundingBox3d boundingBox) {
        double d = MathUtils.min(this.minX, boundingBox.minX);
        double e = MathUtils.min(this.minY, boundingBox.minY);
        double f = MathUtils.min(this.minZ, boundingBox.minZ);
        double g = MathUtils.max(this.maxX, boundingBox.maxX);
        double h = MathUtils.max(this.maxY, boundingBox.maxY);
        double i = MathUtils.max(this.maxZ, boundingBox.maxZ);
        return new BoundingBox3d(d, e, f, g, h, i);
    }

    public boolean intersects(BoundingBox3d boundingBox) {
        return this.intersects(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
    }

    public boolean intersects(double d, double e, double f, double g, double h, double i) {
        return this.minX < g && this.maxX > d && this.minY < h && this.maxY > e && this.minZ < i && this.maxZ > f;
    }

    public boolean intersects(Vector3d start, Vector3d end) {
        return this.intersects(MathUtils.min(start.getX(), end.getX()), MathUtils.min(start.getY(), end.getY()), MathUtils.min(start.getZ(), end.getZ()), MathUtils.max(start.getX(), end.getX()), MathUtils.max(start.getY(), end.getY()), MathUtils.max(start.getZ(), end.getZ()));
    }

    public boolean contains(Vector3d vector) {
        return this.contains(vector.getX(), vector.getY(), vector.getZ());
    }

    public boolean contains(double d, double e, double f) {
        return d >= this.minX && d < this.maxX && e >= this.minY && e < this.maxY && f >= this.minZ && f < this.maxZ;
    }

    public Vector3d getSize() {
        return new Vector3d(getXSize(), getYSize(), getZSize());
    }

    public double getXSize() {
        return this.maxX - this.minX;
    }

    public double getYSize() {
        return this.maxY - this.minY;
    }

    public double getZSize() {
        return this.maxZ - this.minZ;
    }

    public Vector3d getCenter() {
        return new Vector3d(MathUtils.lerp(0.5, this.minX, this.maxX), MathUtils.lerp(0.5, this.minY, this.maxY), MathUtils.lerp(0.5, this.minZ, this.maxZ));
    }

    public void forAllCorners(Consumer<Vector3d> consumer) {
        consumer.accept(Vector3d.at(this.maxX, this.maxY, this.maxZ));
        consumer.accept(Vector3d.at(this.minX, this.maxY, this.maxZ));
        consumer.accept(Vector3d.at(this.maxX, this.minY, this.maxZ));
        consumer.accept(Vector3d.at(this.minX, this.minY, this.maxZ));
        consumer.accept(Vector3d.at(this.maxX, this.maxY, this.minZ));
        consumer.accept(Vector3d.at(this.minX, this.maxY, this.minZ));
        consumer.accept(Vector3d.at(this.maxX, this.minY, this.minZ));
        consumer.accept(Vector3d.at(this.minX, this.minY, this.minZ));
    }

    public BoundingBox3d inflate(double d, double e, double f) {
        double g = this.minX - d;
        double h = this.minY - e;
        double i = this.minZ - f;
        double j = this.maxX + d;
        double k = this.maxY + e;
        double l = this.maxZ + f;
        return new BoundingBox3d(g, h, i, j, k, l);
    }

    public BoundingBox3d inflate(double d) {
        return this.inflate(d, d, d);
    }


    public BoundingBox3d deflate(double d, double e, double f) {
        return this.inflate(-d, -e, -f);
    }

    public BoundingBox3d deflate(double d) {
        return this.inflate(-d);
    }

    public double distanceSq(Vector3d vector) {
        double d = MathUtils.max(MathUtils.max(this.minX - vector.getX(), vector.getX() - this.maxX), 0.0);
        double e = MathUtils.max(MathUtils.max(this.minY - vector.getY(), vector.getY() - this.maxY), 0.0);
        double f = MathUtils.max(MathUtils.max(this.minZ - vector.getZ(), vector.getZ() - this.maxZ), 0.0);
        return d * d + e * e + f * f;
    }

    public String toString() {
        return "BoundingBox3d[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof BoundingBox3d boundingBox)) {
            return false;
        } else {
            return this.minX == boundingBox.minX && this.minY == boundingBox.minY && this.minZ == boundingBox.minZ && this.maxX == boundingBox.maxX && this.maxY == boundingBox.maxY && this.maxZ == boundingBox.maxZ;
        }
    }

//	public int hashCode() {
//		long l = Double.doubleToLongBits(this.minX);
//		int i = (int)(l ^ l >>> 32);
//		l = Double.doubleToLongBits(this.minY);
//		i = 31 * i + (int)(l ^ l >>> 32);
//		l = Double.doubleToLongBits(this.minZ);
//		i = 31 * i + (int)(l ^ l >>> 32);
//		l = Double.doubleToLongBits(this.maxX);
//		i = 31 * i + (int)(l ^ l >>> 32);
//		l = Double.doubleToLongBits(this.maxY);
//		i = 31 * i + (int)(l ^ l >>> 32);
//		l = Double.doubleToLongBits(this.maxZ);
//		i = 31 * i + (int)(l ^ l >>> 32);
//		return i;
//	}

    public int hashCode() {
        return Objects.hash(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public boolean hasNaN() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }
}
