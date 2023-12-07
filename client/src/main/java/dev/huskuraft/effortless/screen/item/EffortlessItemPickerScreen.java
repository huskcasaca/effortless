package dev.huskuraft.effortless.screen.item;

import dev.huskuraft.effortless.content.SearchBy;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.input.EditBox;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class EffortlessItemPickerScreen extends AbstractScreen {

    private static final int MAX_SEARCH_NAME_LENGTH = 255;
    private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;

    private final Consumer<Item> applySettings;
    private TextWidget titleTextWidget;
    private ItemStackList entries;
    private EditBox searchEditBox;
    private Button addButton;
    private Button cancelButton;

    public EffortlessItemPickerScreen(Entrance entrance, Consumer<Item> consumer) {
        super(entrance, Text.translate("effortless.item.picker.title"));
        this.applySettings = consumer;
    }

    @Override
    public void onCreate() {
//        if (this.minecraft != null) {
        var player = getEntrance().getClient().getPlayer();
        var itemStacks = new ArrayList<Item>();
        if (player != null) {
//            CreativeModeTabs.tryRebuildTabContents(((LocalPlayer) player).connection.enabledFeatures(), FabricAdapter.adapt(player).canUseGameMasterBlocks(), FabricAdapter.adapt(player).level().registryAccess());
            itemStacks.add(getEntrance().getContentCreator().itemStack().getItem());
//            itemStack.addAll(CreativeModeTabs.searchTab().getSearchTabDisplayItems());
//            itemStack.add(new ItemStack(Items.AIR));
//            itemStack.addAll(CreativeModeTabs.searchTab().getSearchTabDisplayItems());

        } else {
//            itemStack.addAll(BuiltInRegistries.ITEM.stream().map(ItemStack::new).toList());
        }

//        getEntrance().getClient().getRef().populateSearchTree(SearchRegistry.CREATIVE_NAMES, itemStack);
//        getEntrance().getClient().getRef().populateSearchTree(SearchRegistry.CREATIVE_TAGS, itemStack);
//        }

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.searchEditBox = addWidget(
                new EditBox(getEntrance(), getWidth() / 2 - (ROW_WIDTH - 2) / 2, 24, ROW_WIDTH - 2, 20, Text.translate("effortless.item.picker.search"))
        );
        this.searchEditBox.setMaxLength(MAX_SEARCH_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("effortless.item.picker.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.item.picker.add"), button -> {
            applySettings.accept(entries.getSelected().getItem().getItem());
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.item.picker.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new ItemStackList(getEntrance(), 0, 50, getWidth(), getHeight() - 50 - 36));

        setSearchResult("");
    }

    @Override
    public void onReload() {
        super.onReload();

        addButton.setActive(entries.hasSelected());
    }

    private void setSearchResult(String string) {
        if (string.startsWith("#")) {
            var searchTree = getEntrance().getContentCreator().itemStackSearchTree(SearchBy.TAG);
            entries.reset(searchTree.search(string.substring(1).toLowerCase(Locale.ROOT)));
        } else {
            var searchTree = getEntrance().getContentCreator().itemStackSearchTree(SearchBy.NAME);
            entries.reset(searchTree.search(string.toLowerCase(Locale.ROOT)));
        }
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

}
