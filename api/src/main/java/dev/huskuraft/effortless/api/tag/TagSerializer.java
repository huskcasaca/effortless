package dev.huskuraft.effortless.api.tag;

public interface TagSerializer<T> extends TagReader<T>, TagWriter<T> {

    default T validate(T value) {
        return value;
    }

}
