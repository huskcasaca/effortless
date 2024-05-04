package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class GeneralConfigSerializer implements NetByteBufSerializer<GeneralConfig> {

    @Override
    public GeneralConfig read(NetByteBuf byteBuf) {
        return new GeneralConfig(
                byteBuf.readNullable(NetByteBuf::readBoolean),
                byteBuf.readNullable(NetByteBuf::readBoolean),
                byteBuf.readNullable(NetByteBuf::readBoolean),
                byteBuf.readNullable(NetByteBuf::readBoolean),
                byteBuf.readNullable(NetByteBuf::readVarInt),
                byteBuf.readNullable(NetByteBuf::readVarInt),
                byteBuf.readNullable(NetByteBuf::readVarInt),
                byteBuf.readNullable(NetByteBuf::readVarInt),
                byteBuf.readNullable(NetByteBuf::readVarInt),
                byteBuf.readNullable(buffer1 -> buffer1.readList(NetByteBuf::readResourceLocation)),
                byteBuf.readNullable(buffer1 -> buffer1.readList(NetByteBuf::readResourceLocation)));
    }

    @Override
    public void write(NetByteBuf byteBuf, GeneralConfig generalConfig) {
        byteBuf.writeNullable(generalConfig.useCommands(), NetByteBuf::writeBoolean);
        byteBuf.writeNullable(generalConfig.allowUseMod(), NetByteBuf::writeBoolean);
        byteBuf.writeNullable(generalConfig.allowBreakBlocks(), NetByteBuf::writeBoolean);
        byteBuf.writeNullable(generalConfig.allowPlaceBlocks(), NetByteBuf::writeBoolean);
        byteBuf.writeNullable(generalConfig.maxReachDistance(), NetByteBuf::writeVarInt);
        byteBuf.writeNullable(generalConfig.maxBoxVolumePerBreak(), NetByteBuf::writeVarInt);
        byteBuf.writeNullable(generalConfig.maxBoxVolumePerPlace(), NetByteBuf::writeVarInt);
        byteBuf.writeNullable(generalConfig.maxBoxSideLengthPerBreak(), NetByteBuf::writeVarInt);
        byteBuf.writeNullable(generalConfig.maxBoxSideLengthPerPlace(), NetByteBuf::writeVarInt);
        byteBuf.writeNullable(generalConfig.whitelistedItems(), (buffer1, list) -> buffer1.writeList(list, NetByteBuf::writeResourceLocation));
        byteBuf.writeNullable(generalConfig.blacklistedItems(), (buffer1, list) -> buffer1.writeList(list, NetByteBuf::writeResourceLocation));

    }
}
