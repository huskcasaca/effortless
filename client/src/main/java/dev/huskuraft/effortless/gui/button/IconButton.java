package dev.huskuraft.effortless.gui.button;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class IconButton extends Button {

    private final Resource resource;
    private final int iconX, iconY, iconWidth, iconHeight, iconAltX, iconAltY;
    private boolean useAltIcon = false;

    public IconButton(Entrance entrance, int x, int y, int iconX, int iconY, Resource resource, OnPress onPress) {
        this(entrance, x, y, 20, 20, iconX, iconY, 20, 20, 20, 0, resource, onPress);
    }

    public IconButton(Entrance entrance, int x, int y, int width, int height, int iconX, int iconY, int iconWidth, int iconHeight, int iconAltX, int iconAltY, Resource resource, OnPress onPress) {
        super(entrance, x, y, width, height, Text.empty(), onPress);
        this.iconX = iconX;
        this.iconY = iconY;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.iconAltX = iconAltX;
        this.iconAltY = iconAltY;
        this.resource = resource;
    }

    public void setUseAlternateIcon(boolean useAlternateIcon) {
        this.useAltIcon = useAlternateIcon;
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

//        if (this.isVisible()) {
//            this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());
        int currentIconX = this.iconX;
        int currentIconY = this.iconY;

        if (useAltIcon) {
            currentIconX += iconAltX;
            currentIconY += iconAltY;
        }

        // draws a textured rectangle at the current z-value. Used to be drawTexturedModalRect in Gui.
        renderer.drawTexture(this.resource, this.getX(), this.getY(), currentIconX, currentIconY, this.iconWidth, this.iconHeight);
//        }
    }

}
