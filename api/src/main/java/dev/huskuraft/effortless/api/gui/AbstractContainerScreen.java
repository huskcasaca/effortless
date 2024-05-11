package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;

public abstract class AbstractContainerScreen extends AbstractScreen {

    private int containerWidth;
    private int containerHeight;

    protected AbstractContainerScreen(Entrance entrance, Text title, int containerWidth, int containerHeight) {
        super(entrance, title);
        this.containerWidth = containerWidth;
        this.containerHeight = containerHeight;
    }

    @Override
    public void renderBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderBackground(renderer, mouseX, mouseY, deltaTick);
        renderer.renderSprite(TextureFactory.getInstance().getDemoBackgroundTextureSprite(), getLeft(), getTop(), getWidth(), getHeight());
    }

    @Override
    public final int getWidth() {
        return containerWidth;
    }

    @Override
    public final int getHeight() {
        return containerHeight;
    }

    public void setContainerWidth(int containerWidth) {
        this.containerWidth = containerWidth;
    }

    public void setContainerHeight(int height) {
        this.containerHeight = height;
    }

    @Override
    public int getX() {
        return super.getX() + super.getWidth() / 2 - getWidth() / 2;
    }

    @Override
    public int getY() {
        return super.getY() + super.getHeight() / 2 - getHeight() / 2;
    }

}
