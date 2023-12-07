package dev.huskuraft.effortless.vanilla.content;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.content.ContentCreator;
import dev.huskuraft.effortless.content.SearchBy;
import dev.huskuraft.effortless.content.SearchTree;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.tag.TagRecord;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftAdapter;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftClientAdapter;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.PlainTextSearchTree;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinecraftClientContentCreator extends ContentCreator {

    private static Chance<ItemStack> chance(Item item, byte count) {
        return Chance.itemStack(MinecraftClientAdapter.adapt(item), count);
    }

    // FIXME: 24/10/23
    private static Text name(String key) {
        return Text.translate("effortless.transformer.randomize.example.%s".formatted(key));
    }

    @Override
    public Buffer allocateButter() {
        return MinecraftAdapter.adapt(new FriendlyByteBuf(Unpooled.buffer()));
    }

    @Override
    public TagRecord emptyTagRecord() {
        return MinecraftAdapter.adapt(new CompoundTag());
    }

    @Override
    public ItemStack emptyItemStack() {
        return MinecraftAdapter.adapt(net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public List<ItemRandomizer> getDefaultRandomizers() {

        var variantCobblestone = ItemRandomizer.create(
                name("variant_cobblestone"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.COBBLESTONE, (byte) 1),
                        chance(Items.MOSSY_COBBLESTONE, (byte) 1)));
        var variantStoneBrick = ItemRandomizer.create(
                name("variant_stone_brick"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.STONE_BRICKS, (byte) 1),
                        chance(Items.MOSSY_STONE_BRICKS, (byte) 1),
                        chance(Items.CRACKED_STONE_BRICKS, (byte) 1)));
        var allOres = ItemRandomizer.create(
                name("all_ores"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.COAL_ORE, (byte) 1),
                        chance(Items.COPPER_ORE, (byte) 1),
                        chance(Items.LAPIS_ORE, (byte) 1),
                        chance(Items.IRON_ORE, (byte) 1),
                        chance(Items.REDSTONE_ORE, (byte) 1),
                        chance(Items.GOLD_ORE, (byte) 1),
                        chance(Items.DIAMOND_ORE, (byte) 1),
                        chance(Items.EMERALD_ORE, (byte) 1)));
        var allDeepslateOres = ItemRandomizer.create(
                name("all_deepslate_ores"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.DEEPSLATE_COAL_ORE, (byte) 1),
                        chance(Items.DEEPSLATE_COPPER_ORE, (byte) 1),
                        chance(Items.DEEPSLATE_LAPIS_ORE, (byte) 1),
                        chance(Items.DEEPSLATE_IRON_ORE, (byte) 1),
                        chance(Items.DEEPSLATE_REDSTONE_ORE, (byte) 1),
                        chance(Items.DEEPSLATE_GOLD_ORE, (byte) 1),
                        chance(Items.DEEPSLATE_DIAMOND_ORE, (byte) 1),
                        chance(Items.DEEPSLATE_EMERALD_ORE, (byte) 1)));
        var colorfulCarpet = ItemRandomizer.create(
                name("colorful_carpet"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_CARPET, (byte) 1),
                        chance(Items.LIGHT_GRAY_CARPET, (byte) 1),
                        chance(Items.GRAY_CARPET, (byte) 1),
                        chance(Items.BLACK_CARPET, (byte) 1),
                        chance(Items.BROWN_CARPET, (byte) 1),
                        chance(Items.RED_CARPET, (byte) 1),
                        chance(Items.ORANGE_CARPET, (byte) 1),
                        chance(Items.YELLOW_CARPET, (byte) 1),
                        chance(Items.LIME_CARPET, (byte) 1),
                        chance(Items.GREEN_CARPET, (byte) 1),
                        chance(Items.CYAN_CARPET, (byte) 1),
                        chance(Items.LIGHT_BLUE_CARPET, (byte) 1),
                        chance(Items.BLUE_CARPET, (byte) 1),
                        chance(Items.PURPLE_CARPET, (byte) 1),
                        chance(Items.MAGENTA_CARPET, (byte) 1),
                        chance(Items.PINK_CARPET, (byte) 1)));
        var colorfulConcrete = ItemRandomizer.create(
                name("colorful_concrete"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_CONCRETE, (byte) 1),
                        chance(Items.LIGHT_GRAY_CONCRETE, (byte) 1),
                        chance(Items.GRAY_CONCRETE, (byte) 1),
                        chance(Items.BLACK_CONCRETE, (byte) 1),
                        chance(Items.BROWN_CONCRETE, (byte) 1),
                        chance(Items.RED_CONCRETE, (byte) 1),
                        chance(Items.ORANGE_CONCRETE, (byte) 1),
                        chance(Items.YELLOW_CONCRETE, (byte) 1),
                        chance(Items.LIME_CONCRETE, (byte) 1),
                        chance(Items.GREEN_CONCRETE, (byte) 1),
                        chance(Items.CYAN_CONCRETE, (byte) 1),
                        chance(Items.LIGHT_BLUE_CONCRETE, (byte) 1),
                        chance(Items.BLUE_CONCRETE, (byte) 1),
                        chance(Items.PURPLE_CONCRETE, (byte) 1),
                        chance(Items.MAGENTA_CONCRETE, (byte) 1),
                        chance(Items.PINK_CONCRETE, (byte) 1)));
        var colorfulConcretePowder = ItemRandomizer.create(
                name("colorful_concrete_powder"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_CONCRETE_POWDER, (byte) 1),
                        chance(Items.LIGHT_GRAY_CONCRETE_POWDER, (byte) 1),
                        chance(Items.GRAY_CONCRETE_POWDER, (byte) 1),
                        chance(Items.BLACK_CONCRETE_POWDER, (byte) 1),
                        chance(Items.BROWN_CONCRETE_POWDER, (byte) 1),
                        chance(Items.RED_CONCRETE_POWDER, (byte) 1),
                        chance(Items.ORANGE_CONCRETE_POWDER, (byte) 1),
                        chance(Items.YELLOW_CONCRETE_POWDER, (byte) 1),
                        chance(Items.LIME_CONCRETE_POWDER, (byte) 1),
                        chance(Items.GREEN_CONCRETE_POWDER, (byte) 1),
                        chance(Items.CYAN_CONCRETE_POWDER, (byte) 1),
                        chance(Items.LIGHT_BLUE_CONCRETE_POWDER, (byte) 1),
                        chance(Items.BLUE_CONCRETE_POWDER, (byte) 1),
                        chance(Items.PURPLE_CONCRETE_POWDER, (byte) 1),
                        chance(Items.MAGENTA_CONCRETE_POWDER, (byte) 1),
                        chance(Items.PINK_CONCRETE_POWDER, (byte) 1)));
        var colorfulWool = ItemRandomizer.create(
                name("colorful_wool"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_WOOL, (byte) 1),
                        chance(Items.LIGHT_GRAY_WOOL, (byte) 1),
                        chance(Items.GRAY_WOOL, (byte) 1),
                        chance(Items.BLACK_WOOL, (byte) 1),
                        chance(Items.BROWN_WOOL, (byte) 1),
                        chance(Items.RED_WOOL, (byte) 1),
                        chance(Items.ORANGE_WOOL, (byte) 1),
                        chance(Items.YELLOW_WOOL, (byte) 1),
                        chance(Items.LIME_WOOL, (byte) 1),
                        chance(Items.GREEN_WOOL, (byte) 1),
                        chance(Items.CYAN_WOOL, (byte) 1),
                        chance(Items.LIGHT_BLUE_WOOL, (byte) 1),
                        chance(Items.BLUE_WOOL, (byte) 1),
                        chance(Items.PURPLE_WOOL, (byte) 1),
                        chance(Items.MAGENTA_WOOL, (byte) 1),
                        chance(Items.PINK_WOOL, (byte) 1)));
        var colorfulStainedGlass = ItemRandomizer.create(
                name("colorful_stained_glass"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_STAINED_GLASS, (byte) 1),
                        chance(Items.LIGHT_GRAY_STAINED_GLASS, (byte) 1),
                        chance(Items.GRAY_STAINED_GLASS, (byte) 1),
                        chance(Items.BLACK_STAINED_GLASS, (byte) 1),
                        chance(Items.BROWN_STAINED_GLASS, (byte) 1),
                        chance(Items.RED_STAINED_GLASS, (byte) 1),
                        chance(Items.ORANGE_STAINED_GLASS, (byte) 1),
                        chance(Items.YELLOW_STAINED_GLASS, (byte) 1),
                        chance(Items.LIME_STAINED_GLASS, (byte) 1),
                        chance(Items.GREEN_STAINED_GLASS, (byte) 1),
                        chance(Items.CYAN_STAINED_GLASS, (byte) 1),
                        chance(Items.LIGHT_BLUE_STAINED_GLASS, (byte) 1),
                        chance(Items.BLUE_STAINED_GLASS, (byte) 1),
                        chance(Items.PURPLE_STAINED_GLASS, (byte) 1),
                        chance(Items.MAGENTA_STAINED_GLASS, (byte) 1),
                        chance(Items.PINK_STAINED_GLASS, (byte) 1)));
        var colorfulStainedGlassPane = ItemRandomizer.create(
                name("colorful_stained_glass_pane"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.LIGHT_GRAY_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.GRAY_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.BLACK_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.BROWN_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.RED_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.ORANGE_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.YELLOW_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.LIME_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.GREEN_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.CYAN_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.LIGHT_BLUE_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.BLUE_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.PURPLE_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.MAGENTA_STAINED_GLASS_PANE, (byte) 1),
                        chance(Items.PINK_STAINED_GLASS_PANE, (byte) 1)));
        var colorfulTerracotta = ItemRandomizer.create(
                name("colorful_terracotta"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_TERRACOTTA, (byte) 1),
                        chance(Items.LIGHT_GRAY_TERRACOTTA, (byte) 1),
                        chance(Items.GRAY_TERRACOTTA, (byte) 1),
                        chance(Items.BLACK_TERRACOTTA, (byte) 1),
                        chance(Items.BROWN_TERRACOTTA, (byte) 1),
                        chance(Items.RED_TERRACOTTA, (byte) 1),
                        chance(Items.ORANGE_TERRACOTTA, (byte) 1),
                        chance(Items.YELLOW_TERRACOTTA, (byte) 1),
                        chance(Items.LIME_TERRACOTTA, (byte) 1),
                        chance(Items.GREEN_TERRACOTTA, (byte) 1),
                        chance(Items.CYAN_TERRACOTTA, (byte) 1),
                        chance(Items.LIGHT_BLUE_TERRACOTTA, (byte) 1),
                        chance(Items.BLUE_TERRACOTTA, (byte) 1),
                        chance(Items.PURPLE_TERRACOTTA, (byte) 1),
                        chance(Items.MAGENTA_TERRACOTTA, (byte) 1),
                        chance(Items.PINK_TERRACOTTA, (byte) 1)));
        var colorfulGlazedTerracotta = ItemRandomizer.create(
                name("colorful_glazed_terracotta"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.LIGHT_GRAY_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.GRAY_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.BLACK_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.BROWN_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.RED_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.ORANGE_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.YELLOW_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.LIME_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.GREEN_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.CYAN_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.LIGHT_BLUE_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.BLUE_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.PURPLE_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.MAGENTA_GLAZED_TERRACOTTA, (byte) 1),
                        chance(Items.PINK_GLAZED_TERRACOTTA, (byte) 1)));
        var colorfulShulkerBox = ItemRandomizer.create(
                name("colorful_shulker_box"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_SHULKER_BOX, (byte) 1),
                        chance(Items.LIGHT_GRAY_SHULKER_BOX, (byte) 1),
                        chance(Items.GRAY_SHULKER_BOX, (byte) 1),
                        chance(Items.BLACK_SHULKER_BOX, (byte) 1),
                        chance(Items.BROWN_SHULKER_BOX, (byte) 1),
                        chance(Items.RED_SHULKER_BOX, (byte) 1),
                        chance(Items.ORANGE_SHULKER_BOX, (byte) 1),
                        chance(Items.YELLOW_SHULKER_BOX, (byte) 1),
                        chance(Items.LIME_SHULKER_BOX, (byte) 1),
                        chance(Items.GREEN_SHULKER_BOX, (byte) 1),
                        chance(Items.CYAN_SHULKER_BOX, (byte) 1),
                        chance(Items.LIGHT_BLUE_SHULKER_BOX, (byte) 1),
                        chance(Items.BLUE_SHULKER_BOX, (byte) 1),
                        chance(Items.PURPLE_SHULKER_BOX, (byte) 1),
                        chance(Items.MAGENTA_SHULKER_BOX, (byte) 1),
                        chance(Items.PINK_SHULKER_BOX, (byte) 1)));
        var colorfulBed = ItemRandomizer.create(
                name("colorful_bed"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_BED, (byte) 1),
                        chance(Items.LIGHT_GRAY_BED, (byte) 1),
                        chance(Items.GRAY_BED, (byte) 1),
                        chance(Items.BLACK_BED, (byte) 1),
                        chance(Items.BROWN_BED, (byte) 1),
                        chance(Items.RED_BED, (byte) 1),
                        chance(Items.ORANGE_BED, (byte) 1),
                        chance(Items.YELLOW_BED, (byte) 1),
                        chance(Items.LIME_BED, (byte) 1),
                        chance(Items.GREEN_BED, (byte) 1),
                        chance(Items.CYAN_BED, (byte) 1),
                        chance(Items.LIGHT_BLUE_BED, (byte) 1),
                        chance(Items.BLUE_BED, (byte) 1),
                        chance(Items.PURPLE_BED, (byte) 1),
                        chance(Items.MAGENTA_BED, (byte) 1),
                        chance(Items.PINK_BED, (byte) 1)));
        var colorfulBanner = ItemRandomizer.create(
                name("colorful_banner"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        chance(Items.WHITE_BANNER, (byte) 1),
                        chance(Items.LIGHT_GRAY_BANNER, (byte) 1),
                        chance(Items.GRAY_BANNER, (byte) 1),
                        chance(Items.BLACK_BANNER, (byte) 1),
                        chance(Items.BROWN_BANNER, (byte) 1),
                        chance(Items.RED_BANNER, (byte) 1),
                        chance(Items.ORANGE_BANNER, (byte) 1),
                        chance(Items.YELLOW_BANNER, (byte) 1),
                        chance(Items.LIME_BANNER, (byte) 1),
                        chance(Items.GREEN_BANNER, (byte) 1),
                        chance(Items.CYAN_BANNER, (byte) 1),
                        chance(Items.LIGHT_BLUE_BANNER, (byte) 1),
                        chance(Items.BLUE_BANNER, (byte) 1),
                        chance(Items.PURPLE_BANNER, (byte) 1),
                        chance(Items.MAGENTA_BANNER, (byte) 1),
                        chance(Items.PINK_BANNER, (byte) 1)));

        return Stream.of(
                ItemRandomizer.EMPTY,
                variantCobblestone,
                variantStoneBrick,

                allOres,
                allDeepslateOres,

                colorfulCarpet,
                colorfulConcrete,
                colorfulConcretePowder,
                colorfulWool,
                colorfulStainedGlass,
                colorfulStainedGlassPane,
                colorfulTerracotta,
                colorfulGlazedTerracotta,
                colorfulShulkerBox,
                colorfulBed,
                colorfulBanner
        ).collect(Collectors.toList());
    }

    @Override
    public List<Transformer> getDefaultTransformers() {
        return Stream.of(
                List.of(ArrayTransformer.ZERO),
                List.of(MirrorTransformer.ZERO_X),
                List.of(MirrorTransformer.ZERO_Y),
                List.of(MirrorTransformer.ZERO_Z),
                List.of(RadialTransformer.ZERO),
                getDefaultRandomizers()
        ).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<Pattern> getDefaultPatterns() {
        return List.of(
                new Pattern(
                        Text.text("Simple Array Pattern"),
                        List.of(
                                new ArrayTransformer(new Vector3d(2, 0, 0), 2)
                        )
                )
        );
    }

    @Override
    public SearchTree<ItemStack> getItemsSearchTree(SearchBy searchBy) {
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
    public <T> SearchTree<T> createSearchTree(List<T> list, Function<T, Stream<Text>> keyExtractor) {
        return query -> PlainTextSearchTree.create(list, item -> keyExtractor.apply(item).map(text -> MinecraftClientAdapter.adapt(text).getString())).search(query);
    }

    @Override
    public Text empty() {
        return MinecraftAdapter.adapt(Component.empty());
    }

    @Override
    public Text text(String text) {
        return MinecraftAdapter.adapt(Component.literal(text));
    }

    @Override
    public Text text(String text, Text... args) {
        return MinecraftAdapter.adapt(Component.translatable(text, Arrays.stream(args).map(MinecraftAdapter::adapt).toArray(Object[]::new)));
    }

    @Override
    public Text translate(String text) {
        return MinecraftAdapter.adapt(Component.translatable(text));
    }

    @Override
    public Text translate(String text, Text... args) {
        return MinecraftAdapter.adapt(Component.translatable(text, Arrays.stream(args).map(MinecraftAdapter::adapt).toArray(Object[]::new)));
    }

}
