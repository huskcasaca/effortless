package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector2d;
import dev.huskuraft.effortless.api.math.Vector2i;

public record ChunkPosition(int x, int z) {

    public static final ChunkPosition ZERO = new ChunkPosition(0, 0);

    public static int blockToSectionCoordinate(int i) {
        return i >> 4;
    }

    public static int sectionToBlockCoordinate(int i) {
        return i << 4;
    }

    public static int sectionToBlockCoordinate(int i, int j) {
        return sectionToBlockCoordinate(i) + j;
    }


    public ChunkPosition(BlockPosition blockPosition) {
        this(blockToSectionCoordinate(blockPosition.x()), blockToSectionCoordinate(blockPosition.z()));
    }

    public ChunkPosition(long l) {
        this((int) l, (int) (l >> 32));
    }

    public static ChunkPosition at(double x, double z) {
        return at((int) MathUtils.floor(x), (int) MathUtils.floor(z));
    }

    public static ChunkPosition at(int x, int z) {
        return new ChunkPosition(x, z);
    }

    public static ChunkPosition at(Vector2d vector) {
        return at(vector.x(), vector.y());
    }

    public static ChunkPosition at(Vector2i vector) {
        return at(vector.x(), vector.y());
    }

    public ChunkPosition offset(int i, int k) {
        return i == 0 && k == 0 ? this : new ChunkPosition(x() + i, z() + k);
    }

    public Vector2d getCenter() {
        return toVector2d().add(0.5, 0.5);
    }

    public ChunkPosition offset(Vector2i vector) {
        return offset(vector.x(), vector.y());
    }

    public ChunkPosition sub(Vector2i vector) {
        return offset(-vector.x(), -vector.y());
    }

    public ChunkPosition mul(int i) {
        if (i == 1) {
            return this;
        } else {
            return i == 0 ? ZERO : new ChunkPosition(x() * i, z() * i);
        }
    }

    public ChunkPosition north() {
        return this.relative(Direction.NORTH);
    }

    public ChunkPosition north(int i) {
        return this.relative(Direction.NORTH, i);
    }

    public ChunkPosition south() {
        return this.relative(Direction.SOUTH);
    }

    public ChunkPosition south(int i) {
        return this.relative(Direction.SOUTH, i);
    }

    public ChunkPosition west() {
        return this.relative(Direction.WEST);
    }

    public ChunkPosition west(int i) {
        return this.relative(Direction.WEST, i);
    }

    public ChunkPosition east() {
        return this.relative(Direction.EAST);
    }

    public ChunkPosition east(int i) {
        return this.relative(Direction.EAST, i);
    }

    public ChunkPosition relative(Direction direction) {
        return new ChunkPosition(x() + direction.getStepX(), z() + direction.getStepZ());
    }

    public ChunkPosition relative(Direction direction, int i) {
        return i == 0 ? this : new ChunkPosition(x() + direction.getStepX() * i, z() + direction.getStepZ() * i);
    }

    public ChunkPosition relative(Axis axis, int i) {
        if (i == 0) {
            return this;
        } else {
            int j = axis == Axis.X ? i : 0;
            int l = axis == Axis.Z ? i : 0;
            return new ChunkPosition(x() + j, z() + l);
        }
    }

    public ChunkPosition rotate(Revolve revolve) {
        return switch (revolve) {
            case CLOCKWISE_90 -> new ChunkPosition(-z(), x());
            case CLOCKWISE_180 -> new ChunkPosition(-x(), -z());
            case COUNTERCLOCKWISE_90 -> new ChunkPosition(z(), -x());
            default -> this;
        };
    }

    public ChunkPosition add(ChunkPosition other) {
        return add(other.x(), other.z());
    }

    public ChunkPosition add(int x, int z) {
        return ChunkPosition.at(this.x + x, this.z + z);
    }

    public ChunkPosition withX(int x) {
        return new ChunkPosition(x, z);
    }

    public ChunkPosition withZ(int z) {
        return new ChunkPosition(x, z);
    }

    public Vector2d toVector2d() {
        return Vector2d.at(x, z);
    }

    public int getMiddleBlockX() {
        return this.getBlockX(8);
    }

    public int getMiddleBlockZ() {
        return this.getBlockZ(8);
    }

    public int getMinBlockX() {
        return sectionToBlockCoordinate(this.x);
    }

    public int getMinBlockZ() {
        return sectionToBlockCoordinate(this.z);
    }

    public int getMaxBlockX() {
        return this.getBlockX(15);
    }

    public int getMaxBlockZ() {
        return this.getBlockZ(15);
    }

    public int getRegionX() {
        return this.x >> 5;
    }

    public int getRegionZ() {
        return this.z >> 5;
    }

    public int getRegionLocalX() {
        return this.x & 0x1F;
    }

    public int getRegionLocalZ() {
        return this.z & 0x1F;
    }

    public BlockPosition getBlockAt(int i, int j, int k) {
        return new BlockPosition(this.getBlockX(i), j, this.getBlockZ(k));
    }

    public int getBlockX(int i) {
        return sectionToBlockCoordinate(this.x, i);
    }

    public int getBlockZ(int i) {
        return sectionToBlockCoordinate(this.z, i);
    }

    public BlockPosition getMiddleBlockPosition(int i) {
        return new BlockPosition(this.getMiddleBlockX(), i, this.getMiddleBlockZ());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }
}
