package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.BlockEntity;

public record MinecraftBlockEntity(net.minecraft.world.level.block.entity.BlockEntity refs) implements BlockEntity {

    public static BlockEntity ofNullable(net.minecraft.world.level.block.entity.BlockEntity value) {
        if (value == null) return null;
        return new MinecraftBlockEntity(value);
    }
}
