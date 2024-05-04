package dev.huskuraft.effortless.api.tag;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface TagElement extends PlatformReference {

    default TagRecord asRecord() {
        return (TagRecord) this;
    }

    default TagList asList() {
        return (TagList) this;
    }

    default TagLiteral asLiteral() {
        return (TagLiteral) this;
    }

    default TagPrimitive asPrimitive() {
        return (TagPrimitive) this;
    }

    byte getId();

    String getAsString();

}
