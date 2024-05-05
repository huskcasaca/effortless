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

    List<Text> getTooltips(Player player, TooltipType flag);

    Item getItem();

    int getCount();

    void setCount(int count);

    int getMaxStackSize();

    Text getHoverName();

    ItemStack copy();

    TagRecord getTag();

    void setTag(TagRecord tagRecord);

    default void increase(int count) {
        setCount(getCount() + count);
    }

    default void decrease(int count) {
        setCount(getCount() - count);
    }

    default boolean isEmpty() {
        return this == empty() || getItem().equals(Items.AIR.item()) || getCount() <= 0;
    }

    default boolean isAir() {
        return getItem().equals(Items.AIR.item());
    }

    default boolean isBlock() {
        return getItem() instanceof BlockItem;
    }

    enum TooltipType {
        NORMAL,
        NORMAL_CREATIVE,
        ADVANCED,
        ADVANCED_CREATIVE
    }

}
