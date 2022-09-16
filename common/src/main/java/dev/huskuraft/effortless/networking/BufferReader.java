package dev.huskuraft.effortless.networking;

public interface BufferReader<T> {

    T read(Buffer buffer);

}
