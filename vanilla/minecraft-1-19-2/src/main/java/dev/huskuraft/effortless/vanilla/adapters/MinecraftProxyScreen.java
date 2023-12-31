package dev.huskuraft.effortless.vanilla.adapters;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.huskuraft.effortless.gui.Screen;
import net.minecraft.network.chat.Component;

public class MinecraftProxyScreen extends net.minecraft.client.gui.screens.Screen {

    private final Screen proxy;

    MinecraftProxyScreen(Screen screen) {
        super(Component.empty());
        this.proxy = screen;
    }

    public Screen getProxy() {
        return proxy;
    }

    @Override
    public void tick() {
        proxy.tick();
    }

    @Override
    protected void init() {
        proxy.setWidth(width);
        proxy.setHeight(height);
        proxy.onDestroy();
        proxy.onCreate();
        proxy.onLoad();
    }

    @Override
    protected void rebuildWidgets() {
        proxy.setWidth(width);
        proxy.setHeight(height);
        proxy.onDestroy();
        proxy.onCreate();
        proxy.onLoad();
    }

    @Override
    public void removed() {
        proxy.onDestroy();
    }

    @Override
    public void onClose() {
    }

    @Override
    public void render(PoseStack minecraftMatrixStack, int i, int j, float f) {
        proxy.onReload();
        proxy.render(MinecraftRenderer.fromMinecraft(minecraftMatrixStack), i, j, f);
        proxy.renderOverlay(MinecraftRenderer.fromMinecraft(minecraftMatrixStack), i, j, f);
    }

    @Override
    public boolean isPauseScreen() {
        return proxy.isPauseGame();
    }

    @Override
    public void mouseMoved(double d, double e) {
        proxy.onMouseMoved(d, e);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return proxy.onMouseClicked(d, e, i);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        return proxy.onMouseReleased(d, e, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        return proxy.onMouseDragged(d, e, i, f, g);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        return proxy.onMouseScrolled(d, e, f, 0);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return proxy.onKeyPressed(i, j, k);
    }

    @Override
    public boolean keyReleased(int i, int j, int k) {
        return proxy.onKeyReleased(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return proxy.onCharTyped(c, i);
    }

    //    @Override
    @Deprecated
    public boolean changeFocus(boolean bl) {
        return proxy.onFocusMove(bl);
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return proxy.isMouseOver(d, e);
    }

    @Override
    public Component getTitle() {
        return MinecraftText.toMinecraftText(proxy.getScreenTitle());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftProxyScreen keyBinding && proxy.equals(keyBinding.proxy);
    }

    @Override
    public int hashCode() {
        return proxy.hashCode();
    }
}
