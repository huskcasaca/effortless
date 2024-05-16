package dev.huskuraft.effortless.screen.item;

import java.util.Locale;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.SearchBy;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class EffortlessItemPickerScreen extends AbstractPanelScreen {

    protected final Consumer<Item> consumer;
    protected TextWidget titleTextWidget;
    protected ItemStackList entries;
    protected EditBox searchEditBox;
    protected Button addButton;
    protected Button cancelButton;

    public EffortlessItemPickerScreen(Entrance entrance, Consumer<Item> consumer) {
        super(entrance, Text.translate("effortless.item.picker.title"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_270);
        this.consumer = consumer;
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(0x00404040), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getLeft() + PADDINGS, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS * 2, PANEL_TITLE_HEIGHT_2 - Button.COMPAT_SPACING_V, Text.translate("effortless.item.picker.search"))
        );
        this.searchEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("effortless.item.picker.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        this.entries = addWidget(new ItemStackList(getEntrance(), getLeft() + PADDINGS, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_TITLE_HEIGHT_2, getWidth() - PADDINGS * 2 - 8, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_TITLE_HEIGHT_2 - PANEL_BUTTON_ROW_HEIGHT_1));

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
            entries.reset(searchTree.search(string.substring(1).toLowerCase(Locale.ROOT)));
        } else {
            var searchTree = ClientContentFactory.getInstance().searchItemStack(SearchBy.NAME);
            entries.reset(searchTree.search(string.toLowerCase(Locale.ROOT)));
        }
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}
