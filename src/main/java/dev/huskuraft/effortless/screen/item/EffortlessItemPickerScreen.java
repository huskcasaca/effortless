package dev.huskuraft.effortless.screen.item;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dev.huskuraft.universal.api.core.Item;
import dev.huskuraft.universal.api.core.Items;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.input.EditBox;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.ClientContentFactory;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.platform.SearchBy;
import dev.huskuraft.universal.api.platform.SearchTree;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class EffortlessItemPickerScreen extends AbstractPanelScreen {

    private final Predicate<Item> filter;
    protected final Consumer<Item> consumer;
    protected TextWidget titleTextWidget;
    protected ItemStackList entries;
    protected EditBox searchEditBox;
    protected Button addButton;
    protected Button cancelButton;

    public EffortlessItemPickerScreen(Entrance entrance, Predicate<Item> filter, Consumer<Item> consumer) {
        super(entrance, Text.translate("effortless.item.picker.title"), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.filter = filter;
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, PANEL_TITLE_HEIGHT_2 - Button.COMPAT_SPACING_V, Text.translate("effortless.item.picker.search"))
        );
        this.searchEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("effortless.item.picker.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        this.entries = addWidget(new ItemStackList(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_TITLE_HEIGHT_2, getWidth() - PADDINGS_H * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_TITLE_HEIGHT_2 - PANEL_BUTTON_ROW_HEIGHT_1));

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.item.picker.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.item.picker.add"), button -> {
            detach();
            consumer.accept(entries.getSelected().getItem().getItem());
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        setSearchResult("");
    }

    @Override
    public void onReload() {
        addButton.setActive(entries.hasSelected());
        if (entries.consumeDoubleClick() && entries.hasSelected()) {
            detach();
            consumer.accept(entries.getSelected().getItem().getItem());
        }
    }

    protected void setSearchResult(String string) {
        if (string.startsWith("#")) {
            var searchTree = ClientContentFactory.getInstance().searchItemStack(SearchBy.TAG);
            entries.reset(searchTree.search(string.substring(1).toLowerCase(Locale.ROOT)).stream().filter(itemStack -> filter.test(itemStack.getItem())).toList());
        } else {
            var airSearchTree = SearchTree.of(List.of(Items.AIR.item().getDefaultStack()), items -> Stream.of(items.getName().getString().toLowerCase(Locale.ROOT)));
            var searchTree = ClientContentFactory.getInstance().searchItemStack(SearchBy.NAME);
            entries.reset(
                    Stream.concat(
                            airSearchTree.search(string.toLowerCase(Locale.ROOT)).stream().filter(itemStack -> filter.test(itemStack.getItem())),
                            searchTree.search(string.toLowerCase(Locale.ROOT)).stream().filter(itemStack -> filter.test(itemStack.getItem()))
                    ).toList()
            );
        }
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}
