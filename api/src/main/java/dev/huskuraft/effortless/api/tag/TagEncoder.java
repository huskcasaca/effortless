package dev.huskuraft.effortless.api.tag;

public interface TagEncoder<T> {

    TagElement encode(T t);

    default T validate(T value) {
        return value;
    }

    default TagElement encode(T t, boolean validate) {
        return encode(validate ? validate(t) : t);
    }

}
