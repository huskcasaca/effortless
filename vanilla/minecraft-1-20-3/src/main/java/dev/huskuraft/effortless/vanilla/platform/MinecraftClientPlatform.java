package dev.huskuraft.effortless.vanilla.platform;

import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.input.KeyBinding;
import dev.huskuraft.effortless.input.KeyBindings;
import dev.huskuraft.effortless.platform.ClientPlatform;
import dev.huskuraft.effortless.platform.SearchBy;
import dev.huskuraft.effortless.platform.SearchTree;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftClientAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.PlainTextSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.world.item.CreativeModeTabs;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class MinecraftClientPlatform extends MinecraftCommonPlatform implements ClientPlatform {

    @Override
    public SearchTree<ItemStack> newItemStackSearchTree(SearchBy searchBy) {
        var player = Minecraft.getInstance().player;
        CreativeModeTabs.tryRebuildTabContents(player.connection.enabledFeatures(), true, player.clientLevel.registryAccess());

        return query -> Minecraft.getInstance().getSearchTree(
                switch (searchBy) {
                    case NAME -> SearchRegistry.CREATIVE_NAMES;
                    case TAG -> SearchRegistry.CREATIVE_TAGS;
                }
        ).search(query).stream().map(MinecraftClientAdapter::adapt).toList();
    }

    @Override
    public <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor) {
        return query -> PlainTextSearchTree.create(list, item -> keyExtractor.apply(item).map(text -> MinecraftClientAdapter.adapt(text).getString())).search(query);
    }

    @Override
    public KeyBinding getKeyBinding(KeyBindings keyBindings) {
        return MinecraftClientAdapter.adapt(switch (keyBindings) {
            case ATTACK -> Minecraft.getInstance().options.keyAttack;
            case USE -> Minecraft.getInstance().options.keyUse;
            case PICK -> Minecraft.getInstance().options.keyPickItem;
        });
    }
}
