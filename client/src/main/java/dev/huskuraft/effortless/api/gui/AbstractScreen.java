package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.text.Text;

public abstract class AbstractScreen extends AbstractContainerWidget implements Screen {

    protected boolean transparentBackground = true;

    protected AbstractScreen(Entrance entrance, Text title) {
        super(entrance, 0, 0, 0, 0, title);
    }

    public Text getScreenTitle() {
        return getMessage();
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
        if (isTransparentBackground() && getEntrance().getClient().isLoaded()) {
            renderer.renderGradientRect(0, 0, this.getWidth(), this.getHeight(), -1072689136, -804253680);
        } else {
            renderer.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
            renderer.renderPanelBackgroundTexture(0, 0, 0F, 0F, getWidth(), getHeight());
            renderer.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && this.shouldCloseOnEsc()) {
            this.detach();
            return true;
        } else if (keyCode == 258) {
            boolean bl = !getEntrance().getClient().hasShiftDown();
            if (!this.onFocusMove(bl)) {
                this.onFocusMove(bl);
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
