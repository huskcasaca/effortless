package dev.huskuraft.effortless.api.tag;

import dev.huskuraft.effortless.api.platform.TagFactory;

public interface TagLiteral extends TagElement {

    static TagLiteral of(String value) {
        return TagFactory.getInstance().newLiteral(value);
    }

}
