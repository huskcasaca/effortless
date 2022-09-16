package dev.huskuraft.effortless.tag;

public interface TagWriter<T> {

    void write(TagElement tag, T t);

}
