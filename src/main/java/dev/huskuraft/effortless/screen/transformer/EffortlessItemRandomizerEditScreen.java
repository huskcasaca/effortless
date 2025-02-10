package dev.huskuraft.effortless.screen.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.universal.api.core.BlockItem;
import dev.huskuraft.universal.api.core.Items;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.gui.tooltip.Palette;
import dev.huskuraft.universal.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.screen.item.EffortlessItemPickerScreen;

public class EffortlessItemRandomizerEditScreen extends AbstractPanelScreen {

    private final Consumer<ItemRandomizer> consumer;
    private final ItemRandomizer defaultRandomizer;
    private ItemRandomizer randomizer;
    private TextWidget titleTextWidget;
    private TransformerList transformerEntries;
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

    public EffortlessItemRandomizerEditScreen(Entrance entrance, Consumer<ItemRandomizer> consumer, ItemRandomizer randomizer) {
        super(entrance, Text.translate("effortless.transformer.randomizer.edit.title"));
        this.consumer = consumer;
        this.defaultRandomizer = randomizer;
        this.randomizer = randomizer;
    }

    @Override
    public void onCreate() {
        setWidth(PANEL_WIDTH_60);
        setHeight(randomizer.getSource() == ItemRandomizer.Source.CUSTOMIZE ? PANEL_HEIGHT_FULL : PANEL_TITLE_HEIGHT_1 + 38 + PANEL_BUTTON_ROW_HEIGHT_3);

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.transformerEntries = addWidget(new TransformerList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, 38));
        this.transformerEntries.setShowScrollBar(false);
        this.transformerEntries.setRenderSelection(false);
        this.transformerEntries.reset(List.of(randomizer));

