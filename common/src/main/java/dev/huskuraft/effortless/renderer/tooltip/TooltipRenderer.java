package dev.huskuraft.effortless.renderer.tooltip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.AxisDirection;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.lang.Tuple2;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;

public class TooltipRenderer {

    private final Entrance entrance;

    private final Map<Integer, Map<Object, Entry>> prioritiedMap = new TreeMap<>(Comparator.naturalOrder());

    public TooltipRenderer(Entrance entrance) {
        this.entrance = entrance;
    }

    public EffortlessClient getEntrance() {
        return (EffortlessClient) entrance;
    }

    public void showMessages(Object id, int priority, List<Text> texts) {
        showEntry(id, priority, new ComponentsEntry(texts));
    }

    public void showMessagePairs(Object id, int priority, List<Tuple2<Text, Text>> texts) {
        showEntry(id, priority, new ComponentPairEntry(texts));
    }

    public void showTitledItems(Object id, int priority, Text title, List<ItemStack> groups) {
        showEntry(id, priority, new TitledItemsEntry(title, groups));
    }

    public void showItems(Object id, int priority, Collection<ItemStack> items) {
        showEntry(id, priority, new ItemsEntry(items));
    }

    @SuppressWarnings("unchecked")
    public void showEmptyEntry(Object id, int priority) {
        showEntry(id, priority, new EmptyEntry());
    }

    public void showGroupEntry(Object id, int priority, List<Object> entry) {
        var entries = entry.stream().map(object -> {
            if (object instanceof List<?> list && !list.isEmpty()) {
                if (list.get(0) instanceof Text text) {
                    return new ComponentsEntry(Collections.singletonList(text));
                } else if (list.get(0) instanceof Tuple2<?, ?> pair) {
                    if (pair.value1() instanceof Text && pair.value2() instanceof Text) {
                        return new ComponentPairEntry((Collection<Tuple2<Text, Text>>) list);
                    }
                } else if (list.get(0) instanceof ItemStack) {
                    return new ItemsEntry((Collection<ItemStack>) list);
                }
            } else if (object instanceof Text text) {
                return new ComponentsEntry(Collections.singletonList(text));
            } else if (object instanceof ResourceLocation icon) {
                return new IconEntry(icon);
            }
            return null;
        }).filter(Objects::nonNull).toList();
        showEntry(id, priority, new GroupEntry(entries));
    }

    private void showEntry(Object id, int priority, Entry entry) {
        synchronized (prioritiedMap) {
            prioritiedMap.compute(priority, (k, v) -> {
                if (v == null) {
                    v = new LinkedHashMap<>();
                }
                v.put(id, entry);
                return v;
            });
        }
    }


