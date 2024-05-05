package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.math.Vector3d;

public record BlockInteraction(
        Vector3d position,
        Orientation orientation,
        BlockPosition blockPosition,
        boolean inside,
        boolean miss
) implements Interaction {

    // FIXME: 15/10/23 check coustrcutor params order
    public BlockInteraction(Vector3d position, Orientation orientation, BlockPosition blockPosition, boolean inside) {
        this(position, orientation, blockPosition, inside, false);
    }

    public BlockInteraction withDirection(Orientation orientation) {
        return new BlockInteraction(this.position, orientation, this.blockPosition, this.inside, this.miss);
    }

    public BlockInteraction withBlockPosition(BlockPosition blockPosition) {
        return new BlockInteraction(this.position, this.orientation, blockPosition, this.inside, this.miss);
    }

    public BlockInteraction withPosition(Vector3d position) {
        return new BlockInteraction(position, this.orientation, this.blockPosition, this.inside, this.miss);
    }

    public BlockInteraction withMiss(boolean miss) {
        return new BlockInteraction(this.position, this.orientation, this.blockPosition, this.inside, miss);
    }

    public BlockPosition getBlockPosition() {
        return this.blockPosition;
    }

    public Orientation getDirection() {
        return this.orientation;
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
}
