package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public abstract class AbstractScreen extends AbstractContainerWidget implements Screen {

    protected boolean transparentBackground = true;
    private int screenWidth = 0;
    private int screenHeight = 0;

    protected int getScreenWidth() {
        return screenWidth;
    }

    protected void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    protected int getScreenHeight() {
        return screenHeight;
    }

    protected void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    protected AbstractScreen(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
        this.focusable = true;
    }

    protected AbstractScreen(Entrance entrance, Text title) {
        super(entrance,  title);
    }

    protected AbstractScreen(Entrance entrance) {
        super(entrance);
    }

    public Text getScreenTitle() {
        return getMessage();
    }

    @Override
    public void init(int width, int height) {
        setScreenWidth(width);
        setScreenHeight(height);
        if (getWidth() == UNSPECIFIC_SIZE) {
            setWidth(width);
        }
        if (getHeight() == UNSPECIFIC_SIZE) {
            setHeight(height);
        }
        recreate();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onDestroy() {
        clearWidgets();
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public void attach() {
        getEntrance().getClientManager().pushScreen(this);
        onAttach();
    }

    @Override
    public void detach() {
        getEntrance().getClientManager().popScreen(this);
        onDetach();
    }

    public void detachAll() {
        getEntrance().getClientManager().pushScreen(null);
        onDetach();
    }

    @Override
    public boolean isPauseGame() {
        return false;
    }

    public void renderWidgetBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (isTransparentBackground() && getEntrance().getClient().isLoaded()) {
            renderer.renderGradientRect(0, 0, getScreenWidth(), getScreenHeight(), -1072689136, -804253680);
        } else {
            renderer.setRsShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
            renderer.renderPanelBackgroundTexture(0, 0, 0F, 0F, getWidth(), getHeight());
            renderer.setRsShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            this.detach();
            return true;
        } else if (keyCode == 258) {
            var isShiftDown = !getEntrance().getClient().getWindow().isShiftDown();
            if (!this.onFocusMove(isShiftDown)) {
                this.onFocusMove(isShiftDown);
            }

            return false;
        } else {
            return super.onKeyPressed(keyCode, scanCode, modifiers);
        }
    }

    public boolean isTransparentBackground() {
        return transparentBackground;
    }

    public boolean shouldCloseOnEsc() {
        return true;
    }

}
