package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.BlockEntity;

public record MinecraftBlockEntity(net.minecraft.world.level.block.entity.BlockEntity referenceValue) implements BlockEntity {

    public static BlockEntity ofNullable(net.minecraft.world.level.block.entity.BlockEntity value) {
        return value == null ? null : new MinecraftBlockEntity(value);
    }
}
