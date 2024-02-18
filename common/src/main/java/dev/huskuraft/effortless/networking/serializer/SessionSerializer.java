package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.api.platform.LoaderType;
import dev.huskuraft.effortless.api.platform.Session;

public class SessionSerializer implements BufferSerializer<Session> {

    @Override
    public Session read(Buffer buffer) {
        return new Session(
                buffer.readEnum(LoaderType.class),
                buffer.readString(),
                buffer.readString(),
                buffer.readList(new ModSerializer())
        );
    }

    @Override
    public void write(Buffer buffer, Session session) {
        buffer.writeEnum(session.loaderType());
        buffer.writeString(session.loaderVersion());
        buffer.writeString(session.gameVersion());
        buffer.writeList(session.mods(), new ModSerializer());
    }
}
