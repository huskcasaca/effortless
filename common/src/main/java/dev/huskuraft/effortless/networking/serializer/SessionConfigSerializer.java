package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
import dev.huskuraft.effortless.session.config.SessionConfig;

public class SessionConfigSerializer implements BufferSerializer<SessionConfig> {

    @Override
    public SessionConfig read(Buffer buffer) {
        return new SessionConfig(
                buffer.read(new GeneralConfigSerializer()),
                buffer.readMap(Buffer::readUUID, new GeneralConfigSerializer())
        );
    }

    @Override
    public void write(Buffer buffer, SessionConfig sessionConfig) {
        buffer.write(sessionConfig.globalConfig(), new GeneralConfigSerializer());
        buffer.writeMap(sessionConfig.playerConfigs(), Buffer::writeUUID, new GeneralConfigSerializer());
    }

}
