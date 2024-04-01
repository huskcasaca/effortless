package dev.huskuraft.effortless.screen.item;

import java.util.Locale;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.input.EditBox;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.SearchBy;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class EffortlessItemsScreen extends AbstractScreen {

    private final Consumer<Item> applySettings;
    private TextWidget titleTextWidget;
    private ItemStackList entries;
    private EditBox searchEditBox;
    private Button addButton;
    private Button cancelButton;

    public EffortlessItemsScreen(Entrance entrance, Consumer<Item> consumer) {
        super(entrance, Text.translate("effortless.item.picker.title"));
        this.applySettings = consumer;
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Screen.TITLE_24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getWidth() / 2 - (Dimens.Entry.ROW_WIDTH) / 2, Dimens.Screen.TITLE_24, Dimens.Entry.ROW_WIDTH, 20, Text.translate("effortless.item.picker.search"))
        );
        this.searchEditBox.setMaxLength(ItemRandomizer.MAX_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("effortless.item.picker.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.item.picker.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.item.picker.add"), button -> {
            applySettings.accept(entries.getSelected().getItem().getItem());
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new ItemStackList(getEntrance(), 0, Dimens.Screen.TITLE_24 + 26, getWidth(), getHeight() - Dimens.Screen.TITLE_24 - 26 - Dimens.Screen.BUTTON_ROW_1));

        setSearchResult("");
    }

    @Override
    public void onReload() {
        addButton.setActive(entries.hasSelected());
    }

    private void setSearchResult(String string) {
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
