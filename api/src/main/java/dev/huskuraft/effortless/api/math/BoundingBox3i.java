package dev.huskuraft.effortless.api.math;

import java.util.Objects;
import java.util.function.Consumer;

public class BoundingBox3i {

    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public BoundingBox3i(int x0, int y0, int z0, int x1, int y1, int z1) {
        this.minX = MathUtils.min(x0, x1);
        this.minY = MathUtils.min(y0, y1);
        this.minZ = MathUtils.min(z0, z1);
        this.maxX = MathUtils.max(x0, x1);
        this.maxY = MathUtils.max(y0, y1);
        this.maxZ = MathUtils.max(z0, z1);

    }

    public BoundingBox3i(Vector3i vector) {
        this(vector.x(), vector.y(), vector.z(), vector.x(), vector.y(), vector.z());
    }

    public static BoundingBox3i fromCorners(Vector3i start, Vector3i end) {
        return new BoundingBox3i(MathUtils.min(start.x(), end.x()), MathUtils.min(start.y(), end.y()), MathUtils.min(start.z(), end.z()), MathUtils.max(start.x(), end.x()), MathUtils.max(start.y(), end.y()), MathUtils.max(start.z(), end.z()));
    }

    public static BoundingBox3i infinite() {
        return new BoundingBox3i(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public BoundingBox3i withMinX(int a) {
        return new BoundingBox3i(a, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public BoundingBox3i withMinY(int a) {
        return new BoundingBox3i(this.minX, a, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public BoundingBox3i withMinZ(int a) {
        return new BoundingBox3i(this.minX, this.minY, a, this.maxX, this.maxY, this.maxZ);
    }

    public BoundingBox3i withMaxX(int a) {
        return new BoundingBox3i(this.minX, this.minY, this.minZ, a, this.maxY, this.maxZ);
    }

    public BoundingBox3i withMaxY(int a) {
        return new BoundingBox3i(this.minX, this.minY, this.minZ, this.maxX, a, this.maxZ);
    }

    public BoundingBox3i withMaxZ(int a) {
        return new BoundingBox3i(this.minX, this.minY, this.minZ, this.maxX, this.maxY, a);
    }

    public BoundingBox3i move(int x, int y, int z) {
        return new BoundingBox3i(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public BoundingBox3i move(Vector3i vector) {
        return this.move(vector.x(), vector.y(), vector.z());
    }

    public boolean intersects(BoundingBox3i boundingBox) {
        return this.maxX >= boundingBox.minX && this.minX <= boundingBox.maxX && this.maxZ >= boundingBox.minZ && this.minZ <= boundingBox.maxZ && this.maxY >= boundingBox.minY && this.minY <= boundingBox.maxY;
    }

    public boolean intersects(int i, int j, int k, int l) {
        return this.maxX >= i && this.minX <= k && this.maxZ >= j && this.minZ <= l;
    }

    public boolean contains(Vector3i vector) {
        return contains(vector.x(), vector.y(), vector.z());
    }

    public boolean contains(int x, int y, int z) {
        return x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ && y >= this.minY && y <= this.maxY;
    }

    public Vector3i getSize() {
        return new Vector3i(getXSize(), getYSize(), getZSize());
    }

    public int getXSize() {
        return this.maxX - this.minX;
    }

    public int getYSize() {
        return this.maxY - this.minY;
    }

    public int getZSize() {
        return this.maxZ - this.minZ;
    }

    public int getXSpan() {
        return this.maxX - this.minX + 1;
    }

    public int getYSpan() {
        return this.maxY - this.minY + 1;
    }

    public int getZSpan() {
        return this.maxZ - this.minZ + 1;
    }

    public Vector3i getCenter() {
        return new Vector3i(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
    }

    public void forAllCorners(Consumer<Vector3i> consumer) {
        consumer.accept(Vector3i.at(this.maxX, this.maxY, this.maxZ));
        consumer.accept(Vector3i.at(this.minX, this.maxY, this.maxZ));
        consumer.accept(Vector3i.at(this.maxX, this.minY, this.maxZ));
        consumer.accept(Vector3i.at(this.minX, this.minY, this.maxZ));
        consumer.accept(Vector3i.at(this.maxX, this.maxY, this.minZ));
        consumer.accept(Vector3i.at(this.minX, this.maxY, this.minZ));
        consumer.accept(Vector3i.at(this.maxX, this.minY, this.minZ));
        consumer.accept(Vector3i.at(this.minX, this.minY, this.minZ));
    }

    public String toString() {
        return "BoundingBox3i[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof BoundingBox3i boundingBox)) {
            return false;
        } else {
            return this.minX == boundingBox.minX && this.minY == boundingBox.minY && this.minZ == boundingBox.minZ && this.maxX == boundingBox.maxX && this.maxY == boundingBox.maxY && this.maxZ == boundingBox.maxZ;
        }
    }

    public int hashCode() {
        return Objects.hash(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public int minX() {
        return this.minX;
    }

    public int minY() {
        return this.minY;
    }

    public int minZ() {
        return this.minZ;
    }

    public int maxX() {
        return this.maxX;
    }

    public int maxY() {
        return this.maxY;
    }

    public int maxZ() {
        return this.maxZ;
    }

    public BoundingBox3d toBoundingBox3d() {
        return new BoundingBox3d(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
}
