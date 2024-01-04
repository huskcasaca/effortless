package dev.huskuraft.effortless.api.tag;

public interface TagReader<T> {

    T read(TagElement tag);

}
