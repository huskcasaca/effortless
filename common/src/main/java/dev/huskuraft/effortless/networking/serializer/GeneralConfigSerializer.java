package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.session.config.GeneralConfig;

public class GeneralConfigSerializer implements BufferSerializer<GeneralConfig> {

    @Override
    public GeneralConfig read(Buffer buffer) {
        return new GeneralConfig(
                buffer.readNullable(Buffer::readBoolean),
                buffer.readNullable(Buffer::readBoolean),
                buffer.readNullable(Buffer::readBoolean),
                buffer.readNullable(Buffer::readBoolean),
                buffer.readNullable(Buffer::readVarInt),
                buffer.readNullable(Buffer::readVarInt),
                buffer.readNullable(Buffer::readVarInt),
                buffer.readNullable(Buffer::readVarInt),
                buffer.readNullable(buffer1 -> buffer1.readList(Buffer::readResourceLocation)),
                buffer.readNullable(buffer1 -> buffer1.readList(Buffer::readResourceLocation))
        );
    }

    @Override
    public void write(Buffer buffer, GeneralConfig generalConfig) {
        buffer.writeNullable(generalConfig.useCommands(), Buffer::writeBoolean);
        buffer.writeNullable(generalConfig.allowUseMod(), Buffer::writeBoolean);
        buffer.writeNullable(generalConfig.allowBreakBlocks(), Buffer::writeBoolean);
        buffer.writeNullable(generalConfig.allowPlaceBlocks(), Buffer::writeBoolean);
        buffer.writeNullable(generalConfig.maxReachDistance(), Buffer::writeVarInt);
        buffer.writeNullable(generalConfig.maxDistancePerAxis(), Buffer::writeVarInt);
        buffer.writeNullable(generalConfig.maxBreakBoxVolume(), Buffer::writeVarInt);
        buffer.writeNullable(generalConfig.maxPlaceBoxVolume(), Buffer::writeVarInt);
        buffer.writeNullable(generalConfig.whitelistedItems(), (buffer1, list) -> buffer1.writeList(list, Buffer::writeResourceLocation));
        buffer.writeNullable(generalConfig.blacklistedItems(), (buffer1, list) -> buffer1.writeList(list, Buffer::writeResourceLocation));

    }
}
