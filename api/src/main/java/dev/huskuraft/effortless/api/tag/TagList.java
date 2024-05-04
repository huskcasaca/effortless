package dev.huskuraft.effortless.api.tag;

import java.util.Collection;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.platform.TagFactory;

public interface TagList extends TagElement {

    static TagList of(Collection<TagElement> tags) {
        return TagFactory.getInstance().newList();
    }

    default boolean addTag(TagElement tag) {
        return addTag(size(), tag);
    }

    boolean addTag(int index, TagElement tag);

    boolean setTag(int index, TagElement tag);

    TagElement get(int index);

    int size();

    Stream<TagElement> stream();

}
