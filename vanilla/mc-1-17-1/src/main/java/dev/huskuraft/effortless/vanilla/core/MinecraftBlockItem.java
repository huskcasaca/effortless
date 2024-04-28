package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockItem;
import dev.huskuraft.effortless.api.core.InteractionResult;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;

public record MinecraftBlockItem(net.minecraft.world.item.BlockItem referenceValue) implements BlockItem {

    @Override
    public ItemStack getDefaultStack() {
        return new MinecraftItem(referenceValue()).getDefaultStack();
    }

    @Override
    public Block getBlock() {
        return new MinecraftItem(referenceValue()).getBlock();
    }

    @Override
    public ResourceLocation getId() {
        return new MinecraftItem(referenceValue()).getId();
    }

    @Override
    public InteractionResult use(Player player, BlockInteraction blockInteraction) {
        return new MinecraftItem(referenceValue()).use(player, blockInteraction);
    }

    @Override
    public InteractionResult place(Player player, BlockInteraction blockInteraction) {
        return MinecraftConvertor.toPlatformInteractionResult(referenceValue().place(new BlockPlaceContext(player.reference(), MinecraftConvertor.toPlatformInteractionHand(blockInteraction.getHand()), player.getItemStack(blockInteraction.getHand()).reference(), MinecraftConvertor.toPlatformBlockInteraction(blockInteraction))));
    }
}
