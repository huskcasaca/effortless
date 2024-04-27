package dev.huskuraft.effortless.api.core;

import java.util.List;

import dev.huskuraft.effortless.api.text.Text;

public interface Player extends Entity {

    PlayerProfile getProfile();

    default boolean isOperator() {
        return getServer().getPlayerList().isOperator(getProfile());
    }

    List<ItemStack> getItemStacks();

    void setItemStack(int index, ItemStack itemStack);

    ItemStack getItemStack(InteractionHand hand);

    void setItemStack(InteractionHand hand, ItemStack itemStack);

    void sendMessage(Text messages);

    default void sendMessage(String message) {
        sendMessage(Text.text(message));
    }

    void sendClientMessage(Text message, boolean actionBar);

    default void sendClientMessage(String message, boolean actionBar) {
        sendClientMessage(Text.text(message), actionBar);
    }

    void swing(InteractionHand hand);

    boolean canInteractBlock(BlockPosition blockPosition);

    boolean canAttackBlock(BlockPosition blockPosition);

    boolean destroyBlock(BlockInteraction interaction);

    default boolean useItem(BlockInteraction interaction) {
        return getWorld().getBlockState(interaction.getBlockPosition()).use(this, interaction).consumesAction()
                || getItemStack(interaction.getHand()).getItem().use(this, interaction).consumesAction();
    }


}
