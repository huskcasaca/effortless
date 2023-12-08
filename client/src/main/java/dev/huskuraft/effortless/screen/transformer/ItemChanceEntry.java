package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.input.NumberField;
import dev.huskuraft.effortless.gui.slot.ItemSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.ArrayList;
import java.util.List;

public class ItemChanceEntry extends EditableEntry<Chance<Item>> {

    private ItemSlot itemSlot;
    private TextWidget nameTextWidget;
    private TextWidget chanceTextWidget;
    private NumberField numberField;

    public ItemChanceEntry(Entrance entrance, ItemChanceList itemChanceList, Chance<Item> chance) {
        super(entrance, itemChanceList, chance);
    }

    public static List<Text> getRandomizerEntryTooltip(Player player, Chance<Item> chance, int totalCount) {
        var components = new ArrayList<>(chance.content().getDefaultStack().getDescription(player, ItemStack.TooltipType.ADVANCED_CREATIVE));
        var percentage = String.format("%.2f%%", 100.0 * chance.chance() / totalCount);
        components.add(
                Text.empty()
        );
        components.add(
                Text.translate("effortless.randomizer.edit.total_probability", Text.text(percentage).withStyle(TextStyle.GOLD).append(Text.text(" (" + chance.chance() + "/" + totalCount + ")").withStyle(TextStyle.DARK_GRAY))).withStyle(TextStyle.GRAY)
        );
        return components;
    }

    @Override
    public ItemChanceList getEntryList() {
        return (ItemChanceList) super.getEntryList();
    }

    @Override
    public List<Text> getTooltip() {
        if (getEntrance().getClient().hasAltDown()) {
            return getRandomizerEntryTooltip(getEntrance().getClient().getPlayer(), getItem(), getEntryList().totalCount());
        } else {
            return super.getTooltip();
        }
    }

    @Override
    public void onCreate() {
        this.numberField = addWidget(new NumberField(getEntrance(), getX() + getWidth() - 42, getY() + 1, 42, 18));
        this.numberField.getTextField().setFilter(string -> {
            if (string.isEmpty()) {
                return true;
            }
            try {
                var result = Integer.parseInt(string);
                if (result < Chance.MIN_ITEM_COUNT || result > Chance.MAX_ITEM_COUNT) {
                    numberField.getTextField().setValue(String.valueOf(MathUtils.clamp(result, Chance.MIN_ITEM_COUNT, Chance.MAX_ITEM_COUNT)));
                    return false;
                }
                if (!String.valueOf(result).equals(string)) {
                    numberField.getTextField().setValue(String.valueOf(result));
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.numberField.getTextField().setValue(String.valueOf(getItem().chance()));
        this.numberField.getTextField().setResponder(string -> {
            var count = (byte) 0;
            try {
                count = Byte.parseByte(string);
            } catch (NumberFormatException ignored) {
            }
            this.setItem(Chance.of(getItem().content(), count));
        });
        this.itemSlot = addWidget(new ItemSlot(getEntrance(), getX() + 1, getY() + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getItem().content(), Text.text(String.valueOf(getItem().chance()))));
        this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 24, getY() + 6, getDisplayName(getItem())));
        this.chanceTextWidget = addWidget(new TextWidget(getEntrance(), getX() + getWidth() - 50, getY() + 6, Text.empty()));
    }

    @Override
    public void onReload() {
        var percentage = String.format("%.2f%%", 100.0 * getItem().chance() / getEntryList().totalCount());
        chanceTextWidget.setX(((getX() + getWidth()) - 50 - getTypeface().measureWidth(percentage)));
        chanceTextWidget.setMessage(Text.text(percentage));
    }

    @Override
    public void onBindItem() {
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
    public int getWidth() {
        return Dimens.RegularEntry.ROW_WIDTH;
    }

    @Override
    public int getHeight() {
        return 24;
    }
}
