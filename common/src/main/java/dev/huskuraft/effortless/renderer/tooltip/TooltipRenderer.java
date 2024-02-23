package dev.huskuraft.effortless.renderer.tooltip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dev.huskuraft.effortless.api.core.AxisDirection;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;

public class TooltipRenderer {

    private final Entrance entrance;
    private final Map<Integer, Map<Object, Entry>> prioritiedMap = new HashMap<>();

    public TooltipRenderer(Entrance entrance) {
        this.entrance = entrance;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public void showMessages(Object id, Collection<Text> texts, int priority) {
        showEntry(id, new ComponentsEntry(texts), priority);
    }

    public void showTitledItems(Object id, Text title, Collection<ItemStack> groups, int priority) {
        showEntry(id, new TitledItemsEntry(title, groups), priority);
    }

    public void showItems(Object id, Collection<ItemStack> items, int priority) {
        showEntry(id, new ItemsEntry(items), priority);
    }

    private void showEntry(Object id, Entry entry, int priority) {
        prioritiedMap.compute(priority, (k, v) -> {
            if (v == null) {
                v = new LinkedHashMap<>();
            }
            v.put(id, entry);
            return v;
        });
    }

    public void tick() {
        var iterator = prioritiedMap.values().iterator();
        while (iterator.hasNext()) {
            var map = iterator.next();
            var iterator1 = map.values().iterator();
            while (iterator1.hasNext()) {
                var entry = iterator1.next();
                entry.tick();
                if (!entry.isAlive()) {
                    iterator1.remove();
                }
            }
            if (map.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public void renderGuiOverlay(Renderer renderer, float deltaTick) {
        var contentSide = AxisDirection.NEGATIVE;
        renderer.pushPose();
        var x = 0f;
        var y = renderer.window().getGuiScaledHeight() * 1f;
        renderer.translate(0, 0, -200);
        renderer.translate(x, y, 0);
        renderer.translate(-1f * contentSide.getStep(), 0, 0);
        renderer.translate(0, -8, 0);
        y -= 8;

        for (var map : prioritiedMap.values()) {
            var iterator = new LinkedList<>(map.values()).descendingIterator();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                if (!entry.isVisible()) {
                    continue;
                }
                entry.setContentSide(contentSide);
                renderer.pushPose();
                renderer.renderRect(entry.getWidth(), 0, 0, -entry.getHeight(), renderer.optionColor(0.8f * entry.getAlpha()));
                renderer.translate(entry.getPaddingX(), -entry.getPaddingY(), 0);
                entry.render(renderer, 0, 0, deltaTick);
                renderer.popPose();
                renderer.translate(0, -entry.getHeight() - 1, 0);
                y -= entry.getHeight() + 1;
            }
        }

        renderer.popPose();
    }

    // FIXME: 24/9/23
    private abstract class Entry extends AbstractWidget {

        private static final int FADE_TICKS = 10;

        private int ticksTillRemoval = 20;
        private AxisDirection contentSide = AxisDirection.NEGATIVE;

        protected Entry() {
            super(entrance, Text.empty());
        }

        public abstract int getContentWidth();

        public abstract int getContentHeight();

        public abstract int getPaddingX();

        public abstract int getPaddingY();

        public int getWidth() {
            return getContentWidth() + getPaddingX() * 2;
        }

        public int getHeight() {
            return getContentHeight() + getPaddingY() * 2;
        }

        public void tick() {
            ticksTillRemoval--;
        }

        public boolean isAlive() {
            return ticksTillRemoval >= -FADE_TICKS;
        }

        public boolean isFading() {
            return ticksTillRemoval < 0;
        }

        public float getAlpha() {
            return isFading() ? (float) (ticksTillRemoval + FADE_TICKS) / FADE_TICKS : 1;
        }

        public AxisDirection getContentSide() {
            return contentSide;
        }

        public void setContentSide(AxisDirection contentSide) {
            this.contentSide = contentSide;
        }
    }

    private class ComponentsEntry extends Entry {

        private final List<Text> texts;

        public ComponentsEntry(Collection<Text> texts) {
            var reverse = new ArrayList<>(texts);
            Collections.reverse(reverse);
            this.texts = reverse;
        }

        @Override
        public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            super.renderWidget(renderer, mouseX, mouseY, deltaTick);


            renderer.translate(0, 1, 0);
            var textY = 0;
            for (var text : texts) {
                textY -= 10;
                var positionX = getContentSide() == AxisDirection.POSITIVE ? getContentWidth() - getTypeface().measureWidth(text) : 0;
                renderer.renderTextFromStart(getTypeface(), text, positionX, textY, 0xffffffff, true);
            }
        }

        @Override
        public int getContentWidth() {
            return texts.stream().mapToInt(getTypeface()::measureWidth).max().orElse(0);
        }

        @Override
        public int getContentHeight() {
            return texts.size() * 10;
        }

        @Override
        public int getPaddingX() {
            return 10;
        }

        @Override
        public int getPaddingY() {
            return 2;
        }

        @Override
        public boolean isVisible() {
            return !texts.isEmpty();
        }
    }

    private class TitledItemsEntry extends ItemsEntry {
        private final Text header;

        public TitledItemsEntry(Text header, Collection<ItemStack> items) {
            super(items);
            this.header = header;
        }

        @Override
        public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            super.renderWidget(renderer, mouseX, mouseY, deltaTick);

            var positionX = getContentSide() == AxisDirection.POSITIVE ? getContentWidth() - getTypeface().measureWidth(header) : 0;
            renderer.renderTextFromStart(getTypeface(), header, positionX, -getContentHeight() + 2, 0xffffffff, true);
        }

        @Override
        public int getContentHeight() {
            return super.getContentHeight() + 10;
        }

        @Override
        public int getContentWidth() {
            return MathUtils.max(super.getContentWidth(), getTypeface().measureWidth(header));
        }
    }

    private class ItemsEntry extends Entry {

        private static final int MAX_COLUMN = 9;
        private static final int ITEM_SPACING_X = 18;
        private static final int ITEM_SPACING_Y = 18;
        private final Collection<ItemStack> itemStacks;

        public ItemsEntry(Collection<ItemStack> itemStacks) {
            this.itemStacks = itemStacks;
        }


        @Override
        public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            super.renderWidget(renderer, mouseX, mouseY, deltaTick);

            var i = 0;
            var j = 0;

            for (var itemStack : itemStacks) {
                var x = i * ITEM_SPACING_X;
                var y = j * ITEM_SPACING_Y - ITEM_SPACING_Y * (int) MathUtils.ceil(1f * itemStacks.size() / MAX_COLUMN);

                var text = Text.text(String.valueOf(itemStack.getStackSize()));
                var color = ItemStackUtils.getColorTag(itemStack);

                renderer.renderItem(itemStack, x + 1, y + 1);
                renderer.pushPose();
                renderer.translate(0, 0, 200F);
                renderer.renderText(getTypeface(), text, x + 19 - 2 - getTypeface().measureWidth(text), y + 6 + 3, color == 0 ? 16777215 : color, true);
                renderer.popPose();

                if (i < MAX_COLUMN - 1) {
                    i += 1;
                } else {
                    i = 0;
                    j += 1;
                }
            }
        }

        @Override
        public int getContentWidth() {
            return MathUtils.min(MAX_COLUMN, itemStacks.size()) * ITEM_SPACING_X - 4;
        }

        @Override
        public int getContentHeight() {
            return (int) MathUtils.ceil(1f * itemStacks.size() / MAX_COLUMN) * ITEM_SPACING_Y + 2;
        }

        @Override
        public int getPaddingX() {
            return 10;
        }

        @Override
        public int getPaddingY() {
            return 2;
        }

        @Override
        public boolean isVisible() {
            return !itemStacks.isEmpty();
        }
    }

}
