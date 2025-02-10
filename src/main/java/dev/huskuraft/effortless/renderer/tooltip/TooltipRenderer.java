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
import dev.huskuraft.universal.api.core.AxisDirection;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.core.Tuple2;
import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.renderer.Renderer;
import dev.huskuraft.universal.api.text.Text;

public class TooltipRenderer {

    private final Entrance entrance;

    private final Map<Integer, Map<Object, Entry>> prioritiedMap = new TreeMap<>(Comparator.naturalOrder());

    public TooltipRenderer(Entrance entrance) {
        this.entrance = entrance;
    }

    public EffortlessClient getEntrance() {
        return (EffortlessClient) entrance;
    }

    @SuppressWarnings("unchecked")
    public void showGroupEntry(Object id, int priority, List<Object> entry, boolean immediate) {
        var entries = entry.stream().map(object -> {
            if (object instanceof List<?> list && !list.isEmpty()) {
                if (list.get(0) instanceof Text text) {
                    return new ComponentsEntry(Collections.singletonList(text));
                } else if (list.get(0) instanceof Tuple2<?, ?> tuple2) {
                    if (tuple2.value1() instanceof Text && tuple2.value2() instanceof Text) {
                        return new ComponentPairEntry((Collection<Tuple2<Text, Text>>) list);
                    }
                } else if (list.get(0) instanceof ItemStack) {
                    return new ItemsEntry((Collection<ItemStack>) list);
                }
            } else if (object instanceof Text text) {
                return new ComponentsEntry(Collections.singletonList(text));
            } else if (object instanceof ResourceLocation icon) {
                return new IconEntry(icon);
            } else if (object instanceof Tuple2<?,?> tuple2 && tuple2.value1() instanceof List<?> list && tuple2.value2() instanceof Integer color) {
                if (!list.isEmpty() && list.get(0) instanceof ItemStack) {
                    return new ItemsEntry((Collection<ItemStack>) list, color);
                }
            }
            return null;
        }).filter(Objects::nonNull).toList();
        showEntry(id, priority, new GroupEntry(entries), immediate);
    }

    public void showEntry(Object id, int priority, Entry entry, boolean immediate) {
        synchronized (prioritiedMap) {
            prioritiedMap.compute(priority, (k, entry1) -> {
                if (entry1 == null) {
                    entry1 = new LinkedHashMap<>();
                }
                entry1.compute(id, (k1, v1) -> {
                    if (v1 != null && !immediate) {
                        if (v1.ticksTillRemoval < Entry.FADE_TICKS) {
                            entry.ticksAlive = (int) (Entry.FADE_TICKS * (MathUtils.lerp(1.0 * v1.ticksTillRemoval / Entry.FADE_TICKS, 0, 1) * MathUtils.lerp(1.0 * v1.ticksTillRemoval / Entry.FADE_TICKS, 0, 1)));
                            entry.ticksTillRemoval = Entry.ALIVE_TICKS;
                        } else {
                            entry.ticksAlive = v1.ticksAlive;
                        }
                    }
                    if (immediate) {
                        entry.ticksAlive = Entry.FADE_TICKS;
                    }
                    return entry;
                });
                return entry1;
            });
        }
    }

    public void hideEntry(Object id, int priority, boolean immediate) {
        synchronized (prioritiedMap) {
            if (immediate) {
                prioritiedMap.getOrDefault(priority, new LinkedHashMap<>()).remove(id);
            } else {
                prioritiedMap.getOrDefault(priority, new LinkedHashMap<>()).computeIfPresent(id, (k, entry) -> {
                    entry.ticksTillRemoval = MathUtils.min(entry.ticksTillRemoval, Entry.FADE_TICKS);
                    entry.ticksAlive = Entry.FADE_TICKS;
                    return entry;
                });
            }
        }
    }

    public void hideAllEntries(boolean immediate) {
        synchronized (prioritiedMap) {
            if (immediate) {
                prioritiedMap.clear();
            } else {
                for (var value : prioritiedMap.values()) {
                    for (var entry : value.values()) {
                        entry.ticksTillRemoval = MathUtils.min(entry.ticksTillRemoval, Entry.FADE_TICKS);
                        entry.ticksAlive = Entry.FADE_TICKS;
                    }
                }
            }
        }
    }
//
//    public void resetEntry(Object id, int priority) {
//        synchronized (prioritiedMap) {
//            prioritiedMap.getOrDefault(priority, new LinkedHashMap<>()).computeIfPresent(id, (k, v) -> {
//                v.ticksTillRemoval = Entry.ALIVE_TICKS;
//                v.ticksAlive = 0;
//                return v;
//            });
//        }
//    }


