package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;

public class BlockPositionSerializer implements NetByteBufSerializer<BlockPosition> {

    @Override
    public BlockPosition read(NetByteBuf byteBuf) {
        return BlockPosition.at(
                byteBuf.readVarInt(),
                byteBuf.readVarInt(),
                byteBuf.readVarInt()
        );
    }


    @Override
    public void write(NetByteBuf byteBuf, BlockPosition blockPosition) {
        byteBuf.writeVarInt(blockPosition.x());
        byteBuf.writeVarInt(blockPosition.y());
        byteBuf.writeVarInt(blockPosition.z());
    }

}
