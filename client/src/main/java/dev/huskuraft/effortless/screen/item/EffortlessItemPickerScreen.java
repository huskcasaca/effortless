package dev.huskuraft.effortless.screen.item;

import dev.huskuraft.effortless.content.SearchBy;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.gui.input.EditBox;
import dev.huskuraft.effortless.gui.slot.ItemSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class EffortlessItemPickerScreen extends AbstractScreen {

    private static final int MAX_SEARCH_NAME_LENGTH = 255;
    private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;

    private final Consumer<ItemStack> applySettings;
    private TextWidget titleTextWidget;
    private ItemStackList entries;
    private EditBox searchEditBox;
    private Button addButton;
    private Button cancelButton;

    public EffortlessItemPickerScreen(Entrance entrance, Consumer<ItemStack> consumer) {
        super(entrance, Text.translate("item.picker.title"));
        this.applySettings = consumer;
    }

    @Override
    public void onCreate() {
//        if (this.minecraft != null) {
        var player = getEntrance().getClient().getPlayer();
        var itemStacks = new ArrayList<ItemStack>();
        if (player != null) {
//            CreativeModeTabs.tryRebuildTabContents(((LocalPlayer) player).connection.enabledFeatures(), FabricAdapter.adapt(player).canUseGameMasterBlocks(), FabricAdapter.adapt(player).level().registryAccess());
            itemStacks.add(getEntrance().getContentCreator().emptyItemStack());
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
                new EditBox(getEntrance(), getWidth() / 2 - (ROW_WIDTH - 2) / 2, 24, ROW_WIDTH - 2, 20, Text.translate("item.picker.search"))
        );
        this.searchEditBox.setMaxLength(MAX_SEARCH_NAME_LENGTH);
        this.searchEditBox.setHint(Text.translate("item.picker.search_hint"));
        this.searchEditBox.setResponder(text -> {
            setSearchResult(text);
        });

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("item.picker.add"), button -> {
            applySettings.accept(entries.getSelected().getItem());
            detach();
        }).bounds(getWidth() / 2 - 154, getHeight() - 28, 150, 20).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("item.picker.cancel"), button -> {
            detach();
        }).bounds(getWidth() / 2 + 4, getHeight() - 28, 150, 20).build());

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
            var searchTree = getEntrance().getContentCreator().getItemsSearchTree(SearchBy.TAG);
            entries.reset(searchTree.search(string.substring(1).toLowerCase(Locale.ROOT)));
        } else {
            var searchTree = getEntrance().getContentCreator().getItemsSearchTree(SearchBy.NAME);
            entries.reset(searchTree.search(string.toLowerCase(Locale.ROOT)));
        }
        entries.setSelected(null);
        entries.setScrollAmount(0);
    }

    private static final class ItemStackList extends EditableEntryList<ItemStack> {

        public ItemStackList(Entrance entrance, int x, int y, int width, int height) {
            super(entrance, x, y, width, height);
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getWidth() / 2 + 160;
        }

        @Override
        protected EditableEntry<ItemStack> createHolder(ItemStack item) {
            return new ItemStackEntry(getEntrance(), item);
        }

        protected static class ItemStackEntry extends EditableEntry<ItemStack> {

            private ItemSlot itemSlot;
            private TextWidget nameTextWidget;

            public ItemStackEntry(Entrance entrance, ItemStack itemStack) {
                super(entrance, itemStack);
            }

            @Override
            public void onCreate() {
                this.itemSlot = addWidget(new ItemSlot(getEntrance(), getX(), getY(), Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, getItem(), Text.empty()));
                this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 24, getY() + 6, getDisplayName(getItem())));
            }

            @Override
            public void setItem(ItemStack item) {
                super.setItem(item);
                itemSlot.setItemStack(item);
                nameTextWidget.setMessage(getDisplayName(item));
            }

            @Override
            public Text getNarration() {
                return Text.translate("narrator.select", getDisplayName(getItem()));
            }

            @Override
            public int getWidth() {
                return Dimens.RegularEntry.ROW_WIDTH;
            }

            @Override
            public int getHeight() {
                return 24;
            }

            private Text getDisplayName(ItemStack itemStack) {
                return itemStack.getHoverName();
            }

        }
    }
}
