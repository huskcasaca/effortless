package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.universal.api.platform.LoaderType;
import dev.huskuraft.effortless.session.Session;

public class SessionSerializer implements NetByteBufSerializer<Session> {

    @Override
    public Session read(NetByteBuf byteBuf) {
        return new Session(
                byteBuf.readEnum(LoaderType.class),
                byteBuf.readString(),
                byteBuf.readString(),
                byteBuf.readList(new ModSerializer()),
                byteBuf.readInt()
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, Session session) {
        byteBuf.writeEnum(session.loaderType());
        byteBuf.writeString(session.loaderVersion());
        byteBuf.writeString(session.gameVersion());
        byteBuf.writeList(session.mods(), new ModSerializer());
        byteBuf.writeInt(session.protocolVersion());
    }
}
