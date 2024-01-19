package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;

import java.io.IOException;

public interface Resource extends PlatformReference {

    ResourceLocation location();

    ResourceMetadata metadata() throws IOException;

}
