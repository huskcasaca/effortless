package dev.huskuraft.effortless.networking;

public interface BufferWriter<T> {

    void write(Buffer buffer, T t);

}
