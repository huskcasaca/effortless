package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializable;
import dev.huskuraft.effortless.api.text.Text;

import java.util.List;

public interface ItemStack extends TagSerializable, PlatformReference {

    static ItemStack empty() {
        return Entrance.getInstance().getPlatform().newItemStack();
    }

    static ItemStack of(Item item, int count) {
        return Entrance.getInstance().getPlatform().newItemStack(item, count);
    }

    static ItemStack of(Item item, int count, TagRecord tag) {
        return Entrance.getInstance().getPlatform().newItemStack(item, count, tag);
    }

    List<Text> getTooltips(Player player, TooltipType flag);

    boolean isEmpty();

    boolean isAir();

    boolean isBlock();

    Item getItem();

    int getStackSize();

    void setStackSize(int count);

    int getMaxStackSize();

    Text getHoverName();

    void increase(int count);

    void decrease(int count);

    ItemStack copy();

    TagRecord getTag();

    void setTag(TagRecord tagRecord);

    boolean isItem(Item item);

    boolean itemEquals(ItemStack itemStack);

    boolean tagEquals(ItemStack itemStack);

    BlockState getBlockState(Player player, BlockInteraction interaction);

    enum TooltipType {
        NORMAL,
        NORMAL_CREATIVE,
        ADVANCED,
        ADVANCED_CREATIVE
    }

}
