package dev.huskuraft.effortless.screen.transformer;

import java.util.function.Consumer;

import dev.huskuraft.effortless.api.gui.AbstractContainerScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.screen.item.EffortlessItemPickerScreen;

public class EffortlessItemRandomizerEditScreen extends AbstractContainerScreen {

    private final Consumer<ItemRandomizer> applySettings;
    private final ItemRandomizer defaultSettings;
    private ItemRandomizer lastSettings;
    private TextWidget titleTextWidget;
    private ItemChanceList entries;
    private Button orderButton;
    private Button supplierButton;
    private Button upButton;
    private Button downButton;
    private Button deleteButton;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;

    private Randomizer.Order lastOrder;
    private Randomizer.Target lastTarget;

    public EffortlessItemRandomizerEditScreen(Entrance entrance, Consumer<ItemRandomizer> consumer, ItemRandomizer randomizer) {
        super(entrance, Text.translate("effortless.randomizer.edit.title"), CONTAINER_WIDTH_EXPANDED, CONTAINER_HEIGHT_270);
        this.applySettings = consumer;
        this.defaultSettings = randomizer;
        this.lastSettings = randomizer;
        this.lastOrder = randomizer.getOrder();
        this.lastTarget = randomizer.getTarget();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + TITLE_CONTAINER - 10, getScreenTitle().withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        this.orderButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.order", lastSettings.getOrder().getDisplayName()), button -> {
            lastOrder = Randomizer.Order.values()[(lastOrder.ordinal() + 1) % Randomizer.Order.values().length];
            orderButton.setMessage(Text.translate("effortless.randomizer.edit.order", lastOrder.getDisplayName()));
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), TITLE_CONTAINER + BUTTON_CONTAINER_ROW_1, 0f, 0f, 0.5f).build());
        this.supplierButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.target", lastTarget.getDisplayName()), button -> {
            lastTarget = Randomizer.Target.values()[(lastTarget.ordinal() + 1) % Randomizer.Target.values().length];
            supplierButton.setMessage(Text.translate("effortless.randomizer.edit.target", lastTarget.getDisplayName()));
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), TITLE_CONTAINER + BUTTON_CONTAINER_ROW_1, 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new ItemChanceList(getEntrance(), getLeft() + PADDINGS, getTop() + TITLE_CONTAINER + BUTTON_CONTAINER_ROW_1N, getWidth() - PADDINGS * 2 - 8, getHeight() - TITLE_CONTAINER - BUTTON_CONTAINER_ROW_2 - BUTTON_CONTAINER_ROW_1N));
        this.entries.setAlwaysShowScrollbar(true);
        this.entries.reset(lastSettings.getChances());

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
            new EffortlessItemPickerScreen(
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
            applySettings.accept(lastSettings);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());
    }

    @Override
    public void onReload() {

        this.upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        this.downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        this.deleteButton.setActive(entries.hasSelected());
        this.addButton.setActive(entries.children().size() <= ItemRandomizer.MAX_CHANCE_SIZE);

        this.lastSettings = ItemRandomizer.create(
                lastOrder,
                lastTarget,
                Randomizer.Category.ITEM,
                entries.items());
    }

}
