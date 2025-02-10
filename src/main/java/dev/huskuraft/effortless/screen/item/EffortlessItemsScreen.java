package dev.huskuraft.effortless.screen.item;

import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.universal.api.core.Item;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.core.Items;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;

public class EffortlessItemsScreen extends AbstractPanelScreen {

    private final Consumer<List<Item>> consumer;
    private TextWidget titleTextWidget;
    private ItemStackList entries;
    private Button deleteButton;
    private Button clearButton;
    private Button addButton;
    private Button cancelButton;
    private Button saveButton;

    private List<Item> originalItems;
    private List<Item> items;

    public EffortlessItemsScreen(Entrance entrance, Text title, List<Item> items, Consumer<List<Item>> consumer) {
        super(entrance, title, PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.consumer = consumer;
        this.originalItems = items.stream().distinct().toList();
        this.items = items.stream().distinct().toList();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.entries = addWidget(new ItemStackList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_2));
        this.entries.setAlwaysShowScrollbar(true);

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1 / 3f).build());

        this.clearButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.clear"), button -> {
            entries.clear();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 1 / 3f, 1 / 3f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.add"), button -> {
            new EffortlessItemPickerScreen(getEntrance(), item -> item != Items.AIR.item(), (item) -> {
                if (item != null && !items.contains(item)) {
                    entries.insertSelected(item.getDefaultStack());
                    onReload();
                }
            }).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 2 / 3f, 1 / 3f).build());
        this.entries.reset(items.stream().map(Item::getDefaultStack).toList());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(items);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

    }

    @Override
    public void onReload() {
        this.items = entries.items().stream().map(ItemStack::getItem).toList();
        this.deleteButton.setActive(entries.hasSelected());
        this.clearButton.setActive(!entries.items().isEmpty());
    }

}
