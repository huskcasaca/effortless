package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;

public abstract class AbstractPanelScreen extends AbstractScreen {

    public static final int PANEL_WIDTH_EXPANDED = 56 * 4 + 3 * Button.COMPAT_SPACING_V + 2 * Button.PADDINGS;
    public static final int PANEL_WIDTH = 42 * 4 + 3 * Button.COMPAT_SPACING_V + 2 * Button.PADDINGS;

    public static final int PANEL_HEIGHT_270 = 270;
    public static final int PANEL_HEIGHT_180 = 180;
    public static final int PANEL_HEIGHT_90 = 90;
    public static final int PANEL_HEIGHT_45 = 45;

    public static final int PANEL_BUTTON_ROW_HEIGHT_1 = Dimens.Buttons.HEIGHT * 1 + Button.COMPAT_SPACING_H * 1 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_2 = Dimens.Buttons.HEIGHT * 2 + Button.COMPAT_SPACING_H * 2 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_3 = Dimens.Buttons.HEIGHT * 3 + Button.COMPAT_SPACING_H * 3 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_4 = Dimens.Buttons.HEIGHT * 4 + Button.COMPAT_SPACING_H * 4 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_5 = Dimens.Buttons.HEIGHT * 5 + Button.COMPAT_SPACING_H * 5 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_6 = Dimens.Buttons.HEIGHT * 6 + Button.COMPAT_SPACING_H * 6 + Button.PADDINGS;

    public static final int PANEL_BUTTON_ROW_HEIGHT_1N = Dimens.Buttons.HEIGHT * 1 + Button.COMPAT_SPACING_H * 1 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_2N = Dimens.Buttons.HEIGHT * 2 + Button.COMPAT_SPACING_H * 2 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_3N = Dimens.Buttons.HEIGHT * 3 + Button.COMPAT_SPACING_H * 3 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_4N = Dimens.Buttons.HEIGHT * 4 + Button.COMPAT_SPACING_H * 4 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_5N = Dimens.Buttons.HEIGHT * 5 + Button.COMPAT_SPACING_H * 5 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_6N = Dimens.Buttons.HEIGHT * 6 + Button.COMPAT_SPACING_H * 6 + Button.COMPAT_SPACING_H;

    public static final int UNSPECIFIC = 0;

    public static final int PADDINGS = 6;
    public static final int PANEL_TITLE_HEIGHT_1 = 18;
    public static final int PANEL_TITLE_HEIGHT_2 = 24;

    private int panelWidth;
    private int panelHeight;

    protected AbstractPanelScreen(Entrance entrance, Text title, int panelWidth, int panelHeight) {
        super(entrance, title);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
    }

    protected AbstractPanelScreen(Entrance entrance, Text title) {
        this(entrance, title, UNSPECIFIC, UNSPECIFIC);
    }

    protected AbstractPanelScreen(Entrance entrance) {
        this(entrance, Text.empty(), UNSPECIFIC, UNSPECIFIC);
    }

    @Override
    public void renderBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderBackground(renderer, mouseX, mouseY, deltaTick);
        renderer.renderSprite(TextureFactory.getInstance().getDemoBackgroundTextureSprite(), getLeft(), getTop(), getWidth(), getHeight());
    }

    @Override
    public int getWidth() {
        return panelWidth;
    }

    @Override
    public int getHeight() {
        return panelHeight;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    public void setPanelHeight(int height) {
        this.panelHeight = height;
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
