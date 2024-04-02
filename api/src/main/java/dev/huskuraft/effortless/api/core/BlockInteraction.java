package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.math.Vector3d;

public class BlockInteraction extends Interaction {
    private final Orientation orientation;
    private final BlockPosition blockPosition;
    private final boolean inside;
    private final boolean miss;

    // FIXME: 15/10/23 check coustrcutor params order
    public BlockInteraction(Vector3d position, Orientation orientation, BlockPosition blockPosition, boolean inside) {
        this(position, orientation, blockPosition, inside, false);
    }

    public BlockInteraction(Vector3d position, Orientation orientation, BlockPosition blockPosition, boolean inside, boolean miss) {
        super(position);
        this.orientation = orientation;
        this.blockPosition = blockPosition;
        this.inside = inside;
        this.miss = miss;
    }

    public BlockInteraction withDirection(Orientation orientation) {
        return new BlockInteraction(this.position, orientation, this.blockPosition, this.inside, this.miss);
    }

    public BlockInteraction withPosition(BlockPosition blockPosition) {
        return new BlockInteraction(this.position, this.orientation, blockPosition, this.inside, this.miss);
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

    public boolean isInside() {
        return this.inside;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BlockInteraction that)) return false;

        if (inside != that.inside) return false;
        if (miss != that.miss) return false;
        if (orientation != that.orientation) return false;
        return blockPosition.equals(that.blockPosition);
    }

    @Override
    public int hashCode() {
        int result = orientation.hashCode();
        result = 31 * result + blockPosition.hashCode();
        result = 31 * result + (inside ? 1 : 0);
        result = 31 * result + (miss ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BlockInteraction[" + this.position + ", " + this.orientation + ", " + this.blockPosition + ", " + this.inside + ", " + this.miss + "]";
    }
}
