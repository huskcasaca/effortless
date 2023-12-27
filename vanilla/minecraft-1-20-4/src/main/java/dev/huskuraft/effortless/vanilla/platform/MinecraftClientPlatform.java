package dev.huskuraft.effortless.vanilla.platform;

import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.input.KeyBinding;
import dev.huskuraft.effortless.input.KeyCodes;
import dev.huskuraft.effortless.platform.ClientPlatform;
import dev.huskuraft.effortless.platform.SearchBy;
import dev.huskuraft.effortless.platform.SearchTree;
import dev.huskuraft.effortless.platform.VanillaKeys;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftItemStack;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftKeyBinding;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftText;
import net.minecraft.client.KeyMapping;
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
        ).search(query).stream().map(MinecraftItemStack::fromMinecraft).toList();
    }

    @Override
    public <T> SearchTree<T> newSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor) {
        return query -> PlainTextSearchTree.create(list, item -> keyExtractor.apply(item).map(text -> MinecraftText.toMinecraftText(text).getString())).search(query);
    }

    @Override
    public KeyBinding getKeyBinding(VanillaKeys key) {
        return MinecraftKeyBinding.fromMinecraft(switch (key) {
            case KEY_UP -> Minecraft.getInstance().options.keyUp;
            case KEY_LEFT -> Minecraft.getInstance().options.keyLeft;
            case KEY_DOWN -> Minecraft.getInstance().options.keyDown;
            case KEY_RIGHT -> Minecraft.getInstance().options.keyRight;
            case KEY_JUMP -> Minecraft.getInstance().options.keyJump;
            case KEY_SHIFT -> Minecraft.getInstance().options.keyShift;
            case KEY_SPRINT -> Minecraft.getInstance().options.keySprint;
            case KEY_INVENTORY -> Minecraft.getInstance().options.keyInventory;
            case KEY_SWAP_OFFHAND -> Minecraft.getInstance().options.keySwapOffhand;
            case KEY_DROP -> Minecraft.getInstance().options.keyDrop;
            case KEY_USE -> Minecraft.getInstance().options.keyUse;
            case KEY_ATTACK -> Minecraft.getInstance().options.keyAttack;
            case KEY_PICK_ITEM -> Minecraft.getInstance().options.keyPickItem;
            case KEY_CHAT -> Minecraft.getInstance().options.keyChat;
            case KEY_PLAYER_LIST -> Minecraft.getInstance().options.keyPlayerList;
            case KEY_COMMAND -> Minecraft.getInstance().options.keyCommand;
            case KEY_SOCIAL_INTERACTIONS -> Minecraft.getInstance().options.keySocialInteractions;
            case KEY_SCREENSHOT -> Minecraft.getInstance().options.keyScreenshot;
            case KEY_TOGGLE_PERSPECTIVE -> Minecraft.getInstance().options.keyTogglePerspective;
            case KEY_SMOOTH_CAMERA -> Minecraft.getInstance().options.keySmoothCamera;
            case KEY_FULLSCREEN -> Minecraft.getInstance().options.keyFullscreen;
            case KEY_SPECTATOR_OUTLINES -> Minecraft.getInstance().options.keySpectatorOutlines;
            case KEY_ADVANCEMENTS -> Minecraft.getInstance().options.keyAdvancements;
            case KEY_HOTBAR_SLOTS_1 -> Minecraft.getInstance().options.keyHotbarSlots[0];
            case KEY_HOTBAR_SLOTS_2 -> Minecraft.getInstance().options.keyHotbarSlots[1];
            case KEY_HOTBAR_SLOTS_3 -> Minecraft.getInstance().options.keyHotbarSlots[2];
            case KEY_HOTBAR_SLOTS_4 -> Minecraft.getInstance().options.keyHotbarSlots[3];
            case KEY_HOTBAR_SLOTS_5 -> Minecraft.getInstance().options.keyHotbarSlots[4];
            case KEY_HOTBAR_SLOTS_6 -> Minecraft.getInstance().options.keyHotbarSlots[5];
            case KEY_HOTBAR_SLOTS_7 -> Minecraft.getInstance().options.keyHotbarSlots[6];
            case KEY_HOTBAR_SLOTS_8 -> Minecraft.getInstance().options.keyHotbarSlots[7];
            case KEY_HOTBAR_SLOTS_9 -> Minecraft.getInstance().options.keyHotbarSlots[8];
            case KEY_SAVE_HOTBAR_ACTIVATOR -> Minecraft.getInstance().options.keySaveHotbarActivator;
            case KEY_LOAD_HOTBAR_ACTIVATOR -> Minecraft.getInstance().options.keyLoadHotbarActivator;
        });
    }

    @Override
    public KeyBinding newKeyBinding(String name, String category, KeyCodes key) {
        return MinecraftKeyBinding.fromMinecraft(new KeyMapping(name, key.value(), category));
    }
}
