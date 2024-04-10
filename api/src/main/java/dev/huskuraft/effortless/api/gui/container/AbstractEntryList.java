package dev.huskuraft.effortless.api.gui.container;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.api.gui.EntryList;
import dev.huskuraft.effortless.api.gui.Widget;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.RenderLayers;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public abstract class AbstractEntryList<E extends AbstractEntryList.Entry> extends AbstractContainerWidget implements EntryList {

    private final boolean backgroundTransparent = true;
    private final boolean renderShadow = true;
    protected int x0;
    protected int x1;
    protected int y0;
    protected int y1;
    protected boolean scrolling;
    private double scrollAmount;
    private boolean renderSelection = true;

    protected AbstractEntryList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height, Text.empty());
        this.x0 = x;
        this.x1 = x + width;
        this.y0 = y;
        this.y1 = y + height;
    }

    public boolean isBackgroundTransparent() {
        return backgroundTransparent;
    }

    @Nullable
    @Override
    public E getHovered() {
        return (E) super.getHovered();
    }

    public void setHovered(@Nullable E hovered) {
        super.setHovered(hovered);
    }

    @Nullable
    @Override
    public E getSelected() {
        return (E) super.getFocused();
    }

    public void setSelected(@Nullable E entry) {
        super.setFocused(entry);
    }

    @Nullable
    @Override
    public E getFocused() {
        return (E) super.getFocused();
    }

    public void setFocused(@Nullable E entry) {
        super.setFocused(entry);
    }

    @Override
    public E getWidget(int i) {
        return children().get(i);
    }

    public final List<E> children() {
        return (List<E>) super.children();
    }

    @Nullable
    @Override
    public final E getWidgetAt(double mouseX, double mouseY) {
        var mouseRelativeY = MathUtils.floor(mouseY - y0) + (int) getScrollAmount() - 4;
        var index = 0;
        var accumulated = mouseRelativeY;
        for (var child : children()) {
            accumulated -= child.getHeight();
            if (accumulated < 0) {
                var rowHalfWidth = child.getWidth() / 2;
                var rowCenterX = x0 + getWidth() / 2;
                var rowLeft = rowCenterX - rowHalfWidth;
                var rowRight = rowCenterX + rowHalfWidth;
                if (mouseX < this.getScrollbarPosition() && mouseX >= rowLeft && mouseX <= rowRight && index >= 0 && mouseRelativeY >= 0 && index < this.getEntrySize()) {
                    return child;
                } else {
                    return null;
                }
            }
            index++;
        }
        return null;
    }

    protected boolean moveSelection(SelectionDirection selectionDirection, Predicate<E> predicate) {
        int i = selectionDirection == AbstractEntryList.SelectionDirection.UP ? -1 : 1;
        if (!this.children().isEmpty()) {
            int j = this.children().indexOf(this.getSelected());

            while (true) {
                int k = MathUtils.clamp(j + i, 0, this.getEntrySize() - 1);
                if (j == k) {
                    break;
                }

                E entry = this.children().get(k);
                if (predicate.test(entry)) {
                    this.setSelected(entry);
                    this.ensureVisible(entry);
                    return true;
                }

                j = k;
            }
        }

        return false;
    }

    public <C extends E> C addEntry(C entry) {
        entry.onCreate();
        entry.onReload();

        children().add(entry);
        entry.onPositionChange(-1, children().size() - 1);
        return entry;
    }

    public <C extends E> C addEntry(int index, C entry) {
        entry.onCreate();
        entry.onReload();
        children().add(index, entry);
        entry.onPositionChange(-1, index);
        // FIXME: 24/9/23
        for (int i = 0; i < children().size(); i++) {
            if (i >= index) {
                children().get(i).onPositionChange(i - 1, i);
            }
        }
        return entry;
    }

    public <C extends E> boolean removeEntry(C entry) {
        var index = children().indexOf(entry);
        boolean bl = this.children().remove(entry);
        entry.onPositionChange(index, -1);
        if (bl && entry == this.getSelected()) {
            this.setSelected(null);
        }
        for (int i = 0; i < children().size(); i++) {
            if (i >= index) {
                children().get(i).onPositionChange(i + 1, i);
            }
        }
        return bl;
    }

    public final void clearEntries() {
        this.children().clear();
        this.setSelected(null);
        this.setFocused(null);
    }

    public void replaceEntries(Collection<? extends E> collection) {
        this.children().clear();
        this.children().addAll(collection);
        this.setSelected(null);
        this.setFocused(null);
    }

    public void swap(int i, int j) {
        if (i == j) return;
        if (i < 0 || j < 0 || i >= children().size() || j >= children().size()) return;
        var old = children().get(i);
        children().set(i, children().get(j)).onPositionChange(i, j);
        children().set(j, old).onPositionChange(j, i);
    }

    protected int getEntrySize() {
        return this.children().size();
    }

    protected boolean isEntrySelected(int i) {
        return this.getSelected() == this.children().get(i);
    }

    protected int getMaxPosition() {
        var i = 0;
        for (var child : children()) {
            i += child.getHeight();
        }
        return i;
    }

    protected void clickedHeader(int i, int j) {
    }

    protected int getEntryAtPosition(E entry) {
        var i = 0;
        for (E child : children()) {
            if (entry == child) {
                return i;
            }
            i += child.getHeight();
        }
        throw new NoSuchElementException("Entry not found in list");
    }

    protected int getEntryAtPosition(int i) {
        return getEntryAtPosition(getOrThrow(i));
    }

    protected void centerScrollOn(E entry) {
        this.setScrollAmount(getEntryAtPosition(entry) + (double) entry.getHeight() / 2 - (double) (this.y1 - this.y0) / 2);
    }

    protected void ensureVisible(E entry) {
        int h = this.children().indexOf(entry);
        int i = this.getRowTop(h);
        int j = i - this.y0 - 4 - getOrThrow(h).getHeight();
        if (j < 0) {
            this.scroll(j);
        }
        int k = this.y1 - i - getOrThrow(h).getHeight() - getOrThrow(h).getHeight(); // TODO fix
        if (k < 0) {
            this.scroll(-k);
        }
    }

    protected void scroll(int i) {
        this.setScrollAmount(this.getScrollAmount() + (double) i);
    }

    protected double getScrollAmount() {
        return this.scrollAmount;
    }

    public void setScrollAmount(double d) {
        this.scrollAmount = MathUtils.clamp(d, 0.0, this.getMaxScroll());
    }

    protected void setScrollAmountNoClamp(double d) {
        this.scrollAmount = d;
    }

    protected int getMaxScroll() {
        return MathUtils.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
    }

    protected int getScrollBottom() {
        return (int) this.getScrollAmount() - this.getHeight();
    }

    protected void updateScrollingState(double d, double e, int i) {
        this.scrolling = i == 0 && d >= (double) this.getScrollbarPosition() && d < (double) (this.getScrollbarPosition() + 6);
    }

    // TODO: 12/9/23
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 300 / 2 + 20;
    }

    protected void moveSelection(SelectionDirection selectionDirection) {
        this.moveSelection(selectionDirection, (entry) -> {
            return true;
        });
    }

    protected void refreshSelection() {
        E entry = this.getSelected();
        if (entry != null) {
            this.setSelected(entry);
            this.ensureVisible(entry);
        }
    }

    public E getOrThrow(int i) {
        if (i < 0 || i >= children().size()) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + children().size());
        }
        return this.children().get(i);
    }

    public boolean contains(E entry) {
        return this.children().contains(entry);
    }

    protected int getRowLeft(int i) {
        return this.x0 + this.getWidth() / 2 - getOrThrow(i).getWidth() / 2;
    }

    protected int getRowLeft(E entry) {
        return this.x0 + this.getWidth() / 2 - entry.getWidth() / 2;
    }

    protected int getRowRight(int i) {
        return this.x0 + this.getWidth() / 2 + getOrThrow(i).getWidth() / 2;
    }

    protected int getRowRight(E entry) {
        return this.x0 + this.getWidth() / 2 + entry.getWidth() / 2;
    }

    protected int getRowTop(int i) {
        return this.y0 + 4 - (int) this.getScrollAmount() + getEntryAtPosition(i);
    }

    protected int getRowTop(E entry) {
        return this.y0 + 4 - (int) this.getScrollAmount() + getEntryAtPosition(entry);
    }

    protected int getRowBottom(int i) {
        return this.getRowTop(i) + getOrThrow(i).getHeight();
    }

    protected int getRowBottom(E entry) {
        return this.getRowTop(entry) + entry.getHeight();
    }

    public boolean isRenderSelection() {
        return renderSelection;
    }

    public void setRenderSelection(boolean renderSelection) {
        this.renderSelection = renderSelection;
    }

    // renders
    private void renderList(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (isRenderSelection() && getHovered() != null) {
            renderSelection(renderer, getHovered(), -8355712, -16777216);
        }
        for (var index = 0; index < getEntrySize(); ++index) {
            var entry = getWidget(index);
            var left = getRowLeft(index);
            var top = getRowTop(index);
            var bottom = getRowBottom(index);

            entry.moveX(left - entry.getX());
            entry.moveY(top - entry.getY());

            if (bottom >= this.y0 && top <= this.y1) {
                renderer.pushPose();
                if (isRenderSelection() && getSelected() == entry) {
                    var p = isFocused() ? -1 : -8355712;
                    renderSelection(renderer, entry, p, -16777216);
                }
                renderer.popPose();
                entry.render(renderer, mouseX, mouseY, deltaTick);
            }
        }
    }

    protected void renderSelection(Renderer renderer, E entry, int outerColor, int innerColor) {
        renderer.pushPose();
        renderer.translate(getRowLeft(entry), getRowTop(entry), 0.0);
        var width = entry.getWidth();
        var height = entry.getHeight();
        renderer.renderRect(-2, -2, width + 2, height - 2, outerColor);
        renderer.renderRect(-1, -1, width + 1, height - 3, innerColor);
        renderer.popPose();
    }

    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {

        setHovered(isMouseOver(mouseX, mouseY) ? getWidgetAt(mouseX, mouseY) : null);

        var k = this.getScrollbarPosition();
        var l = k + 6;
        if (isBackgroundTransparent() && getEntrance().getClient().isLoaded()) {
            renderer.renderGradientRect(x0, y0, x1, y1, 0xa1101010, 0x8c101010);
        } else {
            renderer.setRsShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
            renderer.renderPanelBackgroundTexture(x0, y0, (float) x1, (float) (y1 + (int) this.getScrollAmount()), x1 - x0, y1 - y0);
            renderer.setRsShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        renderer.pushPose();
        renderer.enableScissor(this.x0, this.y0, this.x1, this.y1);
        this.renderList(renderer, mouseX, mouseY, deltaTick);
        renderer.disableScissor();
        renderer.popPose();

        if (this.renderShadow) {
            renderer.renderGradientRect(RenderLayers.GUI_OVERLAY, this.x0, this.y0, this.x1, this.y0 + 4, -16777216, 0, 0);
            renderer.renderGradientRect(RenderLayers.GUI_OVERLAY, this.x0, this.y1 - 4, this.x1, this.y1, 0, -16777216, 0);
        }

        var renderScrollBar = this.getMaxScroll();
        if (renderScrollBar > 0) {
            int p = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            p = MathUtils.clamp(p, 32, this.y1 - this.y0 - 8);
            int q = (int) this.getScrollAmount() * (this.y1 - this.y0 - p) / renderScrollBar + this.y0;
            if (q < this.y0) {
                q = this.y0;
            }

            renderer.renderRect(k, this.y0, l, this.y1, -16777216);
            renderer.renderRect(k, q, l, q + p, -8355712);
            renderer.renderRect(k, q, l - 1, q + p - 1, -4144960);
        }

    }

    @Override
    public void renderWidgetOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidgetOverlay(renderer, mouseX, mouseY, deltaTick);

        if (getHovered() != null) {
            getHovered().renderOverlay(renderer, mouseX, mouseY, deltaTick);
        }
    }

    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        updateScrollingState(mouseX, mouseY, button);
        return super.onMouseClicked(mouseX, mouseY, button) || this.scrolling;
    }

    public boolean onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (getSelected() != null && isDragging() && button == 0 && contains(getFocused()) && getSelected().onMouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        } else if (button == 0 && this.scrolling) {
            if (mouseY < this.y0) {
                setScrollAmount(0.0);
            } else if (mouseY > this.y1) {
                setScrollAmount(getMaxScroll());
            } else {
                double h = MathUtils.max(1, getMaxScroll());
                int j = this.y1 - this.y0;
                int k = MathUtils.clamp((int) ((float) (j * j) / (float) getMaxPosition()), 32, j - 8);
                double l = MathUtils.max(1.0, h / (j - k));
                setScrollAmount(getScrollAmount() + deltaY * l);
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean onMouseScrolled(double mouseX, double mouseY, double amountX, double amountY) {
        var widget = this.getWidgetAt(mouseX, mouseY);

        var consumed = widget != null && widget.onMouseScrolled(mouseX, mouseY, amountX, amountY);

        if (!consumed) {
            setScrollAmount(getScrollAmount() - amountY * 16);
        }
        return true;
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (getFocused() != null && getFocused().onKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (keyCode == 264) {
            this.moveSelection(SelectionDirection.DOWN);
            return true;
        } else if (keyCode == 265) {
            this.moveSelection(SelectionDirection.UP);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        return getFocused() != null && getFocused().onKeyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onCharTyped(char character, int modifiers) {
        if (getFocused() != null && getFocused().onCharTyped(character, modifiers)) {
            return true;
        }
        return super.onCharTyped(character, modifiers);
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseY >= this.y0 && mouseY <= this.y1 && mouseX >= this.x0 && mouseX <= this.x1;
    }

    @Override
    public void moveDown(Widget widget) {

    }

    @Override
    public void moveUp(Widget widget) {

    }

    @Override
    public void moveDownNoClamp(Widget widget) {

    }

    @Override
    public void moveUpNoClamp(Widget widget) {

    }

    @Override
    public boolean isEditable() {
        return false;
    }

    protected enum SelectionDirection {
        UP,
        DOWN
    }

    public abstract static class Entry extends AbstractContainerWidget implements EntryList.Entry {

        protected Entry(Entrance entrance) {
            super(entrance, 0, 0, 0, 0, Text.empty());
        }

        @Override
        public void onPositionChange(int from, int to) {

        }

        @Override
        public void onSelected() {

        }

        @Override
        public void onDeselected() {

        }

        @Deprecated
        public Text getNarration() {
            return Text.empty();
        }

        @Override
        public void render(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            renderer.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
            super.render(renderer, mouseX, mouseY, deltaTick);
            renderer.disableScissor();
        }
    }
}
