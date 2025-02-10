package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.BlockEntity;
import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.Direction;
import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.effortless.building.operation.block.Extras;

public record MirrorContext(
       Vector3d center,
       BoundingBox3d bounds,
       Axis axis
) implements PositionBounded {

    public static MirrorContext MIRROR_X = new MirrorContext(new Vector3d(0.5, 0.5, 0.5), new BoundingBox3d(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), Axis.X);
    public static MirrorContext MIRROR_Y = new MirrorContext(new Vector3d(0.5, 0.5, 0.5), new BoundingBox3d(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), Axis.Y);
    public static MirrorContext MIRROR_Z = new MirrorContext(new Vector3d(0.5, 0.5, 0.5), new BoundingBox3d(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), Axis.Z);

    public Vector3d mirror(Vector3d vec) {
        return switch (axis) {
            case Y -> new Vector3d(vec.x(), center.y() + (center.y() - vec.y()), vec.z());
            case X -> new Vector3d(center.x() + (center.x() - vec.x()), vec.y(), vec.z());
            case Z -> new Vector3d(vec.x(), vec.y(), center.z() + (center.z() - vec.z()));
        };
    }

    public BlockPosition mirror(BlockPosition blockPosition) {
        var vector = mirror(blockPosition.getCenter());
        return BlockPosition.at(vector);
    }

    public Direction mirror(Direction direction) {
        return direction.getAxis() != axis ? direction : direction.getOpposite();
    }

    public BlockInteraction mirror(BlockInteraction blockInteraction) {
        var direction = mirror(blockInteraction.getDirection());
        var position = mirror(blockInteraction.getPosition());
        var blockPosition = mirror(blockInteraction.getBlockPosition());
        return blockInteraction.withDirection(direction).withPosition(position).withBlockPosition(blockPosition);
    }

    public BlockState mirror(BlockState blockState) {
        if (blockState == null) {
            return blockState;
        }
        return blockState.mirror(axis);
    }

    public BlockEntity mirror(BlockEntity blockEntity) {
        return blockEntity;
    }

    public Extras mirror(Extras extras) {
        // TODO: 23/5/24
        return extras;
    }

    @Override
    public boolean isInBounds(Vector3d position) {
        return bounds.contains(position);
    }
}
