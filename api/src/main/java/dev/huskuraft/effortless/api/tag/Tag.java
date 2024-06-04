package dev.huskuraft.effortless.api.tag;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Tag extends PlatformReference {

    byte TAG_END = 0;
    byte TAG_BYTE = 1;
    byte TAG_SHORT = 2;
    byte TAG_INT = 3;
    byte TAG_LONG = 4;
    byte TAG_FLOAT = 5;
    byte TAG_DOUBLE = 6;
    byte TAG_BYTE_ARRAY = 7;
    byte TAG_STRING = 8;
    byte TAG_LIST = 9;
    byte TAG_COMPOUND = 10;
    byte TAG_INT_ARRAY = 11;
    byte TAG_LONG_ARRAY = 12;
    byte TAG_ANY_NUMERIC = 99;

    default RecordTag asRecord() {
        return (RecordTag) this;
    }

    default ListTag asList() {
        return (ListTag) this;
    }

    default StringTag asString() {
        return (StringTag) this;
    }

    default NumericTag asPrimitive() {
        return (NumericTag) this;
    }

    byte getId();

    String getAsString();

}
