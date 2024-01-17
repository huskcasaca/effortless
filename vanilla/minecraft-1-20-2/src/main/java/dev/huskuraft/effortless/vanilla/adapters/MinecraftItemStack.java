package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.*;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.text.Text;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

import java.util.List;

class MinecraftItemStack implements ItemStack {

    private final net.minecraft.world.item.ItemStack reference;

    MinecraftItemStack(net.minecraft.world.item.ItemStack itemStack) {
        this.reference = itemStack;
    }

    MinecraftItemStack(net.minecraft.world.item.Item item, int count) {
        this.reference = new net.minecraft.world.item.ItemStack(item, count);
    }

    MinecraftItemStack(net.minecraft.world.item.Item item, CompoundTag tag, int count) {
        this.reference = new net.minecraft.world.item.ItemStack(item, count);
        this.reference.setTag(tag);
    }

    @Override
    public net.minecraft.world.item.ItemStack referenceValue() {
        return reference;
    }

    @Override
    public boolean isEmpty() {
        return reference.isEmpty();
    }

    @Override
    public boolean isAir() {
        return reference.getItem() instanceof AirItem;
    }

    @Override
    public boolean isBlock() {
        return reference.getItem() instanceof BlockItem;
    }

    @Override
    public Item getItem() {
        return MinecraftConvertor.fromPlatformItem(reference.getItem());
    }

    @Override
    public int getStackSize() {
        return reference.getCount();
    }

    @Override
    public void setStackSize(int count) {
        reference.setCount(count);
    }

    @Override
    public int getMaxStackSize() {
        return reference.getMaxStackSize();
    }

    @Override
    public Text getHoverName() {
        return MinecraftConvertor.fromPlatformText(reference.getHoverName());
    }

    @Override
    public List<Text> getTooltips(Player player, TooltipType flag) {
        return reference.getTooltipLines(MinecraftConvertor.toPlatformPlayer(player), switch (flag) {
            case NORMAL -> TooltipFlag.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.NORMAL.asCreative();
            case ADVANCED -> TooltipFlag.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.ADVANCED.asCreative();
        }).stream().map(MinecraftConvertor::fromPlatformText).toList();
    }

    @Override
    public void increase(int count) {
        reference.grow(count);
    }

    @Override
    public void decrease(int count) {
        reference.shrink(count);
    }

    @Override
    public ItemStack copy() {
        return MinecraftConvertor.fromPlatformItemStack(reference.copy());
    }

    @Override
    public boolean isItem(Item item) {
        return reference.is(MinecraftConvertor.toPlatformItem(item));
    }

    @Override
    public boolean itemEquals(ItemStack itemStack) {
        return reference.is(((MinecraftItemStack) itemStack).reference.getItem());
    }

    @Override
    public boolean tagEquals(ItemStack itemStack) {
        return net.minecraft.world.item.ItemStack.isSameItemSameTags(reference, ((MinecraftItemStack) itemStack).reference);
    }

    @Override
    public TagRecord getTag() {
        return MinecraftConvertor.fromPlatformTagRecord(reference.getOrCreateTag());
    }

    @Override
    public void setTag(TagRecord tagRecord) {
        reference.setTag(MinecraftConvertor.toPlatformTagRecord(tagRecord));
    }

    @Override
    public BlockState getBlockState(Player player, BlockInteraction interaction) {

        return MinecraftConvertor.fromPlatformBlockState(Block.byItem(reference.getItem()).getStateForPlacement(new BlockPlaceContext(
                MinecraftConvertor.toPlatformPlayer(player),
                MinecraftConvertor.toPlatformInteractionHand(interaction.getHand()),
                MinecraftConvertor.toPlatformItemStack(this),
                MinecraftConvertor.toPlatformBlockInteraction(interaction)
        )));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftItemStack itemStack && reference.equals(itemStack.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
