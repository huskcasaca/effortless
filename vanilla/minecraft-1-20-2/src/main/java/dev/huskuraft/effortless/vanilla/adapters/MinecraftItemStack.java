package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
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

    public net.minecraft.world.item.ItemStack getRef() {
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
        return MinecraftAdapter.adapt(getRef().getItem());
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
        return MinecraftAdapter.adapt(getRef().getHoverName());
    }

    @Override
    public List<Text> getDescription(Player player, TooltipType flag) {
        return getRef().getTooltipLines(((MinecraftPlayer) player).getRef(), switch (flag) {
            case NORMAL -> TooltipFlag.NORMAL;
            case NORMAL_CREATIVE -> TooltipFlag.NORMAL.asCreative();
            case ADVANCED -> TooltipFlag.ADVANCED;
            case ADVANCED_CREATIVE -> TooltipFlag.ADVANCED.asCreative();
        }).stream().map(MinecraftAdapter::adapt).toList();
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
        return getRef().is(MinecraftAdapter.adapt(item));
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
        return MinecraftAdapter.adapt(getRef().getOrCreateTag());
    }

    @Override
    public void setTag(TagRecord tagRecord) {
        getRef().setTag(MinecraftAdapter.adapt(tagRecord));
    }

    @Override
    public BlockData getBlockData(Player player, BlockInteraction interaction) {
        var playerRef = ((MinecraftPlayer) player).getRef();
        var hitResultRef = MinecraftAdapter.adapt(interaction);
        var handRef = MinecraftAdapter.adapt(interaction.getHand());

        var blockPlaceContextRef = new BlockPlaceContext(playerRef, handRef, getRef(), hitResultRef);

        return MinecraftAdapter.adapt(Block.byItem(getRef().getItem()).getStateForPlacement(blockPlaceContextRef));
    }
}
