package dev.huskuraft.effortless.api.core;

import java.io.IOException;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Resource extends PlatformReference {

    ResourceLocation location();

    ResourceMetadata metadata() throws IOException;

}
