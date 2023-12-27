package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.gui.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MinecraftProxyScreen extends net.minecraft.client.gui.screens.Screen {

    private final Screen screen;

    MinecraftProxyScreen(Screen screen) {
        super(Component.empty());
        this.screen = screen;
    }

    public Screen getProxy() {
        return screen;
    }

    @Override
    public void tick() {
        getProxy().tick();
    }

    @Override
    protected void init() {
        getProxy().setWidth(width);
        getProxy().setHeight(height);
        getProxy().onDestroy();
        getProxy().onCreate();
        getProxy().onLoad();
    }

    @Override
    protected void rebuildWidgets() {
        getProxy().setWidth(width);
        getProxy().setHeight(height);
        getProxy().onDestroy();
        getProxy().onCreate();
        getProxy().onLoad();
    }

    @Override
    public void removed() {
        getProxy().onDestroy();
    }

    @Override
    public void onClose() {
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        getProxy().onReload();
        getProxy().render(new MinecraftRenderer(guiGraphics), i, j, f);
        getProxy().renderOverlay(new MinecraftRenderer(guiGraphics), i, j, f);
    }

    @Override
    public boolean isPauseScreen() {
        return getProxy().isPauseGame();
    }

    @Override
    public void mouseMoved(double d, double e) {
        getProxy().onMouseMoved(d, e);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return getProxy().onMouseClicked(d, e, i);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        return getProxy().onMouseReleased(d, e, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        return getProxy().onMouseDragged(d, e, i, f, g);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f, double g) {
        return getProxy().onMouseScrolled(d, e, f, g);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return getProxy().onKeyPressed(i, j, k);
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        return getProxy().onKeyReleased(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return getProxy().onCharTyped(c, i);
    }

    //    @Override
    @Deprecated
    public boolean changeFocus(boolean bl) {
        return getProxy().onFocusMove(bl);
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return getProxy().isMouseOver(d, e);
    }

    @Override
    public Component getTitle() {
        return MinecraftText.toMinecraftText(getProxy().getScreenTitle());
    }
}
