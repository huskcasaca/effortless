package dev.huskuraft.effortless.gui.slot;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class ItemSlot extends Slot {

    private ItemStack itemStack;

    public ItemSlot(Entrance entrance, int x, int y, int width, int height, Item item, Text message) {
        super(entrance, x, y, width, height, message);
        this.itemStack = item.getDefaultStack();
    }

    public ItemSlot(Entrance entrance, int x, int y, int width, int height, ItemStack itemStack, Text message) {
        super(entrance, x, y, width, height, message);
        this.itemStack = itemStack;
    }

    @Override
    public int getFullWidth() {
        return getWidth();
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

//        renderer.drawItemSlotBackgroundTexture(getX() + 1, getY() + 1);
        renderer.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x9f6c6c6c);
        renderer.drawItem(getTypeface(), itemStack, getX(), getY(), getMessage());
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setDescription(Text description) {
        this.setMessage(description);
    }

    private int getBlitOffset() {
        return 0;
    }

}
