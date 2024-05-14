package dev.huskuraft.effortless.screen.transformer;

import java.util.ArrayList;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.gui.tooltip.Palette;
import dev.huskuraft.effortless.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.effortless.api.input.Keys;
import dev.huskuraft.effortless.api.lang.Lang;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.screen.item.EffortlessBlockItemPickerScreen;

public class EffortlessItemRandomizerEditScreen extends AbstractPanelScreen {

    private final Consumer<ItemRandomizer> applySettings;
    private final ItemRandomizer defaultSettings;
    private ItemRandomizer itemRandomizer;
    private TextWidget titleTextWidget;
    private ItemChanceList entries;
    private Button sourceButton;
    private Button orderButton;
    private Button targetButton;
    private Button upButton;
    private Button downButton;
    private Button deleteButton;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;

    private ItemRandomizer.Source lastSource;
    private ItemRandomizer.Order lastOrder;
    private ItemRandomizer.Target lastTarget;

    public EffortlessItemRandomizerEditScreen(Entrance entrance, Consumer<ItemRandomizer> consumer, ItemRandomizer randomizer) {
        super(entrance, Text.translate("effortless.randomizer.edit.title"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.applySettings = consumer;
        this.defaultSettings = randomizer;
        this.itemRandomizer = randomizer;
        this.lastOrder = randomizer.getOrder();
        this.lastTarget = randomizer.getTarget();
    }

    @Override
    public void onCreate() {
        setPanelHeight(itemRandomizer.getSource() == ItemRandomizer.Source.CUSTOMIZE ? PANEL_HEIGHT_270 : PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_4);
        setPanelWidth(itemRandomizer.getSource() == ItemRandomizer.Source.CUSTOMIZE ? PANEL_WIDTH_EXPANDED : PANEL_WIDTH);

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

        this.sourceButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.source", itemRandomizer.getSource().getDisplayName()), button -> {
            var newSource = ItemRandomizer.Source.values()[(this.itemRandomizer.source().ordinal() + 1) % ItemRandomizer.Source.values().length];
            this.itemRandomizer = this.itemRandomizer.withSource(newSource);
            sourceButton.setMessage(Text.translate("effortless.randomizer.edit.source", this.itemRandomizer.getSource().getDisplayName()));
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_3, 2f, 0f, 1f).build());

        this.orderButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.order", itemRandomizer.getOrder().getDisplayName()), button -> {
            var newOrder = ItemRandomizer.Order.values()[(this.itemRandomizer.getOrder().ordinal() + 1) % ItemRandomizer.Order.values().length];
            this.itemRandomizer = this.itemRandomizer.withOrder(newOrder);
            orderButton.setMessage(Text.translate("effortless.randomizer.edit.order", this.itemRandomizer.order().getDisplayName()));
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_3, 1f, 0f, 1f).build());

        this.targetButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.target", lastTarget.getDisplayName()), button -> {
            var newTarget = ItemRandomizer.Target.values()[(this.itemRandomizer.getTarget().ordinal() + 1) % ItemRandomizer.Target.values().length];
            this.itemRandomizer = this.itemRandomizer.withTarget(newTarget);
            targetButton.setMessage(Text.translate("effortless.randomizer.edit.target", this.itemRandomizer.target().getDisplayName()));
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_3, 0f, 0f, 1f).build());

        this.entries = addWidget(new ItemChanceList(getEntrance(), getLeft() + PADDINGS, getTop() + PANEL_BUTTON_ROW_HEIGHT_3N + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_3N - PANEL_BUTTON_ROW_HEIGHT_2));
        this.entries.setAlwaysShowScrollbar(true);
        this.entries.reset(itemRandomizer.getChances());

        this.upButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.up"), button -> {
            if (entries.hasSelected()) {
                entries.moveUpSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());
        this.downButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.down"), button -> {
            if (entries.hasSelected()) {
                entries.moveDownSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.delete"), button -> {
            entries.deleteSelected();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.add"), button -> {
            new EffortlessBlockItemPickerScreen(
                    getEntrance(),
                    item -> {
                        entries.insertSelected(Chance.of(item, (byte) 1));
                        onReload();
                    }
            ).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.save"), button -> {
            applySettings.accept(itemRandomizer);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());
    }

    @Override
    public void onReload() {

        this.upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        this.downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        this.deleteButton.setActive(entries.hasSelected());
        this.addButton.setActive(entries.children().size() <= ItemRandomizer.MAX_CHANCE_SIZE);

        this.entries.setVisible(itemRandomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.upButton.setVisible(itemRandomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.downButton.setVisible(itemRandomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.deleteButton.setVisible(itemRandomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.addButton.setVisible(itemRandomizer.source() == ItemRandomizer.Source.CUSTOMIZE);

        var sourceTooltip = new ArrayList<Text>();
        sourceTooltip.add(Text.translate("effortless.randomizer.edit.source.tooltip.title"));
        sourceTooltip.add(this.itemRandomizer.getSource().getDisplayName().withStyle(ChatFormatting.GOLD));
        sourceTooltip.add(Text.empty());
        if (!Keys.KEY_LEFT_SHIFT.getBinding().isDown() && !Keys.KEY_LEFT_SHIFT.getBinding().isDown()) {
            sourceTooltip.add(Lang.translate("tooltip.hold_for_summary", Lang.translateKeyDesc("shift").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            sourceTooltip.add(Lang.translate("tooltip.hold_for_summary", Lang.translateKeyDesc("shift").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
            sourceTooltip.add(Text.empty());
            sourceTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.randomizer.edit.source.tooltip.message").withStyle(ChatFormatting.GRAY)));
            for (var source : ItemRandomizer.Source.values()) {
                sourceTooltip.add(Text.empty());
                sourceTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.text("[").append(source.getDisplayName()).append("] ").withColor(Palette.GRAY_AND_GOLD.highlight().color()).append(Text.translate("effortless.randomizer.edit.source.tooltip.%s".formatted(source.getName())).withColor(Palette.GRAY_AND_GOLD.primary().color()))));
            }
        }
        this.sourceButton.setTooltip(sourceTooltip);

        var orderTooltip = new ArrayList<Text>();
        orderTooltip.add(Text.translate("effortless.randomizer.edit.order.tooltip.title"));
        orderTooltip.add(this.itemRandomizer.getOrder().getDisplayName().withStyle(ChatFormatting.GOLD));
        orderTooltip.add(Text.empty());
        if (!Keys.KEY_LEFT_SHIFT.getBinding().isDown() && !Keys.KEY_LEFT_SHIFT.getBinding().isDown()) {
            orderTooltip.add(Lang.translate("tooltip.hold_for_summary", Lang.translateKeyDesc("shift").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            orderTooltip.add(Lang.translate("tooltip.hold_for_summary", Lang.translateKeyDesc("shift").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
            orderTooltip.add(Text.empty());
            orderTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.randomizer.edit.order.tooltip.message").withStyle(ChatFormatting.GRAY)));
            for (var order : Randomizer.Order.values()) {
                orderTooltip.add(Text.empty());
                orderTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.text("[").append(order.getDisplayName()).append("] ").withColor(Palette.GRAY_AND_GOLD.highlight().color()).append(Text.translate("effortless.randomizer.edit.order.tooltip.%s".formatted(order.getName())).withColor(Palette.GRAY_AND_GOLD.primary().color()))));
            }
        }
        this.orderButton.setTooltip(orderTooltip);

        var targetTooltip = new ArrayList<Text>();
        targetTooltip.add(Text.translate("effortless.randomizer.edit.target.tooltip.title"));
        targetTooltip.add(this.itemRandomizer.getTarget().getDisplayName().withStyle(ChatFormatting.GOLD));
        targetTooltip.add(Text.empty());
        if (!Keys.KEY_LEFT_SHIFT.getBinding().isDown() && !Keys.KEY_LEFT_SHIFT.getBinding().isDown()) {
            targetTooltip.add(Lang.translate("tooltip.hold_for_summary", Lang.translateKeyDesc("shift").withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            targetTooltip.add(Lang.translate("tooltip.hold_for_summary", Lang.translateKeyDesc("shift").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
            targetTooltip.add(Text.empty());
            targetTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.randomizer.edit.target.tooltip.message").withStyle(ChatFormatting.GRAY)));
            for (var target : Randomizer.Target.values()) {
                targetTooltip.add(Text.empty());
                targetTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.text("[").append(target.getDisplayName()).append("] ").withColor(Palette.GRAY_AND_GOLD.highlight().color()).append(Text.translate("effortless.randomizer.edit.target.tooltip.%s".formatted(target.getName())).withColor(Palette.GRAY_AND_GOLD.primary().color()))));
            }
        }
        this.targetButton.setTooltip(targetTooltip);

        this.itemRandomizer = this.itemRandomizer.withChances(entries.items());


    }

}
