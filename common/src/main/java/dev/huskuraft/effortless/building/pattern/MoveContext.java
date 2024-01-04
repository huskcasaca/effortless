package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.math.Vector3d;

public class MoveContext {

    private final Vector3d amount;
    private final PositionType positionType;

    private MoveContext(Vector3d amount, PositionType positionType) {
        this.amount = amount;
        this.positionType = positionType;
    }

    public static MoveContext relative(Vector3d amount) {
        return new MoveContext(amount, PositionType.RELATIVE);
    }

    public static MoveContext relative(double x, double y, double z) {
        return new MoveContext(new Vector3d(x, y, z), PositionType.RELATIVE);
    }

    public static MoveContext absolute(Vector3d amount) {
        return new MoveContext(amount, PositionType.ABSOLUTE);
    }

    public BlockInteraction move(BlockInteraction blockInteraction) {
        var direction = blockInteraction.getDirection();
        var blockPosition = move(blockInteraction.getBlockPosition());
        var location = blockInteraction.getPosition().add(blockPosition.sub(blockInteraction.getBlockPosition()).toVector3d());

        return new BlockInteraction(location, direction, blockPosition, blockInteraction.isInside());
    }

    private Vector3d move(Vector3d vector) {
        return switch (positionType) {
            case RELATIVE -> vector.add(amount);
            case ABSOLUTE -> amount;
        };
    }

    private BlockPosition move(BlockPosition blockPosition) {
        return switch (positionType) {
            case RELATIVE -> {
                Vector3d vector = move(blockPosition.getCenter());
                yield BlockPosition.at(vector);
            }
            case ABSOLUTE -> BlockPosition.at(amount);
        };
    }

    public enum PositionType {
        RELATIVE,
        ABSOLUTE
    }

}
