package dev.huskuraft.effortless.api.tag;

public interface TagWriter<T> {

    void write(TagElement tag, T t);

}
