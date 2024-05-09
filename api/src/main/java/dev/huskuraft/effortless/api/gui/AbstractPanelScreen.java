package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;

public abstract class AbstractPanelScreen extends AbstractScreen {

    protected AbstractPanelScreen(Entrance entrance, Text title) {
        super(entrance, title);
    }

    @Override
    public void renderBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderBackground(renderer, mouseX, mouseY, deltaTick);
        renderer.renderSprite(TextureFactory.getInstance().getDemoBackgroundTextureSprite(), getLeft(), getTop(), getWidth(), getHeight());
    }

    @Override
    public final int getWidth() {
        return getContainerWidth();
    }

    @Override
    public final int getHeight() {
        return getContainerHeight();
    }

    @Override
    public int getX() {
        return super.getX() + super.getWidth() / 2 - getWidth() / 2;
    }

    @Override
    public int getY() {
        return super.getY() + super.getHeight() / 2 - getHeight() / 2;
    }

    protected abstract int getContainerWidth();

    protected abstract int getContainerHeight();
}
