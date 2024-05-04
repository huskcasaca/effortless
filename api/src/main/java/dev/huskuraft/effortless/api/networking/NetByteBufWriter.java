package dev.huskuraft.effortless.api.networking;

public interface NetByteBufWriter<T> {

    void write(NetByteBuf byteBuf, T t);

}
