package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockState;

public class MinecraftBlock implements Block {

    private final net.minecraft.world.level.block.Block reference;

    public MinecraftBlock(net.minecraft.world.level.block.Block reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.world.level.block.Block referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftBlock obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public BlockState getDefaultBlockState() {
        return MinecraftBlockState.ofNullable(referenceValue().defaultBlockState());
    }
}
