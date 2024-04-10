package dev.huskuraft.effortless.api.tag;

public interface TagReader<T> {

    T read(TagElement tag);

    default T validate(T value) {
        return value;
    }

    default T read(TagElement tag, boolean validate) {
        return validate ? validate(read(tag)) : read(tag);
    }

}
