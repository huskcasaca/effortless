package dev.huskuraft.effortless.api.core;

import java.util.List;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializable;
import dev.huskuraft.effortless.api.text.Text;

public interface ItemStack extends TagSerializable, PlatformReference {

    static ItemStack empty() {
        return ContentFactory.getInstance().newItemStack();
    }

    static ItemStack of(Item item, int count) {
        return ContentFactory.getInstance().newItemStack(item, count);
    }

    static ItemStack of(Item item, int count, TagRecord tag) {
        return ContentFactory.getInstance().newItemStack(item, count, tag);
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
