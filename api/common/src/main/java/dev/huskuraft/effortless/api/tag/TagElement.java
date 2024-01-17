package dev.huskuraft.effortless.api.tag;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface TagElement extends PlatformReference {

    TagRecord asRecord();

    TagPrimitive asPrimitive();

}
