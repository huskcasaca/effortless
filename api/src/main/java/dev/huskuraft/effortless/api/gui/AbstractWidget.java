package dev.huskuraft.effortless.api.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public abstract class AbstractWidget implements Widget {

    private final ClientEntrance entrance;
    private boolean visible = true;
    private boolean active = true;
    private boolean hovered = false;
    private boolean focused = false;
    private float alpha = 1f;
    private int x;
    private int y;
    private int width;
    private int height;
    private Text message;
    private List<Text> tooltip;

    protected AbstractWidget(Entrance entrance, int x, int y, int width, int height, Text message) {
        this.entrance = (ClientEntrance) entrance;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
        this.tooltip = new ArrayList<>();
    }

    protected AbstractWidget(Entrance entrance, Text message) {
        this.entrance = (ClientEntrance) entrance;
        this.message = message;
        this.tooltip = new ArrayList<>();
    }

    protected ClientEntrance getEntrance() {
        return entrance;
    }

    @Override
    public void tick() {

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
    public void render(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        setHovered(isMouseOver(mouseX, mouseY));

        if (isVisible()) {
            renderWidget(renderer, mouseX, mouseY, deltaTick);
        }
    }

    @Override
    public void renderOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (isVisible()) {
            renderWidgetOverlay(renderer, mouseX, mouseY, deltaTick);
        }
    }

    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
    }

    public void renderWidgetOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (isHovered()) {
            renderer.renderTooltip(getTypeface(), getTooltip(), mouseX, mouseY);
        }
    }

    public Typeface getTypeface() {
        return getEntrance().getClient().getTypeface();
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isHovered() {
        return hovered;
    }

    @Override
    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void moveX(int x) {
        this.x += x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void moveY(int y) {
        this.y += y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text text) {
        this.message = text;
    }

    public void setMessage(String text) {
        this.message = Text.text(text);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.isActive() && this.isVisible() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < (this.getX() + this.getWidth()) && mouseY < (this.getY() + this.getHeight());
    }

    @Override
    public boolean onMouseMoved(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        var mouseOver = isMouseOver(mouseX, mouseY);
        this.focused = mouseOver;
        return mouseOver;
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

    public boolean isHoveredOrFocused() {
        return this.isHovered() || this.isFocused();
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public List<Text> getTooltip() {
        return tooltip;
    }

    public void setTooltip(Text tooltip) {
        this.tooltip = Collections.singletonList(tooltip);
    }

    public void setTooltip(List<Text> tooltip) {
        this.tooltip = tooltip;
    }

    public void setTooltip(Text... tooltip) {
        this.tooltip = List.of(tooltip);
    }

    public void setTooltip(String... tooltip) {
        this.tooltip = Stream.of(tooltip).map(Text::text).toList();
    }
}
