package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.core.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public abstract class AbstractContainerWidget extends AbstractWidget implements ContainerWidget {

    private final List<AbstractWidget> children = new ArrayList<>();
    private boolean isDragging;
    @Nullable
    private AbstractWidget focused;
    @Nullable
    private AbstractWidget hovered;

    protected AbstractContainerWidget(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
    }

    @Override
    public void tick() {
        for (var child : children()) {
            child.tick();
        }
    }

    public final boolean isDragging() {
        return this.isDragging;
    }

    public final void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        for (var child : this.children()) {
            child.render(renderer, mouseX, mouseY, deltaTick);
        }
    }

    @Override
    public void renderWidgetOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidgetOverlay(renderer, mouseX, mouseY, deltaTick);

        for (var child : this.children()) {
            child.renderOverlay(renderer, mouseX, mouseY, deltaTick);
        }
    }

    @Override
    public List<? extends AbstractWidget> children() {
        return children;
    }

    @Override
    public AbstractWidget getWidget(int i) {
        return children().get(i);
    }

    public <T extends AbstractWidget> T addWidget(T widget) {
        children.add(widget);
        return widget;
    }

    public void removeWidget(AbstractWidget widget) {
        children().remove(widget);
    }

    public void clearWidgets() {
        children().clear();
    }

    @Override
    public AbstractWidget getSelected() {
        return this.focused;
    }

    public void setSelected(@Nullable AbstractWidget widget) {
        this.focused = widget;
    }

    @Nullable
    public AbstractWidget getFocused() {
        return this.focused;
    }

    protected void setFocused(@Nullable AbstractWidget widget) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }
        this.focused = widget;
        if (this.focused != null) {
            this.focused.setFocused(true);
        }
    }

    @Override
    public AbstractWidget getHovered() {
        return this.hovered;
    }

    protected void setHovered(@Nullable AbstractWidget widget) {
        this.hovered = widget;
    }

    @Override
    public AbstractWidget getWidgetAt(double mouseX, double mouseY) {
        var var5 = this.children().iterator();
        AbstractWidget target;
        do {
            if (!var5.hasNext()) {
                return null;
            }

            target = var5.next();
        } while (!target.isMouseOver(mouseX, mouseY));

        return target;
    }

    @Override
    public void moveX(int x) {
        super.moveX(x);
        for (var child : children()) {
            child.moveX(x);
        }
    }

    @Override
    public void moveY(int y) {
        super.moveY(y);
        for (var child : children()) {
            child.moveY(y);
        }
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        var mouseOver = super.onMouseClicked(mouseX, mouseY, button);
        var target = (AbstractWidget) null;
        var list = List.copyOf(this.children());
        for (var listener : list) {
            if (listener.onMouseClicked(mouseX, mouseY, button) && listener.isMouseOver(mouseX, mouseY)) {
                target = listener;
            }
        }
        if (target != null && mouseOver) {
            this.setFocused(target);
            if (button == 0) {
                this.setDragging(true);
            }
            return true;
        } else {
            return mouseOver;
        }
    }

    @Override
    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        this.setDragging(false);
        var widget = getWidgetAt(mouseX, mouseY);
        return widget != null && widget.onMouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.getFocused() != null && this.isDragging() && button == 0 && this.getFocused().onMouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean onMouseScrolled(double mouseX, double mouseY, double amountX, double amountY) {
        var widget = getWidgetAt(mouseX, mouseY);
        return widget != null && widget.onMouseScrolled(mouseX, mouseY, amountX, amountY);
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        return this.getFocused() != null && this.getFocused().onKeyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        return this.getFocused() != null && this.getFocused().onCharTyped(character, modifiers);
    }

    @Override
    public boolean onFocusMove(boolean forwards) {
        var focused = this.getFocused();
        var hasFocus = focused != null;
        if (!hasFocus || !focused.onFocusMove(forwards)) {
            var list = this.children();
            int i = list.indexOf(focused);
            int j;
            if (hasFocus && i >= 0) {
                j = i + (forwards ? 1 : 0);
            } else if (forwards) {
                j = 0;
            } else {
                j = list.size();
            }

            var listIterator = list.listIterator(j);
            var hasValueSupplier = (BooleanSupplier) null;
            if (forwards) {
                hasValueSupplier = listIterator::hasNext;
            } else {
                hasValueSupplier = listIterator::hasPrevious;
            }

            var valueSupplier = (Supplier) null;
            if (forwards) {
                Objects.requireNonNull(listIterator);
                valueSupplier = listIterator::next;
            } else {
                Objects.requireNonNull(listIterator);
                valueSupplier = listIterator::previous;
            }

            var listener = (AbstractWidget) null;
            do {
                if (!hasValueSupplier.getAsBoolean()) {
                    this.setFocused(null);
                    return false;
                }

                listener = (AbstractWidget) valueSupplier.get();
            } while (!listener.onFocusMove(forwards));

            this.setFocused(listener);
        }
        return true;
    }

}