        this.sourceButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.source", randomizer.getSource().getDisplayName()), button -> {
            var newSource = ItemRandomizer.Source.values()[(this.randomizer.source().ordinal() + 1) % ItemRandomizer.Source.values().length];
            this.randomizer = this.randomizer.withSource(newSource);
            sourceButton.setMessage(Text.translate("effortless.transformer.randomizer.edit.source", this.randomizer.getSource().getDisplayName()));
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + transformerEntries.getHeight() + PANEL_BUTTON_ROW_HEIGHT_2, 1f, 0f, 1f).build());

        this.orderButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.order", randomizer.getOrder().getDisplayName()), button -> {
            var newOrder = ItemRandomizer.Order.values()[(this.randomizer.getOrder().ordinal() + 1) % ItemRandomizer.Order.values().length];
            this.randomizer = this.randomizer.withOrder(newOrder);
            orderButton.setMessage(Text.translate("effortless.transformer.randomizer.edit.order", this.randomizer.order().getDisplayName()));
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + transformerEntries.getHeight() + PANEL_BUTTON_ROW_HEIGHT_2, 0f, 0f, 1 / 2f).build());

        this.targetButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.target", randomizer.getTarget().getDisplayName()), button -> {
            var newTarget = ItemRandomizer.Target.values()[(this.randomizer.getTarget().ordinal() + 1) % ItemRandomizer.Target.values().length];
            this.randomizer = this.randomizer.withTarget(newTarget);
            targetButton.setMessage(Text.translate("effortless.transformer.randomizer.edit.target", this.randomizer.target().getDisplayName()));
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + transformerEntries.getHeight() + PANEL_BUTTON_ROW_HEIGHT_2, 0f, 1 / 2f, 1 / 2f).build());

        this.entries = addWidget(new ItemChanceList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_BUTTON_ROW_HEIGHT_2N + transformerEntries.getHeight() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_2N - transformerEntries.getHeight() - PANEL_BUTTON_ROW_HEIGHT_2));
        this.entries.setAlwaysShowScrollbar(true);
        this.entries.reset(randomizer.getChances());

        this.upButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.up"), button -> {
            if (entries.hasSelected()) {
                entries.moveUpSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());
        this.downButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.down"), button -> {
            if (entries.hasSelected()) {
                entries.moveDownSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.delete"), button -> {
            entries.deleteSelected();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.add"), button -> {
            new EffortlessItemPickerScreen(
                    getEntrance(),
                    item -> item instanceof BlockItem || item.equals(Items.AIR.item()),
                    item -> {
                        entries.insertSelected(Chance.of(item, 1));
                        onReload();
                    }
            ).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.transformer.randomizer.edit.save"), button -> {
            consumer.accept(randomizer);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());
    }

    @Override
    public void onReload() {

        this.upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        this.downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        this.deleteButton.setActive(entries.hasSelected());
        this.addButton.setActive(entries.children().size() <= ItemRandomizer.MAX_CHANCE_SIZE);

        this.entries.setVisible(randomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.upButton.setVisible(randomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.downButton.setVisible(randomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.deleteButton.setVisible(randomizer.source() == ItemRandomizer.Source.CUSTOMIZE);
        this.addButton.setVisible(randomizer.source() == ItemRandomizer.Source.CUSTOMIZE);

        var sourceTooltip = new ArrayList<Text>();
        sourceTooltip.add(Text.translate("effortless.transformer.randomizer.edit.source.tooltip.title"));
        sourceTooltip.add(this.randomizer.getSource().getDisplayName().withStyle(ChatFormatting.GOLD));
        sourceTooltip.add(Text.empty());
        sourceTooltip.add(TooltipHelper.holdShiftForSummary());
        if (TooltipHelper.isSummaryButtonDown()) {
            sourceTooltip.add(Text.empty());
            sourceTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.transformer.randomizer.edit.source.tooltip.message").withStyle(ChatFormatting.GRAY)));
            for (var source : ItemRandomizer.Source.values()) {
                sourceTooltip.add(Text.empty());
                sourceTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.text("").append(source.getDisplayName()).append("\n").withColor(Palette.GRAY_AND_WHITE.highlight().color()).append(Text.translate("effortless.transformer.randomizer.edit.source.tooltip.%s".formatted(source.getName())).withColor(Palette.GRAY_AND_WHITE.primary().color()))));
            }
        }
        this.sourceButton.setTooltip(sourceTooltip);

        var orderTooltip = new ArrayList<Text>();
        orderTooltip.add(Text.translate("effortless.transformer.randomizer.edit.order.tooltip.title"));
        orderTooltip.add(this.randomizer.getOrder().getDisplayName().withStyle(ChatFormatting.GOLD));
        orderTooltip.add(Text.empty());
        orderTooltip.add(TooltipHelper.holdShiftForSummary());
        if (TooltipHelper.isSummaryButtonDown()) {
            orderTooltip.add(Text.empty());
            orderTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.transformer.randomizer.edit.order.tooltip.message").withStyle(ChatFormatting.GRAY)));
            for (var order : Randomizer.Order.values()) {
                orderTooltip.add(Text.empty());
                orderTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.text("").append(order.getDisplayName()).append("\n").withColor(Palette.GRAY_AND_WHITE.highlight().color()).append(Text.translate("effortless.transformer.randomizer.edit.order.tooltip.%s".formatted(order.getName())).withColor(Palette.GRAY_AND_WHITE.primary().color()))));
            }
        }
        this.orderButton.setTooltip(orderTooltip);

        var targetTooltip = new ArrayList<Text>();
        targetTooltip.add(Text.translate("effortless.transformer.randomizer.edit.target.tooltip.title"));
        targetTooltip.add(this.randomizer.getTarget().getDisplayName().withStyle(ChatFormatting.GOLD));
        targetTooltip.add(Text.empty());
        targetTooltip.add(TooltipHelper.holdShiftForSummary());
        if (TooltipHelper.isSummaryButtonDown()) {
            targetTooltip.add(Text.empty());
            targetTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.translate("effortless.transformer.randomizer.edit.target.tooltip.message").withStyle(ChatFormatting.GRAY)));
            for (var target : Randomizer.Target.values()) {
                targetTooltip.add(Text.empty());
                targetTooltip.addAll(TooltipHelper.wrapLines(getTypeface(), Text.text("").append(target.getDisplayName()).append("\n").withColor(Palette.GRAY_AND_WHITE.highlight().color()).append(Text.translate("effortless.transformer.randomizer.edit.target.tooltip.%s".formatted(target.getName())).withColor(Palette.GRAY_AND_WHITE.primary().color()))));
            }
        }
        this.targetButton.setTooltip(targetTooltip);

        this.randomizer = this.randomizer.withChances(entries.items());

        this.transformerEntries.reset(List.of(randomizer));


    }

}
