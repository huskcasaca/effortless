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
import java.util.stream.Collectors;

public class MinecraftItemStack implements ItemStack {

    private final net.minecraft.world.item.ItemStack reference;

    public MinecraftItemStack(net.minecraft.world.item.ItemStack itemStack) {
        this.reference = itemStack;
    }

    public MinecraftItemStack(net.minecraft.world.item.Item item, int count) {
        this.reference = new net.minecraft.world.item.ItemStack(item, count);
    }

    public MinecraftItemStack(net.minecraft.world.item.Item item, CompoundTag tag, int count) {
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
        return new MinecraftItem(reference.getItem());
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
        return new MinecraftText(reference.getHoverName());
    }

    @Override
    public List<Text> getTooltips(Player player, TooltipType flag) {
        var minecraftFlag = switch (flag) {
            case NORMAL -> TooltipFlag.Default.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.Default.NORMAL;
            case ADVANCED -> TooltipFlag.Default.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.Default.ADVANCED;
        };
        return reference.getTooltipLines(player.reference(), minecraftFlag).stream().map(text -> new MinecraftText(text)).collect(Collectors.toList());
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
        return new MinecraftItemStack(reference.copy());
    }

    @Override
    public boolean isItem(Item item) {
        return reference.is(item.<net.minecraft.world.item.Item>reference());
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
        return new MinecraftTagRecord(reference.getOrCreateTag());
    }

    @Override
    public void setTag(TagRecord tagRecord) {
        reference.setTag(tagRecord.reference());
    }

    @Override
    public BlockState getBlockState(Player player, BlockInteraction interaction) {

        return new MinecraftBlockState(Block.byItem(reference.getItem()).getStateForPlacement(new BlockPlaceContext(
                player.reference(),
                MinecraftConvertor.toPlatformInteractionHand(interaction.getHand()),
                reference(),
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
