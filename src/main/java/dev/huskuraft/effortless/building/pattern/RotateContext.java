package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.BlockEntity;
import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.Direction;
import dev.huskuraft.universal.api.core.Revolve;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.effortless.building.operation.block.Extras;

public record RotateContext(
        Axis axis,
        Vector3d center,
        double angle,
        int radius,
        int length
) implements PositionBounded {

    public static RotateContext CLOCKWISE_X_90 = new RotateContext(Axis.X, Vector3d.ZERO.add(0.5, 0.5, 0.5), MathUtils.PI / 2, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static RotateContext CLOCKWISE_Y_90 = new RotateContext(Axis.Y, Vector3d.ZERO.add(0.5, 0.5, 0.5), MathUtils.PI / 2, Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static RotateContext CLOCKWISE_Z_90 = new RotateContext(Axis.Z, Vector3d.ZERO.add(0.5, 0.5, 0.5), MathUtils.PI / 2, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private static BlockState rotateOriginalBlockState(double startAngleToCenter, BlockState blockState) {
        BlockState newBlockState = blockState;

        if (startAngleToCenter < -0.751 * MathUtils.PI || startAngleToCenter > 0.749 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_180);
        } else if (startAngleToCenter < -0.251 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.COUNTERCLOCKWISE_90);
        } else if (startAngleToCenter > 0.249 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_90);
        }

        return newBlockState;
    }

    private static BlockState rotateBlockState(BlockState blockState, double angle, boolean alternate) {
        BlockState newBlockState;
        angle = angle - MathUtils.PI;

        if (angle < -0.751 * MathUtils.PI || angle > 0.749 * MathUtils.PI) {
            newBlockState = blockState;
            if (alternate) {
                newBlockState = newBlockState.mirror(Axis.X);
            }
        } else if (angle < -0.251 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.COUNTERCLOCKWISE_90);
            if (alternate) {
                newBlockState = newBlockState.mirror(Axis.Z);
            }
        } else if (angle > 0.249 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_90);
            if (alternate) {
                newBlockState = newBlockState.mirror(Axis.Z);
            }
        } else {
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_180);
            if (alternate) {
                newBlockState = newBlockState.mirror(Axis.X);
            }
        }

        return newBlockState;
    }

    private static Direction rotateDirection(Direction direction, double angle) {
        Direction newDirection;
        angle = angle - MathUtils.PI;

        if (angle < -0.751 * MathUtils.PI || angle > 0.749 * MathUtils.PI) {
            newDirection = direction;
        } else if (angle < -0.251 * MathUtils.PI) {
            newDirection = direction.getClockWise(Axis.Y);
        } else if (angle > 0.249 * MathUtils.PI) {
            newDirection = direction.getClockWise(Axis.Y).getClockWise(Axis.Y).getClockWise(Axis.Y);
        } else {
            newDirection = direction.getClockWise(Axis.Y).getClockWise(Axis.Y);
        }

        return newDirection;
    }

    private static BlockInteraction rotateX(BlockInteraction blockInteraction, Vector3d center, double angle) {
        var rotatedPosition = blockInteraction.getPosition().sub(center).rotX(angle).add(center);
        var rotatedBlockPosition = BlockPosition.at(center.add(blockInteraction.getBlockPosition().getCenter().sub(center).rotX(angle)));
        return blockInteraction.withDirection(rotateDirection(blockInteraction.getDirection(), angle)).withPosition(rotatedPosition).withBlockPosition(rotatedBlockPosition);
    }

    private static BlockInteraction rotateY(BlockInteraction blockInteraction, Vector3d center, double angle) {
        var rotatedPosition = blockInteraction.getPosition().sub(center).rotY(angle).add(center);
        var rotatedBlockPosition = BlockPosition.at(center.add(blockInteraction.getBlockPosition().getCenter().sub(center).rotY(angle)));
        return blockInteraction.withDirection(rotateDirection(blockInteraction.getDirection(), angle)).withPosition(rotatedPosition).withBlockPosition(rotatedBlockPosition);
    }

    private static BlockInteraction rotateZ(BlockInteraction blockInteraction, Vector3d center, double angle) {
        var rotatedPosition = blockInteraction.getPosition().sub(center).rotZ(angle).add(center);
        var rotatedBlockPosition = BlockPosition.at(center.add(blockInteraction.getBlockPosition().getCenter().sub(center).rotZ(angle)));
        return blockInteraction.withDirection(rotateDirection(blockInteraction.getDirection(), angle)).withPosition(rotatedPosition).withBlockPosition(rotatedBlockPosition);
    }

    public BlockInteraction rotate(BlockInteraction blockInteraction) {
        return switch (axis) {
            case X -> rotateX(blockInteraction, center, angle);
            case Y -> rotateY(blockInteraction, center, angle);
            case Z -> rotateZ(blockInteraction, center, angle);
        };
    }

    public BlockPosition rotate(BlockPosition blockPosition) {
        return switch (axis) {
            case X -> BlockPosition.at(center.add(blockPosition.getCenter().sub(center).rotX(angle)));
            case Y -> BlockPosition.at(center.add(blockPosition.getCenter().sub(center).rotY(angle)));
            case Z -> BlockPosition.at(center.add(blockPosition.getCenter().sub(center).rotZ(angle)));
        };
    }

    public BlockEntity rotate(BlockEntity blockEntity) {
        return blockEntity;
    }

    public BlockState rotate(BlockState blockState) {
        if (blockState == null) {
            return null;
        }
        return rotateBlockState(blockState, angle, false);
    }

    public Extras rotate(Extras extras) {
        // TODO: 23/5/24
        return extras;
    }

    @Override
    public boolean isInBounds(Vector3d position) {
        return switch (axis) {
            case X -> position.withX(0).distance(center.withX(0)) <= radius && position.withY(0).withZ(0).distance(center.withY(0).withZ(0)) <= length;
            case Y -> position.withY(0).distance(center.withY(0)) <= radius && position.withX(0).withZ(0).distance(center.withX(0).withZ(0)) <= length;
            case Z -> position.withZ(0).distance(center.withZ(0)) <= radius && position.withX(0).withY(0).distance(center.withX(0).withY(0)) <= length;
        };
    }
}
