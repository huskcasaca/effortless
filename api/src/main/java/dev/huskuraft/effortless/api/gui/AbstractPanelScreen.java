package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;
import dev.huskuraft.effortless.api.texture.TextureSprite;

public abstract class AbstractPanelScreen extends AbstractScreen {

    public static final int PANEL_WIDTH_EXPANDED = 56 * 4 + 3 * Button.COMPAT_SPACING_V + 2 * Button.PADDINGS;
    public static final int PANEL_WIDTH = 42 * 4 + 3 * Button.COMPAT_SPACING_V + 2 * Button.PADDINGS;

    public static final int PANEL_HEIGHT_270 = 270;
    public static final int PANEL_HEIGHT_180 = 180;
    public static final int PANEL_HEIGHT_90 = 90;
    public static final int PANEL_HEIGHT_45 = 45;

    public static final int PANEL_BUTTON_ROW_HEIGHT_1 = Dimens.Buttons.HEIGHT + Button.COMPAT_SPACING_H + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_2 = Dimens.Buttons.HEIGHT * 2 + Button.COMPAT_SPACING_H * 2 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_3 = Dimens.Buttons.HEIGHT * 3 + Button.COMPAT_SPACING_H * 3 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_4 = Dimens.Buttons.HEIGHT * 4 + Button.COMPAT_SPACING_H * 4 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_5 = Dimens.Buttons.HEIGHT * 5 + Button.COMPAT_SPACING_H * 5 + Button.PADDINGS;
    public static final int PANEL_BUTTON_ROW_HEIGHT_6 = Dimens.Buttons.HEIGHT * 6 + Button.COMPAT_SPACING_H * 6 + Button.PADDINGS;

    public static final int PANEL_BUTTON_ROW_HEIGHT_1N = Dimens.Buttons.HEIGHT + Button.COMPAT_SPACING_H + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_2N = Dimens.Buttons.HEIGHT * 2 + Button.COMPAT_SPACING_H * 2 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_3N = Dimens.Buttons.HEIGHT * 3 + Button.COMPAT_SPACING_H * 3 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_4N = Dimens.Buttons.HEIGHT * 4 + Button.COMPAT_SPACING_H * 4 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_5N = Dimens.Buttons.HEIGHT * 5 + Button.COMPAT_SPACING_H * 5 + Button.COMPAT_SPACING_H;
    public static final int PANEL_BUTTON_ROW_HEIGHT_6N = Dimens.Buttons.HEIGHT * 6 + Button.COMPAT_SPACING_H * 6 + Button.COMPAT_SPACING_H;

    public static final int UNSPECIFIC = 0;

    public static final int PADDINGS_H = 6;
    public static final int PADDINGS_V = 6;
    public static final int INNER_PADDINGS_H = Button.COMPAT_SPACING_H;
    public static final int INNER_PADDINGS_V = Button.COMPAT_SPACING_V;
    public static final int PANEL_TITLE_HEIGHT_1 = 18;
    public static final int PANEL_TITLE_HEIGHT_2 = 24;

    public static final TextureSprite DEMO_BACKGROUND_SPRITE = TextureFactory.getInstance().getDemoBackgroundTextureSprite();
    public static final int ANIMATED_OFFSET_Y = 12;
    public static final int MAX_ANIMATION_TICKS = 2;
    private int panelWidth;
    private int panelHeight;
    protected float animationTicks = 0;
    private boolean detached = false;

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
    public void onPartialTick(float partialTick) {
        this.animationTicks = Math.min(Math.max(animationTicks + (detached ? -1 : 1) * partialTick, 0), MAX_ANIMATION_TICKS) ;
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

    private float getAnimationFactor() {
        var fac = 1f - Math.min(animationTicks, MAX_ANIMATION_TICKS) / MAX_ANIMATION_TICKS;
        if (detached) {
            return 1 - MathUtils.lerp((1 - fac) * (1 - fac), 1f, 0f);
        }
        return 1 - MathUtils.lerp(fac * fac, 0f, 1f);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        if (detached) {
            super.detach();
        }
    }

    @Override
    public void detach() {
        this.detached = true;
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (detached && animationTicks == 0) {
            super.detach();
        }
        renderer.pushPose();
        renderer.translate(0, 0 * getAnimationFactor() * ANIMATED_OFFSET_Y, 0);
        renderer.translate(getX() + getWidth() / 2f, getY() + getHeight() / 2f, 0);
        renderer.scale(MathUtils.lerp(getAnimationFactor(), 0.92, 1));
        renderer.translate(-getX() - getWidth() / 2f, -getY() - getHeight() / 2f, 0);
        renderer.setRsShaderColor(1.0F, 1.0F, 1.0F, getAnimationFactor());
        renderer.renderSprite(DEMO_BACKGROUND_SPRITE, getLeft(), getTop(), getWidth(), getHeight());
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);
        renderer.popPose();
    }

}
