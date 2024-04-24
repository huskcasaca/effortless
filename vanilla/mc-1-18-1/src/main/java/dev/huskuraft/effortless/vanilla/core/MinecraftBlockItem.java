package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockItem;

public class MinecraftBlockItem extends MinecraftItem implements BlockItem {

    public MinecraftBlockItem(net.minecraft.world.item.BlockItem reference) {
        super(reference);
    }

    @Override
    public net.minecraft.world.item.BlockItem referenceValue() {
        return (net.minecraft.world.item.BlockItem) super.referenceValue();
    }

    @Override
    public Block getBlock() {
        return new MinecraftBlock(referenceValue().getBlock());
    }

}
