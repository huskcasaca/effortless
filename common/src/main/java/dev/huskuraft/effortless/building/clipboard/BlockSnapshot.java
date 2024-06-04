package dev.huskuraft.effortless.building.clipboard;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.tag.RecordTag;
import dev.huskuraft.effortless.building.operation.Mirrorable;
import dev.huskuraft.effortless.building.operation.Movable;
import dev.huskuraft.effortless.building.operation.Rotatable;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public record BlockSnapshot(
        BlockPosition relativePosition,
        BlockState blockState,
        RecordTag entityTag
) implements Rotatable<BlockSnapshot>, Movable<BlockSnapshot>, Mirrorable<BlockSnapshot> {

    @Override
    public BlockSnapshot rotate(RotateContext rotateContext) {
        return new BlockSnapshot(
                rotateContext.rotate(relativePosition),
                rotateContext.rotate(blockState),
                entityTag
        );
    }

    @Override
    public BlockSnapshot move(MoveContext moveContext) {
        return new BlockSnapshot(
                moveContext.move(relativePosition),
                blockState,
                entityTag
        );
    }

    @Override
    public BlockSnapshot mirror(MirrorContext mirrorContext) {
        return new BlockSnapshot(
                mirrorContext.mirror(relativePosition),
                mirrorContext.mirror(blockState),
                entityTag
        );
    }
}
