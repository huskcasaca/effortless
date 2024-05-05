package dev.huskuraft.effortless.api.networking;

public interface NetByteBufReader<T> {

    T read(NetByteBuf byteBuf);

}
