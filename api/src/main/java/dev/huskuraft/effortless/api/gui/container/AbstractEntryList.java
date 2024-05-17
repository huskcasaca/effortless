package dev.huskuraft.effortless.api.gui.container;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.EntryList;
import dev.huskuraft.effortless.api.gui.Widget;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.RenderLayers;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public abstract class AbstractEntryList<E extends AbstractEntryList.Entry> extends AbstractContainerWidget implements EntryList {

    private static final int DEFAULT_VERTICAL_PADDING = 4;
    public static final int DOUBLE_CLICK_THRESHOLD = 250;

    protected boolean backgroundTransparent = false;
    protected boolean renderShadow = true;
    protected boolean renderSelection = true;
    protected boolean isAlwaysShowScrollbar = false;
    protected boolean isShowScrollBar = true;

    protected boolean scrolling;

    protected int x0;
    protected int x1;
    protected int y0;
    protected int y1;
    private double scrollAmount;
    private long lastClickTime = 0;

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

    public void setBackgroundTransparent(boolean backgroundTransparent) {
        this.backgroundTransparent = backgroundTransparent;
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
        entry.setParent(this);
        entry.onCreate();
        entry.onReload();

        children().add(entry);
        entry.onPositionChange(-1, children().size() - 1);
        return entry;
    }

    public <C extends E> C addEntry(int index, C entry) {
        entry.setParent(this);
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
        entry.setParent(null);
        var index = children().indexOf(entry);
        var removed = this.children().remove(entry);
        entry.onPositionChange(index, -1);
        if (removed && entry == this.getSelected()) {
            this.setSelected(null);
        }
        for (int i = 0; i < children().size(); i++) {
            if (i >= index) {
                children().get(i).onPositionChange(i + 1, i);
            }
        }
        return removed;
    }

    public final void clearEntries() {
        children().forEach(widget -> widget.setParent(null));
        children().clear();
        setSelected(null);
        setFocused(null);
    }

    public void replaceEntries(Collection<? extends E> collection) {
        children().forEach(widget -> widget.setParent(null));
        children().clear();
        collection.forEach(widget -> widget.setParent(this));
        children().addAll(collection);
        setSelected(null);
        setFocused(null);
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
        return children().stream().mapToInt(Widget::getHeight).sum();
    }

    protected void clickedHeader(int i, int j) {
    }

    protected int getEntryPosition(E entry) {
        var i = 0;
        for (E child : children()) {
            if (entry == child) {
                return i;
            }
            i += child.getHeight();
        }
        throw new NoSuchElementException("Entry not found in list");
    }

    protected int getEntryPosition(int i) {
        return getEntryPosition(getOrThrow(i));
    }

    protected void centerScrollOn(E entry) {
        this.setScrollAmount(getEntryPosition(entry) + (double) entry.getHeight() / 2 - (double) (this.y1 - this.y0) / 2);
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

    private int firstVisibleIndex = 0;
    private double firstVisibleOffset = 0d;

    public void setScrollAmount(double d) {
        var scrollAmountClamped = MathUtils.clamp(d, 0.0, this.getMaxScroll());
        scrollBy(scrollAmountClamped - scrollAmount);
        this.scrollAmount = scrollAmountClamped;
    }

    private void scrollBy(double scrolledAmount) {
        this.firstVisibleOffset -= scrolledAmount;
        if (children().isEmpty()) {
            this.firstVisibleIndex = 0;
            this.firstVisibleOffset = 0d;
        }
        if (children().size() <= firstVisibleIndex) {
            this.firstVisibleIndex = 0;
            this.firstVisibleOffset = 0d;
            return;
        }
        var height = children().get(firstVisibleIndex).getHeight();

        if (height + this.firstVisibleOffset < 0) {
            for (var index = firstVisibleIndex; index < children().size(); index++) {
                var height1 = children().get(index).getHeight();
                if (this.firstVisibleOffset + height1 >= 0) {
                    break;
                }
                this.firstVisibleIndex += 1;
                this.firstVisibleOffset += height1;
                if (this.firstVisibleOffset >= 0) {
                    break;
                }
            }
            return;
        }

        if (this.firstVisibleOffset > 0) {
            for (var index = firstVisibleIndex - 1; index >= 0; index--) {
                var height1 = children().get(index).getHeight();
                this.firstVisibleIndex -= 1;
                this.firstVisibleOffset -= height1;
                if (this.firstVisibleOffset <= 0) {
                    break;
                }
            }
            return;
        }
    }

    protected void setScrollAmountNoClamp(double d) {
        scrollBy(d - scrollAmount);
        this.scrollAmount = d;
    }

    protected int getMaxScroll() {
        return MathUtils.max(0, this.getMaxPosition() - (this.y1 - this.y0 - 4));
    }

    protected int getScrollBottom() {
        return (int) this.getScrollAmount() - this.getHeight();
    }

    protected void updateScrollingState(double d, double e, int i) {
        this.scrolling = i == 0 && d >= (double) this.getScrollbarPosition() && d < (double) (this.x0 + this.getScrollbarPosition() + 6);
    }

    // TODO: 12/9/23
    protected int getScrollbarPosition() {
        return this.x0 + this.getWidth() + 2;
    }

    protected int getScrollbarWidth() {
        return 6;
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
        var relativeIndex = firstVisibleIndex;
        return (int) (this.y0 + 4 + IntStream.range(Math.min(i, relativeIndex), Math.max(i, relativeIndex)).mapToObj(children()::get).mapToInt(AbstractWidget::getHeight).sum() + firstVisibleOffset);
    }

    protected int getRowTop(E entry) {
        return getRowTop(children().indexOf(entry));
//        return this.y0 + 4 - (int) this.getScrollAmount() + getEntryPosition(entry);
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
            renderSelection(renderer, getHovered(), 0xff808080, 0xff000000);
        }
        for (var i = 0; i < getEntrySize(); ++i) {
            var entry = getWidget(i);
            if (i < firstVisibleIndex) {
                entry.setVisible(false);
                continue;
            }
            var left = getRowLeft(i);
            var top = getRowTop(i);
            var bottom = getRowBottom(i);

            entry.moveX(left - entry.getX());
            entry.moveY(top - entry.getY());

            if (bottom < this.y0) {
                entry.setVisible(false);
                continue;
            }

            if (top > this.y1) {
                for (int j = i; j < getEntrySize(); j++) {
                    getWidget(j).setVisible(false);
                }
                break;
            }
            entry.setVisible(true);
            renderer.pushPose();
            if (isRenderSelection() && getSelected() == entry) {
                renderSelection(renderer, entry, isFocused() ? 0xffffffff : 0xff808080, 0xff000000);
            }
            renderer.popPose();
            entry.render(renderer, mouseX, mouseY, deltaTick);
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

    public void renderWidgetBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        if (getEntrance().getClient().isLoaded() && !isBackgroundTransparent()) {
            renderer.renderGradientRect(x0, y0, x1, y1, 0xdc000000, 0xdc000000);
//            renderer.renderGradientRect(x0, y0, x1, y1, 0xa1101010, 0x8c101010);
        } else {
//            renderer.setRsShaderColor(0.125F, 0.125F, 0.125F, 1.0F);
//            renderer.renderPanelBackgroundTexture(x0, y0, (float) x1, (float) (y1 + (int) this.getScrollAmount()), x1 - x0, y1 - y0);
//            renderer.setRsShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {

        setHovered(isMouseOver(mouseX, mouseY) ? getWidgetAt(mouseX, mouseY) : null);

        var left = getScrollbarPosition();
        var width = getScrollbarWidth();
        renderer.pushPose();
        renderer.pushScissor(this.x0, this.y0, getWidth(), getHeight());
        renderList(renderer, mouseX, mouseY, deltaTick);
        renderer.popScissor();
        renderer.popPose();

        if (this.renderShadow) {
            renderer.renderGradientRect(RenderLayers.GUI_OVERLAY, this.x0, this.y0, this.x1, this.y0 + 4, 0xff000000, 0x00000000, 0);
            renderer.renderGradientRect(RenderLayers.GUI_OVERLAY, this.x0, this.y1 - 4, this.x1, this.y1, 0x00000000, 0xff000000, 0);
        }

        var renderScrollBar = this.getMaxScroll();
        if (isShowScrollBar && (renderScrollBar > 0 || isAlwaysShowScrollbar)) {
            var size = (int) ((float) ((this.y1 - this.y0) * (this.y1 - this.y0)) / (float) this.getMaxPosition());
            size = MathUtils.clamp(size, 32, this.y1 - this.y0);
            var top = renderScrollBar == 0 ? 0 : ((int) this.getScrollAmount() * (this.y1 - this.y0 - size) / renderScrollBar) + this.y0;
            if (top < this.y0) {
                top = this.y0;
            }

            renderer.renderRect(left, this.y0, left + width, this.y1, 0xff000000);
            renderer.renderRect(left, top, left + width, top + size, 0xff808080);
            renderer.renderRect(left, top, left + width - 1, top + size - 1, 0xffc0c0c0);
        }
    }

    public boolean isScrollbarVisible() {
        var renderScrollBar = this.getMaxScroll();
        return isShowScrollBar && (renderScrollBar > 0 || isAlwaysShowScrollbar);
    }

    public void setAlwaysShowScrollbar(boolean alwaysShowScrollbar) {
        this.isAlwaysShowScrollbar = alwaysShowScrollbar;
    }

    public void setShowScrollBar(boolean showScrollBar) {
        this.isShowScrollBar = showScrollBar;
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
        var result = super.onMouseClicked(mouseX, mouseY, button) || this.scrolling;
        if (result && getFocused() != null && getFocused().isMouseOver(mouseX, mouseY)) {
            if (System.currentTimeMillis() - lastClickTime > DOUBLE_CLICK_THRESHOLD) {
                this.lastClickTime = System.currentTimeMillis();
            } else {
                this.lastClickTime = System.currentTimeMillis() - lastClickTime;
            }
        }
        return result;
    }

    public boolean consumeDoubleClick() {
        var clicked = lastClickTime > 0 && lastClickTime <= DOUBLE_CLICK_THRESHOLD;
        if (clicked) {
            lastClickTime = 0;
        }
        return clicked;
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
        return mouseY >= this.getTop() && mouseY <= this.getBottom() && ((mouseX >= this.getLeft() && mouseX <= this.getRight()) || (mouseX >= this.getScrollbarPosition() && mouseX <= this.getScrollbarPosition() + this.getScrollbarWidth()));
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
            renderer.pushScissor(getX(), getY(), getWidth(), getHeight());
            super.render(renderer, mouseX, mouseY, deltaTick);
            renderer.popScissor();
        }

        @Override
        public int getWidth() {
            return MathUtils.min(Dimens.Entry.ROW_WIDTH, getParent().getWidth() - DEFAULT_VERTICAL_PADDING * 2);
        }
    }
}
