package dev.huskuraft.effortless.api.core;

import java.util.List;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.text.Text;

public interface ItemStack extends PlatformReference {

    static ItemStack empty() {
        return ContentFactory.getInstance().newItemStack();
    }

    static ItemStack of(Item item, int count) {
        return ContentFactory.getInstance().newItemStack(item, count);
    }

    List<Text> getTooltips(Player player, TooltipType flag);

    Item getItem();

    int getCount();

    void setCount(int count);

    Text getHoverName();

    ItemStack copy();

    default int getMaxStackSize() {
        return getItem().getMaxStackSize();
    }

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

    default boolean isDamaged() {
        return isDamageableItem() && this.getDamageValue() > 0;
    }

    default boolean isStackable() {
        return getMaxStackSize() > 1 && (!isDamageableItem() || !isDamaged());
    }

    int getDamageValue();

    void setDamageValue(int damage);

    int getMaxDamage();

    default int getDurabilityLeft() {
        return getMaxDamage() - getDamageValue();
    }

    boolean isDamageableItem();

    default boolean isCorrectToolForDrops(BlockState blockState) {
        return getItem().isCorrectToolForDrops(blockState);
    }

    default boolean damage(int damage) {
        if (isDamageableItem()) {
            setDamageValue(Math.min(getDamageValue() + damage, getMaxDamage()));
            return true;
        }
        return false;
    }

    default void mineBlock(World world, Player player, BlockPosition blockPosition, BlockState blockState) {
        if (getItem().mineBlock(world, player, blockPosition, blockState, this)) {
            player.awardStat(StatTypes.ITEM_USED.get(getItem()));
        }
    }

    default Text getName() {
        return getItem().getName(this);
    }

    default ItemStack withCount(int count) {
        ItemStack itemStack = copy();
        itemStack.setCount(count);
        return itemStack;
    }

    enum TooltipType {
        NORMAL,
        NORMAL_CREATIVE,
        ADVANCED,
        ADVANCED_CREATIVE
    }

}
