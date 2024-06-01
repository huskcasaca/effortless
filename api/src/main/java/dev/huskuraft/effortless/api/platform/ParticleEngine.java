package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Direction;

public interface ParticleEngine extends PlatformReference {

    void destroy(BlockPosition blockPosition, BlockState blockState);

    void crack(BlockPosition blockPosition, Direction direction);
}
