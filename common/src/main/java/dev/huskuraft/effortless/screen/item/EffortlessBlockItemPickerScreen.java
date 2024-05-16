package dev.huskuraft.effortless.screen.item;

import java.util.Locale;
import java.util.function.Consumer;

import dev.huskuraft.effortless.api.core.BlockItem;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.platform.SearchBy;

public class EffortlessBlockItemPickerScreen extends EffortlessItemPickerScreen {

    public EffortlessBlockItemPickerScreen(Entrance entrance, Consumer<BlockItem> consumer) {
        super(entrance, item -> consumer.accept((BlockItem) item));
    }

    protected void setSearchResult(String string) {
        if (string.startsWith("#")) {
            var searchTree = ClientContentFactory.getInstance().searchItemStack(SearchBy.TAG);
            entries.reset(searchTree.search(string.substring(1).toLowerCase(Locale.ROOT)).stream().filter(ItemStack::isBlock).toList());
        } else {
            var searchTree = ClientContentFactory.getInstance().searchItemStack(SearchBy.NAME);
            entries.reset(searchTree.search(string.toLowerCase(Locale.ROOT)).stream().filter(ItemStack::isBlock).toList());
        }
        entries.setSelected(null);
        entries.setScrollAmount(0);

    }

}
