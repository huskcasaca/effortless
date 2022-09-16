package dev.huskuraft.effortless.gui.slot;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

// FIXME: 27/9/23 render
public class ItemSlot extends AbstractWidget {

    private ItemStack itemStack;

    public ItemSlot(Entrance entrance, int x, int y, int width, int height, ItemStack itemStack, Text message) {
        super(entrance, x, y, width, height, message);
        this.itemStack = itemStack;
    }


    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        renderer.drawItemSlotBackgroundTexture(getX() + 1, getY() + 1);
        renderer.drawItem(getTypeface(), itemStack, getX() + 1, getY() + 1, getMessage());
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
