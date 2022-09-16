package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;

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

    private static BlockData rotateOriginalBlockState(Player player, BlockPosition startPos, double startAngleToCenter, BlockData blockData) {
        BlockData newBlockData = blockData;

        if (startAngleToCenter < -0.751 * MathUtils.PI || startAngleToCenter > 0.749 * MathUtils.PI) {
            newBlockData = blockData.rotate(Revolve.CLOCKWISE_180);
        } else if (startAngleToCenter < -0.251 * MathUtils.PI) {
            newBlockData = blockData.rotate(Revolve.COUNTERCLOCKWISE_90);
        } else if (startAngleToCenter > 0.249 * MathUtils.PI) {
            newBlockData = blockData.rotate(Revolve.CLOCKWISE_90);
        }

        return newBlockData;
    }

    private static BlockData rotateBlockState(Player player, BlockPosition startPos, Vector3d relVec, BlockData blockData, boolean alternate) {
        BlockData newBlockData;
        double angleToCenter = MathUtils.atan2(relVec.getX(), relVec.getZ()); //between -PI and PI

        if (angleToCenter < -0.751 * MathUtils.PI || angleToCenter > 0.749 * MathUtils.PI) {
            newBlockData = blockData.rotate(Revolve.CLOCKWISE_180);
            if (alternate) {
                newBlockData = newBlockData.mirror(Axis.X);
            }
        } else if (angleToCenter < -0.251 * MathUtils.PI) {
            newBlockData = blockData.rotate(Revolve.CLOCKWISE_90);
            if (alternate) {
                newBlockData = newBlockData.mirror(Axis.Z);
            }
        } else if (angleToCenter > 0.249 * MathUtils.PI) {
            newBlockData = blockData.rotate(Revolve.COUNTERCLOCKWISE_90);
            if (alternate) {
                newBlockData = newBlockData.mirror(Axis.Z);
            }
        } else {
            newBlockData = blockData;
            if (alternate) {
                newBlockData = newBlockData.mirror(Axis.X);
            }
        }

        return newBlockData;
    }

    public BlockInteraction revolve(BlockInteraction blockInteraction) {
        return blockInteraction;
    }

    public enum PositionType {
        RELATIVE,
        ABSOLUTE
    }

}