    public void tick() {

        synchronized (prioritiedMap) {
            var iterator = prioritiedMap.values().iterator();
            while (iterator.hasNext()) {
                var map = iterator.next();
                for (var iterator1 = map.values().iterator(); iterator1.hasNext(); ) {
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
    }

    public void renderGuiOverlay(Renderer renderer, float deltaTick) {
        var contentSide = AxisDirection.POSITIVE;
        var contentGravity = AxisDirection.NEGATIVE;
        renderer.pushPose();
        renderer.translate(0, 0, -200);
        renderer.translate(0f, renderer.window().getGuiScaledHeight() * 1f, 0);
        renderer.translate(-1f * contentSide.getStep(), 0, 0);
        renderer.translate(0, -8, 0);
        synchronized (prioritiedMap) {
            for (var map : prioritiedMap.values()) {
                for (var iterator = new LinkedList<>(map.values()).descendingIterator(); iterator.hasNext(); ) {
                    var entry = iterator.next();
                    if (!entry.isVisible()) {
                        continue;
                    }
                    entry.setContentSide(contentGravity);
                    renderer.pushPose();
                    if (contentSide == AxisDirection.POSITIVE) {
                        renderer.translate(renderer.window().getGuiScaledWidth() - entry.getWidth(), 0, 0);
                    }
                    renderer.renderRect(0, 0, entry.getWidth(), -entry.getHeight() - 8, renderer.optionColor(0.8f * entry.getAlpha()));
                    renderer.translate(0, -4, 0);
                    renderer.pushPose();
                    renderer.translate(entry.getPaddingX(), -entry.getPaddingY(), 0);
                    entry.render(renderer, 0, 0, deltaTick);
                    renderer.popPose();
                    renderer.popPose();
                    renderer.translate(0, -entry.getHeight(), 0);
                    renderer.translate(0, -10, 0);
                }
            }
        }
        renderer.popPose();
    }

    private class ComponentPairEntry extends Entry {

        private final List<Tuple2<Text, Text>> textPairs;

        public ComponentPairEntry(Collection<Tuple2<Text, Text>> textPairs) {
            var reverse = new ArrayList<>(textPairs);
            Collections.reverse(reverse);
            this.textPairs = reverse;
        }

        @Override
        public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            super.renderWidget(renderer, mouseX, mouseY, deltaTick);

            renderer.translate(0, 1, 0);
            var top = 0;
            for (var textPair : textPairs) {
                top -= 10;
                renderer.renderTextFromStart(getTypeface(), textPair.value1(), 0, top, 0xffffffff, true);
                renderer.renderTextFromStart(getTypeface(), textPair.value2(), getContentWidth() - getTypeface().measureWidth(textPair.value2()), top, 0xffffffff, true);
            }
        }

        @Override
        public int getContentWidth() {
            return textPairs.stream().mapToInt(text -> getTypeface().measureWidth(text.value1()) + getTypeface().measureWidth(text.value2())).max().orElse(0) + getTypeface().measureWidth(" ");
        }

        @Override
        public int getContentHeight() {
            return textPairs.size() * 10;
        }

        @Override
        public int getPaddingX() {
            return 10;
        }

        @Override
        public int getPaddingY() {
            return 0; // 2;
        }

        @Override
        public boolean isVisible() {
            return !textPairs.isEmpty();
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
            var top = 0;
            for (var text : texts) {
                top -= 10;
                renderer.renderTextFromStart(getTypeface(), text, getContentSide() == AxisDirection.POSITIVE ? getContentWidth() - getTypeface().measureWidth(text) : 0, top, 0xffffffff, true);
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
            return 0; // 2;
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
            renderer.renderTextFromStart(getTypeface(), header, positionX, -getContentHeight() + 1, 0xffffffff, true);
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

    public class ItemsEntry extends Entry {

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
                var x = getContentSide() == AxisDirection.NEGATIVE ? i * ITEM_SPACING_X : -(i + 1) * ITEM_SPACING_X + getContentWidth();
                var y = j * ITEM_SPACING_Y - ITEM_SPACING_Y * (int) MathUtils.ceil(1f * itemStacks.size() / MAX_COLUMN);

                var text = Text.text(String.valueOf(itemStack.getStackSize()));
                var color = ItemStackUtils.getColorTag(itemStack);

                renderer.pushPose();
                renderer.translate(-1, -1, 0);
                renderer.renderItem(itemStack, x + 1, y + 1);
                renderer.pushPose();
                renderer.translate(0, 0, 200F);
                renderer.renderText(getTypeface(), text, x + 19 - 2 - getTypeface().measureWidth(text), y + 6 + 3, color == 0 ? 16777215 : color, true);
                renderer.popPose();
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
            return (int) MathUtils.ceil(1f * itemStacks.size() / MAX_COLUMN) * ITEM_SPACING_Y;
        }

        @Override
        public int getPaddingX() {
            return 10;
        }

        @Override
        public int getPaddingY() {
            return 0; // 2;
        }

        @Override
        public boolean isVisible() {
            return true;
        }
    }



    public class IconEntry extends Entry {

        private static final int MAX_COLUMN = 9;
        private static final int ITEM_SPACING_X = 18;
        private static final int ITEM_SPACING_Y = 18;
        private final ResourceLocation icon;

        public IconEntry(ResourceLocation icon) {
            this.icon = icon;
        }


        @Override
        public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            super.renderWidget(renderer, mouseX, mouseY, deltaTick);

            renderer.pushPose();
            renderer.translate(0, -18, 200F);
            renderer.renderTexture(icon, 0, 0, 16, 16, 0f, 0f, 18, 18, 18, 18);
            renderer.popPose();
        }

        @Override
        public int getContentWidth() {
            return 18;
        }

        @Override
        public int getContentHeight() {
            return 18;
        }

        @Override
        public int getPaddingX() {
            return 10;
        }

        @Override
        public int getPaddingY() {
            return 0; // 2;
        }

        @Override
        public boolean isVisible() {
            return true;
        }
    }

    private class GroupEntry extends Entry {
        private final List<Entry> entries;
        public GroupEntry(List<Entry> entries) {
            this.entries = entries;
        }

        @Override
        public int getPaddingY() {
            return entries.stream().mapToInt(Entry::getPaddingY).max().orElse(0);
        }

        @Override
        public int getWidth() {
            return getContentWidth() + getPaddingX() * 2;
        }

        @Override
        public int getHeight() {
            return getContentHeight() + getPaddingY() * 2;
        }

        @Override
        public int getContentWidth() {
            return entries.stream().mapToInt(Entry::getWidth).max().orElse(0);
        }

        @Override
        public int getContentHeight() {
            return entries.stream().mapToInt(Entry::getHeight).sum();
        }

        @Override
        public boolean isVisible() {
            return entries.stream().anyMatch(Entry::isVisible);
        }

        @Override
        public void render(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            for (var entry : entries) {
                if (!entry.isVisible()) {
                    continue;
                }
                renderer.pushPose();
                renderer.pushPose();
                renderer.translate(entry.getPaddingX(), -entry.getPaddingY(), 0);
                entry.render(renderer, 0, 0, deltaTick);
                renderer.popPose();
                renderer.popPose();
                renderer.translate(0, -entry.getHeight(), 0);
            }
        }

        @Override
        public void tick() {
            super.tick();
            for (var entry : entries) {
                entry.tick();
            }
        }

        @Override
        public void setContentSide(AxisDirection contentSide) {
            super.setContentSide(contentSide);
            for (var entry : entries) {
                entry.setContentSide(contentSide);
            }
        }


    }

    private class EmptyEntry extends Entry {

        @Override
        public boolean isVisible() {
            return false;
        }
    }

    // FIXME: 24/9/23
    private abstract class Entry extends AbstractWidget {

        private static final int FADE_TICKS = 10;

        private int ticksTillRemoval = 40;
        private AxisDirection contentSide = AxisDirection.POSITIVE;

        protected Entry() {
            super(entrance, Text.empty());
        }

        public int getContentWidth(){
            return 0;
        }
        public int getContentHeight(){
            return 0;
        }
        public int getPaddingX(){
            return 0;
        }
        public int getPaddingY(){
            return 0;
        }

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

}
