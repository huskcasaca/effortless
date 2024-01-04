package dev.huskuraft.effortless.api.networking;

public interface BufferWriter<T> {

    void write(Buffer buffer, T t);

}
