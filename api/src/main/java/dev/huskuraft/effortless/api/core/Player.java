package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.text.Text;

public interface Player extends Entity {

    PlayerProfile getProfile();

    Inventory getInventory();

    default boolean isOperator() {
        return getServer().isOperator(getProfile());
    }

    default boolean isSinglePlayerOwner() {
        return getServer().isSinglePlayerOwner(getProfile());
    }

    default ItemStack getItemStack(InteractionHand hand) {
        return switch (hand) {
            case MAIN -> getItemBySlot(EquipmentSlot.MAINHAND);
            case OFF -> getItemBySlot(EquipmentSlot.OFFHAND);
        };
    }

    default void setItemStack(InteractionHand hand, ItemStack itemStack) {
        switch (hand) {
            case MAIN -> getInventory().setSelectedItem(itemStack);
            case OFF -> getInventory().setOffhandItem(itemStack);
        }
    }

    default ItemStack getItemBySlot(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> getInventory().getSelectedItem();
            case OFFHAND -> getInventory().getOffhandItem();
            case FEET, LEGS, CHEST, HEAD -> getInventory().getArmorItems().get(slot.getIndex());
        };
    }

    void drop(ItemStack itemStack, boolean dropAround, boolean includeThrowerName);

    void sendMessage(Text messages);

    default void sendMessage(String message) {
        sendMessage(Text.text(message));
    }

    void sendClientMessage(Text message, boolean actionBar);

    default void sendClientMessage(String message, boolean actionBar) {
        sendClientMessage(Text.text(message), actionBar);
    }

    void swing(InteractionHand hand);

    void awardStat(Stat<?> stat, int increment);

    default void awardStat(Stat<?> stat) {
        awardStat(stat, 1);
    }

    void resetStat(Stat<?> stat);


}
