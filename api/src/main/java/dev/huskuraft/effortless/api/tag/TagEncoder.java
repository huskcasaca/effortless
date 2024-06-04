package dev.huskuraft.effortless.api.tag;

public interface TagEncoder<T> {

    Tag encode(T t);

    default T validate(T value) {
        return value;
    }

    default Tag encode(T t, boolean validate) {
        return encode(validate ? validate(t) : t);
    }

}
