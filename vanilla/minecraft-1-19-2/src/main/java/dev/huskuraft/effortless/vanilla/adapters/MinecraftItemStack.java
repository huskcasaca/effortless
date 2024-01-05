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

    public static ItemStack fromMinecraft(net.minecraft.world.item.ItemStack itemStack) {
        return new MinecraftItemStack(itemStack);
    }

    public static ItemStack fromMinecraft(net.minecraft.world.item.Item item, int count) {
        return new MinecraftItemStack(item, count);
    }

    public static ItemStack fromMinecraft(net.minecraft.world.item.Item item, CompoundTag tag, int count) {
        return new MinecraftItemStack(item, tag, count);
    }

    public static net.minecraft.world.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return ((MinecraftItemStack) itemStack).reference;
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
        return MinecraftItem.fromMinecraft(reference.getItem());
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
        return MinecraftText.fromMinecraftText(reference.getHoverName());
    }

    @Override
    public List<Text> getTooltips(Player player, TooltipType flag) {
        return reference.getTooltipLines(MinecraftPlayer.toMinecraftPlayer(player), switch (flag) {
            case NORMAL -> TooltipFlag.Default.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.Default.NORMAL;
            case ADVANCED -> TooltipFlag.Default.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.Default.ADVANCED;
        }).stream().map(MinecraftText::fromMinecraftText).toList();
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
        return MinecraftItemStack.fromMinecraft(reference.copy());
    }

    @Override
    public boolean isItem(Item item) {
        return reference.is(MinecraftItem.toMinecraft(item));
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
        return MinecraftTagRecord.fromMinecraft(reference.getOrCreateTag());
    }

    @Override
    public void setTag(TagRecord tagRecord) {
        reference.setTag(MinecraftTagRecord.toMinecraft(tagRecord));
    }

    @Override
    public BlockState getBlockState(Player player, BlockInteraction interaction) {

        return MinecraftBlockState.fromMinecraftBlockState(Block.byItem(reference.getItem()).getStateForPlacement(new BlockPlaceContext(
                MinecraftPlayer.toMinecraftPlayer(player),
                MinecraftPlayer.toMinecraftInteractionHand(interaction.getHand()),
                MinecraftItemStack.toMinecraftItemStack(this),
                MinecraftPlayer.toMinecraftBlockInteraction(interaction)
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