package dev.huskuraft.effortless.tag;

public interface TagReader<T> {

    T read(TagElement tag);

}
