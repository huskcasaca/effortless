package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.BufferSerializer;

public class BlockPositionSerializer extends BufferSerializer<BlockPosition> {

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
        buffer.writeInt(blockPosition.getX());
        buffer.writeInt(blockPosition.getY());
        buffer.writeInt(blockPosition.getZ());
    }

}
