package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class MinecraftItemStack extends ItemStack {

    private final net.minecraft.world.item.ItemStack itemStack;

    public MinecraftItemStack(net.minecraft.world.item.ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public MinecraftItemStack(net.minecraft.world.item.Item item, int count) {
        this.itemStack = new net.minecraft.world.item.ItemStack(item, count);
    }

    public MinecraftItemStack(net.minecraft.world.item.Item item, CompoundTag tag, int count) {
        this.itemStack = new net.minecraft.world.item.ItemStack(item, count);
        this.itemStack.setTag(tag);
    }

    public static net.minecraft.world.item.ItemStack toMinecraftItemStack(ItemStack itemStack) {
        return ((MinecraftItemStack) itemStack).getRef();
    }

    private net.minecraft.world.item.ItemStack getRef() {
        return itemStack;
    }

    @Override
    public boolean isEmpty() {
        return getRef().isEmpty();
    }

    @Override
    public boolean isAir() {
        return getRef().getItem() instanceof AirItem;
    }

    @Override
    public boolean isBlock() {
        return getRef().getItem() instanceof BlockItem;
    }

    @Override
    public Item getItem() {
        return new MinecraftItem(getRef().getItem());
    }

    @Override
    public int getStackSize() {
        return getRef().getCount();
    }

    @Override
    public void setStackSize(int count) {
        getRef().setCount(count);
    }

    @Override
    public int getMaxStackSize() {
        return getRef().getMaxStackSize();
    }

    @Override
    public Text getHoverName() {
        return MinecraftText.fromMinecraftText(getRef().getHoverName());
    }

    @Override
    public List<Text> getDescription(Player player, TooltipType flag) {
        return getRef().getTooltipLines(MinecraftPlayer.toMinecraftPlayer(player), switch (flag) {
            case NORMAL -> TooltipFlag.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.NORMAL.asCreative();
            case ADVANCED -> TooltipFlag.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.ADVANCED.asCreative();
        }).stream().map(MinecraftText::fromMinecraftText).toList();
    }

    @Override
    public void increase(int count) {
        getRef().grow(count);
    }

    @Override
    public void decrease(int count) {
        getRef().shrink(count);
    }

    @Override
    public ItemStack copy() {
        return new MinecraftItemStack(getRef().copy());
    }

    @Override
    public boolean isItem(Item item) {
        return getRef().is(((MinecraftItem) item).getRef());
    }

    @Override
    public boolean itemEquals(ItemStack itemStack) {
        return getRef().is(((MinecraftItemStack) itemStack).getRef().getItem());
    }

    @Override
    public boolean tagEquals(ItemStack itemStack) {
        return net.minecraft.world.item.ItemStack.isSameItemSameTags(getRef(), ((MinecraftItemStack) itemStack).getRef());
    }

    @Override
    public TagRecord getTag() {
        return MinecraftTagRecord.toTagRecord(getRef().getOrCreateTag());
    }

    @Override
    public void setTag(TagRecord tagRecord) {
        getRef().setTag(MinecraftTagRecord.toMinecraft(tagRecord));
    }

    @Override
    public BlockData getBlockData(Player player, BlockInteraction interaction) {

        var blockPlaceContext = new BlockPlaceContext(
                MinecraftPlayer.toMinecraftPlayer(player),
                MinecraftBasicTypes.toMinecraftInteractionHand(interaction.getHand()),
                MinecraftItemStack.toMinecraftItemStack(this),
                MinecraftBasicTypes.toMinecraftBlockInteraction(interaction)
        );

        return new MinecraftBlockData(Block.byItem(getRef().getItem()).getStateForPlacement(blockPlaceContext));
    }
}