    public void tick() {

        synchronized (prioritiedMap) {
            var iterator = prioritiedMap.values().iterator();
            while (iterator.hasNext()) {
                var map = iterator.next();
                for (var iterator1 = map.values().iterator(); iterator1.hasNext(); ) {
                    var entry = iterator1.next();
                    entry.onTick();
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
        renderer.translate(0f, renderer.getWindow().getGuiScaledHeight() * 1f, 0);
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
                    var removalTicks = entry.ticksTillRemoval - deltaTick;
                    if (removalTicks < Entry.FADE_TICKS) {
                        renderer.translate(entry.getWidth() * (MathUtils.lerp(removalTicks / Entry.FADE_TICKS, 1, 0) * MathUtils.lerp(removalTicks / Entry.FADE_TICKS, 1, 0)), 0, 0);
                    }
                    var appearTicks = entry.ticksAlive + deltaTick;
                    if (appearTicks < Entry.FADE_TICKS) {
                        renderer.translate(entry.getWidth() * (MathUtils.lerp(appearTicks / Entry.FADE_TICKS, 1, 0) * MathUtils.lerp(appearTicks / Entry.FADE_TICKS, 1, 0)), 0, 0);
                    }

                    if (contentSide == AxisDirection.POSITIVE) {
                        renderer.translate(renderer.getWindow().getGuiScaledWidth() - entry.getWidth(), 0, 0);
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
        private final Integer color;

        public ItemsEntry(Collection<ItemStack> itemStacks, Integer color) {
            this.itemStacks = itemStacks;
            this.color = color;
        }

        public ItemsEntry(Collection<ItemStack> itemStacks) {
            this.itemStacks = itemStacks;
            this.color = null;
        }


        @Override
        public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
            super.renderWidget(renderer, mouseX, mouseY, deltaTick);

            var i = 0;
            var j = 0;

            for (var itemStack : itemStacks) {
                var x = getContentSide() == AxisDirection.NEGATIVE ? i * ITEM_SPACING_X : -(i + 1) * ITEM_SPACING_X + getContentWidth();
                var y = j * ITEM_SPACING_Y - ITEM_SPACING_Y * (int) MathUtils.ceil(1f * itemStacks.size() / MAX_COLUMN);

                var text = Text.text(String.valueOf(itemStack.getCount()));

                renderer.pushPose();
                renderer.translate(-1, -1, 0);
                renderer.renderItem(itemStack, x + 1, y + 1);
                renderer.pushPose();
                renderer.translate(0, 0, 200F);
                renderer.renderText(getTypeface(), text, x + 19 - 2 - getTypeface().measureWidth(text), y + 6 + 3, color == null ? 16777215 : color, true);
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
        public void onTick() {
            super.onTick();
            for (var entry : entries) {
                entry.onTick();
            }
        }

        @Override
        public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
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
        private static final int ALIVE_TICKS = 30;

        private int ticksAlive = 0;
        private int ticksTillRemoval = ALIVE_TICKS;
        private AxisDirection contentSide = AxisDirection.POSITIVE;

        protected Entry() {
            super(entrance, Text.empty());
        }

        public int getContentWidth() {
            return 0;
        }

        public int getContentHeight() {
            return 0;
        }

        public int getPaddingX() {
            return 0;
        }

        public int getPaddingY() {
            return 0;
        }

        public int getWidth() {
            return getContentWidth() + getPaddingX() * 2;
        }

        public int getHeight() {
            return getContentHeight() + getPaddingY() * 2;
        }

        public void onTick() {
            ticksAlive++;
            ticksTillRemoval--;
        }

        public boolean isAlive() {
            return ticksTillRemoval >= 0;
        }

        public boolean isFading() {
            return ticksTillRemoval < FADE_TICKS || ticksAlive < FADE_TICKS;
        }

        public float getAlpha() {
            return isFading() ? MathUtils.min((float) (ticksTillRemoval) / FADE_TICKS, (float) (ticksAlive) / FADE_TICKS) : 1;
        }

        public AxisDirection getContentSide() {
            return contentSide;
        }

        public void setContentSide(AxisDirection contentSide) {
            this.contentSide = contentSide;
        }
    }

}
