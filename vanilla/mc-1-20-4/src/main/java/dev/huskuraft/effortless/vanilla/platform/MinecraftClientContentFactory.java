package dev.huskuraft.effortless.vanilla.platform;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.input.KeyCodes;
import dev.huskuraft.effortless.api.input.OptionKeys;
import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.SearchBy;
import dev.huskuraft.effortless.api.platform.SearchTree;
import dev.huskuraft.effortless.api.renderer.RenderStateFactory;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;
import dev.huskuraft.effortless.vanilla.core.MinecraftItemStack;
import dev.huskuraft.effortless.vanilla.core.MinecraftKeyBinding;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftRenderStateFactory;
import dev.huskuraft.effortless.vanilla.texture.MinecraftTextureFactory;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.PlainTextSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;

public class MinecraftClientContentFactory extends MinecraftCommonContentFactory implements ClientContentFactory {

    public static final MinecraftClientContentFactory INSTANCE = new MinecraftClientContentFactory();

    @Override
    public SearchTree<ItemStack> searchItemStack(SearchBy searchBy) {
        var minecraftPlayer = Minecraft.getInstance().player;
        CreativeModeTabs.tryRebuildTabContents(minecraftPlayer.connection.enabledFeatures(), true, minecraftPlayer.clientLevel.registryAccess());

		var minecraftSearchTree = Minecraft.getInstance().getSearchTree(
				switch (searchBy) {
					case NAME -> SearchRegistry.CREATIVE_NAMES;
					case TAG -> SearchRegistry.CREATIVE_TAGS;
				}
		);
        return query -> minecraftSearchTree.search(query).stream().map(itemStack -> new MinecraftItemStack(itemStack)).collect(Collectors.toList());
    }

    @Override
    public <T> SearchTree<T> search(List<T> list, Function<T, Stream<Text>> keyExtractor) {
        return query -> PlainTextSearchTree.create(list, item -> keyExtractor.apply(item).map(text -> ((Component) text.reference()).getString())).search(query);
    }

    @Override
    public KeyBinding getOptionKeyBinding(OptionKeys key) {
        var minecraftKeyBinding = switch (key) {
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
        };
        return new MinecraftKeyBinding(minecraftKeyBinding);
    }

    @Override
    public KeyBinding newKeyBinding(String name, String category, KeyCodes key) {
        return new MinecraftKeyBinding(new KeyMapping(name, key.value(), category));
    }

    @Override
    public RenderStateFactory getRenderStateFactory() {
        return MinecraftRenderStateFactory.INSTANCE;
    }

    @Override
    public TextureFactory getTextureFactory() {
        return MinecraftTextureFactory.INSTANCE;
    }
}
