package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.session.config.SessionConfig;

public class SessionConfigSerializer implements NetByteBufSerializer<SessionConfig> {

    @Override
    public SessionConfig read(NetByteBuf byteBuf) {
        return new SessionConfig(
                byteBuf.read(new ConstraintConfigSerializer()),
                byteBuf.readMap(NetByteBuf::readUUID, new ConstraintConfigSerializer())
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, SessionConfig sessionConfig) {
        byteBuf.write(sessionConfig.globalConfig(), new ConstraintConfigSerializer());
        byteBuf.writeMap(sessionConfig.playerConfigs(), NetByteBuf::writeUUID, new ConstraintConfigSerializer());
    }

}
