package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Orientation;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;

public class BlockInteractionSerializer implements BufferSerializer<BlockInteraction> {

    @Override
    public BlockInteraction read(Buffer buffer) {
        return new BlockInteraction(
                buffer.read(new Vector3dSerializer()),
                buffer.readEnum(Orientation.class),
                buffer.read(new BlockPositionSerializer()),
                buffer.readBoolean()
        );
    }

    @Override
    public void write(Buffer buffer, BlockInteraction blockInteraction) {
        buffer.write(blockInteraction.getPosition(), new Vector3dSerializer());
        buffer.writeEnum(blockInteraction.getDirection());
        buffer.write(blockInteraction.getBlockPosition(), new BlockPositionSerializer());
        buffer.writeBoolean(blockInteraction.isInside());
    }

}
