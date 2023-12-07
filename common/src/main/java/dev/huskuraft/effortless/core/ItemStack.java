package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.tag.TagSerializable;
import dev.huskuraft.effortless.text.Text;

import java.util.List;

public abstract class ItemStack implements TagSerializable {

    public static ItemStack empty() {
        return Effortless.getInstance().getContentCreator().emptyItemStack();
    }

    public static ItemStack of(Item item, int count) {
        return Effortless.getInstance().getContentCreator().emptyItemStack(item, count);
    }

    public static ItemStack of(Item item, int count, TagRecord tag) {
        return Effortless.getInstance().getContentCreator().emptyItemStack(item, count, tag);
    }

    public abstract List<Text> getDescription(Player player, TooltipType flag);

    public abstract boolean isEmpty();

    public abstract boolean isAir();

    public abstract boolean isBlock();

    public abstract Item getItem();

    public abstract int getStackSize();

    public abstract void setStackSize(int count);

    public abstract int getMaxStackSize();

    public abstract Text getHoverName();

    public abstract void increase(int count);

    public abstract void decrease(int count);

    public abstract ItemStack copy();

    public abstract TagRecord getTag();

    public abstract void setTag(TagRecord tagRecord);

    public abstract boolean isItem(Item item);

    public abstract boolean itemEquals(ItemStack itemStack);

    public abstract boolean tagEquals(ItemStack itemStack);

    public abstract BlockData getBlockData(Player player, BlockInteraction interaction);

    public enum TooltipType {
        NORMAL,
        NORMAL_CREATIVE,
        ADVANCED,
        ADVANCED_CREATIVE
    }


}
