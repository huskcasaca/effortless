package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.BlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

public record MinecraftBlockEntity(net.minecraft.world.level.block.entity.BlockEntity refs) implements BlockEntity {

    public static BlockEntity ofNullable(net.minecraft.world.level.block.entity.BlockEntity value) {
        if (value == null) return null;
        if (value instanceof BaseContainerBlockEntity baseContainerBlockEntity) return new MinecraftContainerBlockEntity(baseContainerBlockEntity);
        return new MinecraftBlockEntity(value);
    }
}
