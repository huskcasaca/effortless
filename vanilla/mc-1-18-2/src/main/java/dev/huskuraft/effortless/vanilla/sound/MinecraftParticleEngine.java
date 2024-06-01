package dev.huskuraft.effortless.vanilla.sound;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Direction;
import dev.huskuraft.effortless.api.platform.ParticleEngine;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;

public record MinecraftParticleEngine(
        net.minecraft.client.particle.ParticleEngine refs
) implements ParticleEngine {

    @Override
    public void destroy(BlockPosition blockPosition, BlockState blockState) {
        refs.destroy(MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference());
    }

    @Override
    public void crack(BlockPosition blockPosition, Direction direction) {
        refs.crack(MinecraftConvertor.toPlatformBlockPosition(blockPosition), MinecraftConvertor.toPlatformDirection(direction));
    }

}
