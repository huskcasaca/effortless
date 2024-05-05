package dev.huskuraft.effortless.api.tag;

public interface TagDecoder<T> {

    T decode(TagElement tag);

    default T validate(T value) {
        return value;
    }

    default T decode(TagElement tag, boolean validate) {
        return validate ? validate(decode(tag)) : decode(tag);
    }

}
