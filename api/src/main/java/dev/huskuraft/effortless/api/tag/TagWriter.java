package dev.huskuraft.effortless.api.tag;

public interface TagWriter<T> {

    void write(TagElement tag, T t);

    default T validate(T value) {
        return value;
    }

    default void write(TagElement tag, T t, boolean validate) {
        write(tag, validate ? validate(t): t);
    }

}
