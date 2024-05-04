package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;

public class BlockPositionSerializer implements NetByteBufSerializer<BlockPosition> {

    @Override
    public BlockPosition read(NetByteBuf byteBuf) {
        return BlockPosition.at(
                byteBuf.readInt(),
                byteBuf.readInt(),
                byteBuf.readInt()
        );
    }


    @Override
    public void write(NetByteBuf byteBuf, BlockPosition blockPosition) {
        byteBuf.writeInt(blockPosition.x());
        byteBuf.writeInt(blockPosition.y());
        byteBuf.writeInt(blockPosition.z());
    }

}
