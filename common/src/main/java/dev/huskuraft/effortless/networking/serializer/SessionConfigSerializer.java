package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.session.config.SessionConfig;

public class SessionConfigSerializer implements NetByteBufSerializer<SessionConfig> {

    @Override
    public SessionConfig read(NetByteBuf byteBuf) {
        return new SessionConfig(
                byteBuf.read(new GeneralConfigSerializer()),
                byteBuf.readMap(NetByteBuf::readUUID, new GeneralConfigSerializer())
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, SessionConfig sessionConfig) {
        byteBuf.write(sessionConfig.globalConfig(), new GeneralConfigSerializer());
        byteBuf.writeMap(sessionConfig.playerConfigs(), NetByteBuf::writeUUID, new GeneralConfigSerializer());
    }

}
