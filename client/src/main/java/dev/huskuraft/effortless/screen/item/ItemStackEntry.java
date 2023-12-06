package dev.huskuraft.effortless.screen.item;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.slot.ItemSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;

public class ItemStackEntry extends EditableEntry<ItemStack> {

    private ItemSlot itemSlot;
    private TextWidget nameTextWidget;

    public ItemStackEntry(Entrance entrance, ItemStack itemStack) {
        super(entrance, itemStack);
    }

    @Override
    public void onCreate() {
        this.itemSlot = addWidget(new ItemSlot(getEntrance(), getX() + 1, getY() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getItem(), Text.empty()));
        this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 24, getY() + 6, getDisplayName(getItem())));
    }

    @Override
    public void onBindItem() {
        itemSlot.setItemStack(item);
        nameTextWidget.setMessage(getDisplayName(item));
    }

    @Override
    public Text getNarration() {
        return Text.translate("narrator.select", getDisplayName(getItem()));
    }

    @Override
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        return 24;
    }

    private Text getDisplayName(ItemStack itemStack) {
        return itemStack.getHoverName();
    }

}
