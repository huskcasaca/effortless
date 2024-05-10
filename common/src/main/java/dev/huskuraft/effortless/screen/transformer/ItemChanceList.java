package dev.huskuraft.effortless.screen.transformer;

import java.util.ArrayList;
import java.util.List;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.container.EditableEntryList;
import dev.huskuraft.effortless.api.gui.input.NumberField;
import dev.huskuraft.effortless.api.gui.slot.ItemSlot;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;

public final class ItemChanceList extends EditableEntryList<Chance<Item>> {

    public ItemChanceList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected EditableEntryList.Entry<Chance<Item>> createHolder(Chance<Item> item) {
        return new Entry(getEntrance(), this, item);
    }

    public static class Entry extends EditableEntryList.Entry<Chance<Item>> {

        private ItemSlot itemSlot;
        private TextWidget nameTextWidget;
        private TextWidget chanceTextWidget;
        private NumberField numberField;

        public Entry(Entrance entrance, ItemChanceList itemChanceList, Chance<Item> chance) {
            super(entrance, itemChanceList, chance);
        }

        public static List<Text> getRandomizerEntryTooltip(Player player, Chance<Item> chance, int totalCount) {
            var components = new ArrayList<>(chance.content().getDefaultStack().getTooltips(player, ItemStack.TooltipType.ADVANCED_CREATIVE));
            var percentage = String.format("%.2f%%", 100.0 * chance.chance() / totalCount);
            components.add(
                    Text.empty()
            );
            components.add(
                    Text.translate("effortless.randomizer.edit.total_probability", ChatFormatting.GOLD + percentage + ChatFormatting.DARK_GRAY + " (" + chance.chance() + "/" + totalCount + ")").withStyle(ChatFormatting.GRAY)
            );
            return components;
        }

        public int totalCount() {
            return getEntryList().items().stream().mapToInt(Chance::chance).sum();
        }

        @Override
        public ItemChanceList getEntryList() {
            return (ItemChanceList) super.getEntryList();
        }

        @Override
        public List<Text> getTooltip() {
            if (getEntrance().getClient().getWindow().isAltDown()) {
                return getRandomizerEntryTooltip(getEntrance().getClient().getPlayer(), getItem(), totalCount());
            } else {
                return super.getTooltip();
            }
        }

        @Override
        public void onCreate() {
            this.numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 72, getY() + 1, 72, 18, NumberField.TYPE_INTEGER));
            this.numberField.setValueRange(Chance.MIN_ITEM_COUNT, Chance.MAX_ITEM_COUNT);
            this.numberField.setValue(getItem().chance());
            this.numberField.setValueChangeListener(value -> {
                this.setItem(Chance.of(getItem().content(), value.intValue()));
            });
            this.itemSlot = addWidget(new ItemSlot(getEntrance(), getX() + 1, getY() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getItem().content(), Text.text(String.valueOf(getItem().chance()))));
            this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 24, getY() + 6, getDisplayName(getItem())));
            this.chanceTextWidget = addWidget(new TextWidget(getEntrance(), getX() + getWidth() - 76, getY() + 6, Text.empty(), TextWidget.Gravity.END));
        }

        @Override
        public void onReload() {
            chanceTextWidget.setMessage(String.format("%.2f%%", 100.0 * getItem().chance() / totalCount()));
            itemSlot.setItemStack(getItem().content().getDefaultStack());
            itemSlot.setDescription(Text.text(String.valueOf(getItem().chance())));
            nameTextWidget.setMessage(getDisplayName(getItem()));
        }

        // TODO: 8/2/23
        @Override
        public Text getNarration() {
            return Text.translate("narrator.select", getDisplayName(getItem()));
        }

        private Text getDisplayName(Chance<Item> chance) {
            return chance.content().getDefaultStack().getHoverName();
        }

        @Override
        public int getHeight() {
            return 24;
        }
    }
}
