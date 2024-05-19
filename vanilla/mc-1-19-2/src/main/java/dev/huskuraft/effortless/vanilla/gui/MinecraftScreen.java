package dev.huskuraft.effortless.vanilla.gui;

import java.util.List;

import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.api.gui.Widget;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.vanilla.core.MinecraftText;

public record MinecraftScreen(
    net.minecraft.client.gui.screens.Screen referenceValue
) implements Screen {

    @Override
    public Text getScreenTitle() {
        return MinecraftText.ofNullable(referenceValue().getTitle());
    }

    @Override
    public void init(int width, int height) {

    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isPauseGame() {
        return referenceValue().isPauseScreen();
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {

    }

    @Override
    public List<? extends Widget> children() {
        return null;
    }

    @Override
    public Widget getFocused() {
        return null;
    }

    @Override
    public Widget getSelected() {
        return null;
    }

    @Override
    public Widget getHovered() {
        return null;
    }

    @Override
    public Widget getWidget(int i) {
        return null;
    }

    @Override
    public Widget getWidgetAt(double mouseX, double mouseY) {
        return null;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public boolean onMouseMoved(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    @Override
    public boolean onMouseScrolled(double mouseX, double mouseY, double amountX, double amountY) {
        return false;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        return false;
    }

    @Override
    public boolean onFocusMove(boolean forwards) {
        return false;
    }

    @Override
    public void render(Renderer renderer, int mouseX, int mouseY, float deltaTick) {

    }

    @Override
    public void renderOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick) {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onAnimateTick(float partialTick) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onReload() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public boolean isHovered() {
        return false;
    }

    @Override
    public void setHovered(boolean hovered) {

    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public void setX(int x) {

    }

    @Override
    public void moveX(int x) {

    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void setY(int y) {

    }

    @Override
    public void moveY(int x) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public void setWidth(int width) {

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setHeight(int height) {

    }

    @Override
    public Widget getParent() {
        return null;
    }
}
