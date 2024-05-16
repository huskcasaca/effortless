package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public abstract class AbstractScreen extends AbstractContainerWidget implements Screen {

    protected boolean transparentBackground = true;

    protected AbstractScreen(Entrance entrance, Text title) {
        super(entrance, 0, 0, 0, 0, title);
    }

    protected AbstractScreen(Entrance entrance) {
        super(entrance, 0, 0, 0, 0, Text.empty());
    }


    public Text getScreenTitle() {
        return getMessage();
    }

    @Override
    public void init(int width, int height) {
        setWidth(width);
        setHeight(height);
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
        getEntrance().getClientManager().pushPanel(this);
        onAttach();
    }

    @Override
    public void detach() {
        getEntrance().getClientManager().popPanel(this);
        onDetach();
    }

    @Override
    public boolean isPauseGame() {
        return false;
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);
    }

    protected void renderBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (isTransparentBackground() && getEntrance().getClient().isLoaded()) {
            renderer.renderGradientRect(0, 0, super.getWidth(), super.getHeight(), -1072689136, -804253680);
        } else {
            renderer.setRsShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
            renderer.renderPanelBackgroundTexture(0, 0, 0F, 0F, getWidth(), getHeight());
            renderer.setRsShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void render(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        onReload();
        setHovered(isMouseOver(mouseX, mouseY));

        if (isVisible()) {
            renderBackground(renderer, mouseX, mouseY, deltaTick);
            renderWidget(renderer, mouseX, mouseY, deltaTick);
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
