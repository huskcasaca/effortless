package dev.huskuraft.effortless.api.networking;

public interface BufferReader<T> {

    T read(Buffer buffer);

}
