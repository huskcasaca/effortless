package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.universal.api.core.BlockEntity;
import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.math.Vector3i;

public class MoveContext {

    private final Vector3i amount;
    private final PositionType positionType;

    private MoveContext(Vector3i amount, PositionType positionType) {
        this.amount = amount;
        this.positionType = positionType;
    }

    public static MoveContext relative(Vector3i amount) {
        return new MoveContext(amount, PositionType.RELATIVE);
    }

    public static MoveContext relative(int x, int y, int z) {
        return new MoveContext(new Vector3i(x, y, z), PositionType.RELATIVE);
    }

    public static MoveContext absolute(Vector3i amount) {
        return new MoveContext(amount, PositionType.ABSOLUTE);
    }

    public BlockInteraction move(BlockInteraction blockInteraction) {
        var direction = blockInteraction.getDirection();
        var blockPosition = move(blockInteraction.getBlockPosition());
        var location = blockInteraction.getPosition().add(blockPosition.sub(blockInteraction.getBlockPosition()).toVector3d());

        return new BlockInteraction(location, direction, blockPosition, blockInteraction.isInside());
    }

    public Vector3i move(Vector3i vector) {
        return switch (positionType) {
            case RELATIVE -> vector.add(amount);
            case ABSOLUTE -> amount;
        };
    }

    public BlockPosition move(BlockPosition blockPosition) {
        return switch (positionType) {
            case RELATIVE -> blockPosition.add(amount);
            case ABSOLUTE -> BlockPosition.at(amount);
        };
    }

    public BlockEntity move(BlockEntity blockEntity) {
        return blockEntity;
    }

    public enum PositionType {
        RELATIVE,
        ABSOLUTE
    }

}
