package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Orientation;
import dev.huskuraft.effortless.api.core.Revolve;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;

public class RotateContext {

    private final Vector3d center;
    private final double angle;

    private RotateContext(Vector3d center, double angle) {
        this.center = center;
        this.angle = angle;
    }

    public static RotateContext absolute(Vector3d position, double angle) {
        return new RotateContext(position, angle);
    }

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
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_180);
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
            newBlockState = blockState;
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
            newOrientation = orientation.getClockWise(Axis.Y).getClockWise(Axis.Y);
        } else if (angle < -0.251 * MathUtils.PI) {
            newOrientation = orientation.getClockWise(Axis.Y);
        } else if (angle > 0.249 * MathUtils.PI) {
            newOrientation = orientation.getClockWise(Axis.Y).getClockWise(Axis.Y).getClockWise(Axis.Y);
        } else {
            newOrientation = orientation;
        }

        return newOrientation;
    }


    private static BlockInteraction rotateBlockInteraction(BlockInteraction blockInteraction, Vector3d center, double angle) {
        var rotatedPosition = blockInteraction.getPosition().sub(center).rotY(angle).add(center);
        var rotatedBlockPosition = BlockPosition.at(center.add(blockInteraction.getBlockPosition().getCenter().sub(center).rotY(angle)));
        return blockInteraction.withDirection(rotateOrientation(blockInteraction.getDirection(), angle)).withPosition(rotatedPosition).withBlockPosition(rotatedBlockPosition);
    }


    public BlockInteraction rotate(BlockInteraction blockInteraction) {
        return rotateBlockInteraction(blockInteraction, center, angle);
    }

    public BlockState rotate(BlockState blockState) {
        return rotateBlockState(blockState, angle, false);
    }

}
