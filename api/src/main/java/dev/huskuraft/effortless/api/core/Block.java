
package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Block extends PlatformReference {

    BlockState getDefaultBlockState();

}