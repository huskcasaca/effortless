package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.Revolve;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;

public class RevolveContext {

    private final Vector3d position;
    private final PositionType positionType;
    private final double angle;

    private RevolveContext(Vector3d position, PositionType positionType, double angle) {
        this.position = position;
        this.positionType = positionType;
        this.angle = angle;
    }

    public static RevolveContext relative(Vector3d position, double angle) {
        return new RevolveContext(position, PositionType.RELATIVE, angle);
    }

    public static RevolveContext absolute(Vector3d position, double angle) {
        return new RevolveContext(position, PositionType.ABSOLUTE, angle);
    }

    private static BlockState rotateOriginalBlockState(Player player, BlockPosition startPos, double startAngleToCenter, BlockState blockState) {
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

    private static BlockState rotateBlockState(Player player, BlockPosition startPos, Vector3d relVec, BlockState blockState, boolean alternate) {
        BlockState newBlockState;
        double angleToCenter = MathUtils.atan2(relVec.x(), relVec.z()); //between -PI and PI

        if (angleToCenter < -0.751 * MathUtils.PI || angleToCenter > 0.749 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_180);
            if (alternate) {
                newBlockState = newBlockState.mirror(Axis.X);
            }
        } else if (angleToCenter < -0.251 * MathUtils.PI) {
            newBlockState = blockState.rotate(Revolve.CLOCKWISE_90);
            if (alternate) {
                newBlockState = newBlockState.mirror(Axis.Z);
            }
        } else if (angleToCenter > 0.249 * MathUtils.PI) {
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

    public BlockInteraction revolve(BlockInteraction blockInteraction) {
        return blockInteraction;
    }

    public enum PositionType {
        RELATIVE,
        ABSOLUTE
    }

}
