package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;

public class BlockPositionSerializer implements BufferSerializer<BlockPosition> {

    @Override
    public BlockPosition read(Buffer buffer) {
        return BlockPosition.at(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt()
        );
    }


    @Override
    public void write(Buffer buffer, BlockPosition blockPosition) {
        buffer.writeInt(blockPosition.x());
        buffer.writeInt(blockPosition.y());
        buffer.writeInt(blockPosition.z());
    }

}
