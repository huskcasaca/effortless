package dev.huskuraft.effortless.api.tag;

import java.util.Collection;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.platform.TagFactory;

public interface ListTag extends Tag {

    static ListTag newList() {
        return TagFactory.getInstance().newList();
    }

    static ListTag of(Collection<Tag> tags) {
        var tag = TagFactory.getInstance().newList();
        tags.forEach(tag::addTag);
        return tag;
    }

    default boolean addTag(Tag tag) {
        return addTag(size(), tag);
    }

    boolean addTag(int index, Tag tag);

    boolean setTag(int index, Tag tag);

    Tag getTag(int index);

    int size();

    Stream<Tag> stream();

}
