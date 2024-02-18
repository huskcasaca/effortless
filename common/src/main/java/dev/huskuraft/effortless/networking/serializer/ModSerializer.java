package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.platform.Mod;

public class ModSerializer implements BufferSerializer<Mod> {

    @Override
    public Mod read(Buffer buffer) {
        return Mod.create(buffer.readString(),
                buffer.readString(),
                buffer.readString(),
                buffer.readString());
    }

    @Override
    public void write(Buffer buffer, Mod mod) {
        buffer.writeString(mod.getId());
        buffer.writeString(mod.getVersionStr());
        buffer.writeString(mod.getDescription());
        buffer.writeString(mod.getName());
    }
}
