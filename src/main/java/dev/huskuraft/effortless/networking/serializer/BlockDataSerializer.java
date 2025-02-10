package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.clipboard.BlockData;

public class BlockDataSerializer implements NetByteBufSerializer<BlockData> {
    @Override
    public BlockData read(NetByteBuf byteBuf) {
        return new BlockData(
                byteBuf.read(new BlockPositionSerializer()),
                byteBuf.readNullable(NetByteBuf::readBlockState),
                byteBuf.readNullable(NetByteBuf::readRecordTag)
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, BlockData blockData) {
        byteBuf.write(blockData.blockPosition(), new BlockPositionSerializer());
        byteBuf.writeNullable(blockData.blockState(), NetByteBuf::writeBlockState);
        byteBuf.writeNullable(blockData.entityTag(), NetByteBuf::writeRecordTag);
    }
}
