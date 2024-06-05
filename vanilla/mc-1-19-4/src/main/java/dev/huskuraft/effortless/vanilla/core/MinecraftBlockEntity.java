package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.BlockEntity;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.tag.RecordTag;
import dev.huskuraft.effortless.vanilla.tag.MinecraftRecordTag;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

public record MinecraftBlockEntity(net.minecraft.world.level.block.entity.BlockEntity refs) implements BlockEntity {

    public static BlockEntity ofNullable(net.minecraft.world.level.block.entity.BlockEntity refs) {
        if (refs == null) return null;
        if (refs instanceof BaseContainerBlockEntity baseContainerBlockEntity) return new MinecraftContainerBlockEntity(baseContainerBlockEntity);
        return new MinecraftBlockEntity(refs);
    }

    @Override
    public BlockState getBlockState() {
        return MinecraftBlockState.ofNullable(refs.getBlockState());
    }

    @Override
    public BlockPosition getBlockPosition() {
        return MinecraftConvertor.toPlatformBlockPosition(refs.getBlockPos());
    }

    @Override
    public World getWorld() {
        return MinecraftWorld.ofNullable(refs.getLevel());
    }

    @Override
    public RecordTag getTag() {
        return MinecraftRecordTag.ofNullable(refs.saveWithoutMetadata());
    }

    @Override
    public void setTag(RecordTag recordTag) {
        refs.load(recordTag.reference());
    }
}
