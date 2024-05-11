package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;

public abstract class AbstractContainerScreen extends AbstractScreen {

    public static final int CONTAINER_WIDTH_NORMAL = 56 * 4 + 3 * Button.COMPAT_SPACING_V + 2 * Button.PADDINGS;
    public static final int CONTAINER_WIDTH_THIN = 42 * 4 + 3 * Button.COMPAT_SPACING_V + 2 * Button.PADDINGS;
    public static final int CONTAINER_HEIGHT_NORMAL = 260;
    public static final int CONTAINER_HEIGHT_THIN = 180;

    public static final int BUTTON_CONTAINER_ROW_1 = Dimens.Buttons.HEIGHT * 1 + Button.COMPAT_SPACING_H * 1 + Button.PADDINGS;
    public static final int BUTTON_CONTAINER_ROW_2 = Dimens.Buttons.HEIGHT * 2 + Button.COMPAT_SPACING_H * 2 + Button.PADDINGS;
    public static final int BUTTON_CONTAINER_ROW_3 = Dimens.Buttons.HEIGHT * 3 + Button.COMPAT_SPACING_H * 3 + Button.PADDINGS;
    public static final int BUTTON_CONTAINER_ROW_4 = Dimens.Buttons.HEIGHT * 4 + Button.COMPAT_SPACING_H * 4 + Button.PADDINGS;
    public static final int BUTTON_CONTAINER_ROW_5 = Dimens.Buttons.HEIGHT * 5 + Button.COMPAT_SPACING_H * 5 + Button.PADDINGS;
    public static final int BUTTON_CONTAINER_ROW_6 = Dimens.Buttons.HEIGHT * 6 + Button.COMPAT_SPACING_H * 6 + Button.PADDINGS;

    public static final int BUTTON_CONTAINER_ROW_C1 = Dimens.Buttons.HEIGHT * 1 + Button.COMPAT_SPACING_H * 1 + Button.COMPAT_SPACING_H;
    public static final int BUTTON_CONTAINER_ROW_C2 = Dimens.Buttons.HEIGHT * 2 + Button.COMPAT_SPACING_H * 2 + Button.COMPAT_SPACING_H;
    public static final int BUTTON_CONTAINER_ROW_C3 = Dimens.Buttons.HEIGHT * 3 + Button.COMPAT_SPACING_H * 3 + Button.COMPAT_SPACING_H;

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
