package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.platform.Mod;

public class ModSerializer implements BufferSerializer<Mod> {

    @Override
    public Mod read(Buffer buffer) {
        return Mod.create(
                buffer.readNullable(Buffer::readString),
                buffer.readNullable(Buffer::readString),
                buffer.readNullable(Buffer::readString),
                buffer.readNullable(Buffer::readString));
    }

    @Override
    public void write(Buffer buffer, Mod mod) {
        buffer.writeNullable(mod.getId(), Buffer::writeString);
        buffer.writeNullable(mod.getVersionStr(), Buffer::writeString);
        buffer.writeNullable(mod.getDescription(), Buffer::writeString);
        buffer.writeNullable(mod.getName(), Buffer::writeString);
    }
}
