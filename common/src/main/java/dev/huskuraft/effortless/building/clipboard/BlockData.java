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

public record BlockData(
        BlockPosition blockPosition,
        BlockState blockState,
        RecordTag entityTag
) implements Rotatable<BlockData>, Movable<BlockData>, Mirrorable<BlockData> {

    @Override
    public BlockData rotate(RotateContext rotateContext) {
        return new BlockData(
                rotateContext.rotate(blockPosition),
                rotateContext.rotate(blockState),
                entityTag
        );
    }

    @Override
    public BlockData move(MoveContext moveContext) {
        return new BlockData(
                moveContext.move(blockPosition),
                blockState,
                entityTag
        );
    }

    @Override
    public BlockData mirror(MirrorContext mirrorContext) {
        return new BlockData(
                mirrorContext.mirror(blockPosition),
                mirrorContext.mirror(blockState),
                entityTag
        );
    }
}
