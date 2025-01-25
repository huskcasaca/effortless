package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.Inventory;
import dev.huskuraft.effortless.api.core.ItemStack;

public record MinecraftInventory(net.minecraft.world.entity.player.Inventory refs) implements Inventory {

    @Override
    public ItemStack getItem(int index) {
        return new MinecraftContainer(refs).getItem(index);
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        new MinecraftContainer(refs).setItem(index, itemStack);
    }

    @Override
    public int getBagSize() {
        return refs.items.size();
    }

    @Override
    public int getArmorSize() {
        return refs.armor.size();
    }

    @Override
    public int getOffhandSize() {
        return refs.offhand.size();
    }

    @Override
    public int getHotbarSize() {
        return net.minecraft.world.entity.player.Inventory.getSelectionSize();
    }

    @Override
    public int getSelected() {
        return refs.selected;
    }

}
