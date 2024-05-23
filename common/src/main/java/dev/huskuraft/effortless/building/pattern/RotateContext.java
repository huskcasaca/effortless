package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Orientation;
import dev.huskuraft.effortless.api.core.Revolve;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;

public record RotateContext(
        Axis axis,
        Vector3d center,
        double angle,
        int radius,
        int length
) implements PositionBounded {

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
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_90);
            if (alternate) {
                newBlockState = newBlockState.mirror(Axis.Z);
            }
        } else if (angle > 0.249 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.COUNTERCLOCKWISE_90);
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

    private static Orientation rotateOrientation(Orientation orientation, double angle) {
        Orientation newOrientation;
        angle = angle - MathUtils.PI;

        if (angle < -0.751 * MathUtils.PI || angle > 0.749 * MathUtils.PI) {
            newOrientation = orientation;
        } else if (angle < -0.251 * MathUtils.PI) {
            newOrientation = orientation.getClockWise(Axis.Y);
        } else if (angle > 0.249 * MathUtils.PI) {
            newOrientation = orientation.getClockWise(Axis.Y).getClockWise(Axis.Y).getClockWise(Axis.Y);
        } else {
            newOrientation = orientation.getClockWise(Axis.Y).getClockWise(Axis.Y);
        }

        return newOrientation;
    }

    private static BlockInteraction rotateX(BlockInteraction blockInteraction, Vector3d center, double angle) {
        var rotatedPosition = blockInteraction.getPosition().sub(center).rotX(angle).add(center);
        var rotatedBlockPosition = BlockPosition.at(center.add(blockInteraction.getBlockPosition().getCenter().sub(center).rotX(angle)));
        return blockInteraction.withDirection(rotateOrientation(blockInteraction.getDirection(), angle)).withPosition(rotatedPosition).withBlockPosition(rotatedBlockPosition);
    }

    private static BlockInteraction rotateY(BlockInteraction blockInteraction, Vector3d center, double angle) {
        var rotatedPosition = blockInteraction.getPosition().sub(center).rotY(angle).add(center);
        var rotatedBlockPosition = BlockPosition.at(center.add(blockInteraction.getBlockPosition().getCenter().sub(center).rotY(angle)));
        return blockInteraction.withDirection(rotateOrientation(blockInteraction.getDirection(), angle)).withPosition(rotatedPosition).withBlockPosition(rotatedBlockPosition);
    }

    private static BlockInteraction rotateZ(BlockInteraction blockInteraction, Vector3d center, double angle) {
        var rotatedPosition = blockInteraction.getPosition().sub(center).rotZ(angle).add(center);
        var rotatedBlockPosition = BlockPosition.at(center.add(blockInteraction.getBlockPosition().getCenter().sub(center).rotZ(angle)));
        return blockInteraction.withDirection(rotateOrientation(blockInteraction.getDirection(), angle)).withPosition(rotatedPosition).withBlockPosition(rotatedBlockPosition);
    }

    public BlockInteraction rotate(BlockInteraction blockInteraction) {
        return switch (axis) {
            case X -> rotateX(blockInteraction, center, angle);
            case Y -> rotateY(blockInteraction, center, angle);
            case Z -> rotateZ(blockInteraction, center, angle);
        };
    }

    public BlockState rotate(BlockState blockState) {
        if (blockState == null) {
            return null;
        }
        return rotateBlockState(blockState, angle, false);
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
