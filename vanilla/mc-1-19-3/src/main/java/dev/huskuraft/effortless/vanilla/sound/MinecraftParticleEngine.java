package dev.huskuraft.effortless.vanilla.sound;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Orientation;
import dev.huskuraft.effortless.api.platform.ParticleEngine;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;

public record MinecraftParticleEngine(
        net.minecraft.client.particle.ParticleEngine referenceValue
) implements ParticleEngine {

    @Override
    public void destroy(BlockPosition blockPosition, BlockState blockState) {
        referenceValue().destroy(MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference());
    }

    @Override
    public void crack(BlockPosition blockPosition, Orientation orientation) {
        referenceValue().crack(MinecraftConvertor.toPlatformBlockPosition(blockPosition), MinecraftConvertor.toPlatformOrientation(orientation));
    }

}
