package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.math.Vector3d;

public record BlockInteraction(
        Vector3d position,
        Direction direction,
        BlockPosition blockPosition,
        boolean inside,
        boolean miss
) implements Interaction {

    // FIXME: 15/10/23 check coustrcutor params order
    public BlockInteraction(Vector3d position, Direction direction, BlockPosition blockPosition, boolean inside) {
        this(position, direction, blockPosition, inside, false);
    }

    public BlockInteraction withDirection(Direction direction) {
        return new BlockInteraction(this.position, direction, this.blockPosition, this.inside, this.miss);
    }

    public BlockInteraction withBlockPosition(BlockPosition blockPosition) {
        return new BlockInteraction(this.position, this.direction, blockPosition, this.inside, this.miss);
    }

    public BlockInteraction withPosition(Vector3d position) {
        return new BlockInteraction(position, this.direction, this.blockPosition, this.inside, this.miss);
    }

    public BlockInteraction withMiss(boolean miss) {
        return new BlockInteraction(this.position, this.direction, this.blockPosition, this.inside, miss);
    }

    public BlockPosition getBlockPosition() {
        return this.blockPosition;
    }

    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public Target getTarget() {
        return this.miss ? Target.MISS : Target.BLOCK;
    }

    @Override
    public Vector3d getPosition() {
        return position;
    }

    public boolean isInside() {
        return this.inside;
    }

    public boolean isMiss() {
        return this.miss;
    }
}
