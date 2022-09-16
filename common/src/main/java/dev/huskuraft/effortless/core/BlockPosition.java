package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.math.Vector3i;

public final class BlockPosition extends Vector3i {

    public static final BlockPosition ZERO = new BlockPosition(0, 0, 0);
    public static final BlockPosition ONE = new BlockPosition(1, 1, 1);

    public BlockPosition(int x, int y, int z) {
        super(x, y, z);
    }

    public static BlockPosition at(double x, double y, double z) {
        return at((int) MathUtils.floor(x), (int) MathUtils.floor(y), (int) MathUtils.floor(z));
    }

    public static BlockPosition at(int x, int y, int z) {
        return new BlockPosition(x, y, z);
    }

    public static BlockPosition at(Vector3d vector) {
        return at(vector.getX(), vector.getY(), vector.getZ());
    }

    public static BlockPosition at(Vector3i vector) {
        return at(vector.getX(), vector.getY(), vector.getZ());
    }

    public BlockPosition offset(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new BlockPosition(getX() + i, getY() + j, getZ() + k);
    }

    public Vector3d getCenter() {
        return toVector3().add(0.5, 0.5, 0.5);
    }

    public BlockPosition offset(Vector3i vector) {
        return offset(vector.getX(), vector.getY(), vector.getZ());
    }

    public BlockPosition subtract(Vector3i vector) {
        return offset(-vector.getX(), -vector.getY(), -vector.getZ());
    }

    public BlockPosition multiply(int i) {
        if (i == 1) {
            return this;
        } else {
            return i == 0 ? ZERO : new BlockPosition(getX() * i, getY() * i, getZ() * i);
        }
    }

    public BlockPosition above() {
        return this.relative(Orientation.UP);
    }

    public BlockPosition above(int i) {
        return this.relative(Orientation.UP, i);
    }

    public BlockPosition below() {
        return this.relative(Orientation.DOWN);
    }

    public BlockPosition below(int i) {
        return this.relative(Orientation.DOWN, i);
    }

    public BlockPosition north() {
        return this.relative(Orientation.NORTH);
    }

    public BlockPosition north(int i) {
        return this.relative(Orientation.NORTH, i);
    }

    public BlockPosition south() {
        return this.relative(Orientation.SOUTH);
    }

    public BlockPosition south(int i) {
        return this.relative(Orientation.SOUTH, i);
    }

    public BlockPosition west() {
        return this.relative(Orientation.WEST);
    }

    public BlockPosition west(int i) {
        return this.relative(Orientation.WEST, i);
    }

    public BlockPosition east() {
        return this.relative(Orientation.EAST);
    }

    public BlockPosition east(int i) {
        return this.relative(Orientation.EAST, i);
    }

    public BlockPosition relative(Orientation orientation) {
        return new BlockPosition(getX() + orientation.getStepX(), getY() + orientation.getStepY(), getZ() + orientation.getStepZ());
    }

    public BlockPosition relative(Orientation orientation, int i) {
        return i == 0 ? this : new BlockPosition(getX() + orientation.getStepX() * i, getY() + orientation.getStepY() * i, getZ() + orientation.getStepZ() * i);
    }

    public BlockPosition relative(Axis axis, int i) {
        if (i == 0) {
            return this;
        } else {
            int j = axis == Axis.X ? i : 0;
            int k = axis == Axis.Y ? i : 0;
            int l = axis == Axis.Z ? i : 0;
            return new BlockPosition(getX() + j, getY() + k, getZ() + l);
        }
    }

    public BlockPosition rotate(Revolve revolve) {
        return switch (revolve) {
            case CLOCKWISE_90 -> new BlockPosition(-getZ(), getY(), getX());
            case CLOCKWISE_180 -> new BlockPosition(-getX(), getY(), -getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPosition(getZ(), getY(), -getX());
            default -> this;
        };
    }

    public BlockPosition cross(Vector3i vector) {
        return new BlockPosition(getY() * vector.getZ() - getZ() * vector.getY(), getZ() * vector.getX() - getX() * vector.getZ(), getX() * vector.getY() - getY() * vector.getX());
    }

    public int get(Axis axis) {
        return switch (axis) {
            case X -> this.x;
            case Y -> this.y;
            case Z -> this.z;
        };
    }

}
