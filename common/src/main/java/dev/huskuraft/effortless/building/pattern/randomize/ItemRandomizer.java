package dev.huskuraft.effortless.building.pattern.randomize;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockItem;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.Transformers;

public record ItemRandomizer(UUID id, Text name, Order order, Target target, Source source, Collection<Chance<Item>> chances) implements Randomizer<Item> {

    public static final int MAX_CHANCE_SIZE = 36; // Inventory.INVENTORY_SIZE // FIXME: 23/10/23 move

    public static final ItemRandomizer RANDOM_INVENTORY = ItemRandomizer.create(Text.translate("effortless.randomizer.item.random_inventory"), Order.RANDOM, Target.SINGLE,  Source.INVENTORY, Collections.emptyList());
    public static final ItemRandomizer RANDOM_HOTBAR = ItemRandomizer.create(Text.translate("effortless.randomizer.item.random_hotbar"), Order.RANDOM, Target.SINGLE,  Source.HOTBAR, Collections.emptyList());
    public static final ItemRandomizer RANDOM_HANDS = ItemRandomizer.create(Text.translate("effortless.randomizer.item.random_hands"), Order.RANDOM, Target.SINGLE,  Source.HANDS, Collections.emptyList());

    public static final ItemRandomizer SEQUENCE_INVENTORY = ItemRandomizer.create(Text.translate("effortless.randomizer.item.sequence_inventory"), Order.SEQUENCE, Target.SINGLE,  Source.INVENTORY, Collections.emptyList());
    public static final ItemRandomizer SEQUENCE_HOTBAR = ItemRandomizer.create(Text.translate("effortless.randomizer.item.sequence_hotbar"), Order.SEQUENCE, Target.SINGLE,  Source.HOTBAR, Collections.emptyList());
    public static final ItemRandomizer SEQUENCE_HANDS = ItemRandomizer.create(Text.translate("effortless.randomizer.item.sequence_hands"), Order.SEQUENCE, Target.SINGLE,  Source.HANDS, Collections.emptyList());

    public static final ItemRandomizer EMPTY = ItemRandomizer.create(Text.translate("effortless.randomizer.item.empty"), Order.SEQUENCE, Target.SINGLE,  Source.CUSTOMIZE, Collections.emptyList());

    public static ItemRandomizer customize(Text name, Order order, Target target, Collection<Chance<Item>> chances) {
        return create(name, order, target, Source.CUSTOMIZE, chances);
    }

    public static ItemRandomizer single(Text name, Item content) {
        return customize(name, Order.SEQUENCE, Target.SINGLE, List.of(Chance.of(content)));
    }

    public static ItemRandomizer create(Text name, Order order, Target target, Source source, Collection<Chance<Item>> chances) {
        var filtered = chances.stream().filter(chance -> chance.content() instanceof BlockItem).toList();
        return new ItemRandomizer(UUID.randomUUID(), name, order, target, source, filtered);
    }

    private static Text name(String key) {
        return Text.translate("effortless.transformer.randomize.example.%s".formatted(key));
    }

    public static List<ItemRandomizer> getDefaultItemRandomizers() {

        var variantCobblestone = ItemRandomizer.customize(
                name("variant_cobblestone"), Order.RANDOM, Target.SINGLE,
                List.of(
                        Chance.of(Items.COBBLESTONE.item(), (byte) 1),
                        Chance.of(Items.MOSSY_COBBLESTONE.item(), (byte) 1)));
        var variantStoneBrick = ItemRandomizer.customize(
                name("variant_stone_brick"), Order.RANDOM, Target.SINGLE,
                List.of(
                        Chance.of(Items.STONE_BRICKS.item(), (byte) 1),
                        Chance.of(Items.MOSSY_STONE_BRICKS.item(), (byte) 1),
                        Chance.of(Items.CRACKED_STONE_BRICKS.item(), (byte) 1)));
        var allOres = ItemRandomizer.customize(
                name("all_ores"), Order.RANDOM, Target.SINGLE,
                List.of(
                        Chance.of(Items.COAL_ORE.item(), (byte) 1),
                        Chance.of(Items.COPPER_ORE.item(), (byte) 1),
                        Chance.of(Items.LAPIS_ORE.item(), (byte) 1),
                        Chance.of(Items.IRON_ORE.item(), (byte) 1),
                        Chance.of(Items.REDSTONE_ORE.item(), (byte) 1),
                        Chance.of(Items.GOLD_ORE.item(), (byte) 1),
                        Chance.of(Items.DIAMOND_ORE.item(), (byte) 1),
                        Chance.of(Items.EMERALD_ORE.item(), (byte) 1)));
        var allDeepslateOres = ItemRandomizer.customize(
                name("all_deepslate_ores"), Order.RANDOM, Target.SINGLE,
                List.of(
                        Chance.of(Items.DEEPSLATE_COAL_ORE.item(), (byte) 1),
                        Chance.of(Items.DEEPSLATE_COPPER_ORE.item(), (byte) 1),
                        Chance.of(Items.DEEPSLATE_LAPIS_ORE.item(), (byte) 1),
                        Chance.of(Items.DEEPSLATE_IRON_ORE.item(), (byte) 1),
                        Chance.of(Items.DEEPSLATE_REDSTONE_ORE.item(), (byte) 1),
                        Chance.of(Items.DEEPSLATE_GOLD_ORE.item(), (byte) 1),
                        Chance.of(Items.DEEPSLATE_DIAMOND_ORE.item(), (byte) 1),
                        Chance.of(Items.DEEPSLATE_EMERALD_ORE.item(), (byte) 1)));
        var colorfulCarpet = ItemRandomizer.customize(
                name("colorful_carpet"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_CARPET.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_CARPET.item(), (byte) 1),
                        Chance.of(Items.GRAY_CARPET.item(), (byte) 1),
                        Chance.of(Items.BLACK_CARPET.item(), (byte) 1),
                        Chance.of(Items.BROWN_CARPET.item(), (byte) 1),
                        Chance.of(Items.RED_CARPET.item(), (byte) 1),
                        Chance.of(Items.ORANGE_CARPET.item(), (byte) 1),
                        Chance.of(Items.YELLOW_CARPET.item(), (byte) 1),
                        Chance.of(Items.LIME_CARPET.item(), (byte) 1),
                        Chance.of(Items.GREEN_CARPET.item(), (byte) 1),
                        Chance.of(Items.CYAN_CARPET.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_CARPET.item(), (byte) 1),
                        Chance.of(Items.BLUE_CARPET.item(), (byte) 1),
                        Chance.of(Items.PURPLE_CARPET.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_CARPET.item(), (byte) 1),
                        Chance.of(Items.PINK_CARPET.item(), (byte) 1)));
        var colorfulConcrete = ItemRandomizer.customize(
                name("colorful_concrete"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.GRAY_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.BLACK_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.BROWN_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.RED_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.ORANGE_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.YELLOW_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.LIME_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.GREEN_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.CYAN_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.BLUE_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.PURPLE_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_CONCRETE.item(), (byte) 1),
                        Chance.of(Items.PINK_CONCRETE.item(), (byte) 1)));
        var colorfulConcretePowder = ItemRandomizer.customize(
                name("colorful_concrete_powder"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.GRAY_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.BLACK_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.BROWN_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.RED_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.ORANGE_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.YELLOW_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.LIME_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.GREEN_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.CYAN_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.BLUE_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.PURPLE_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_CONCRETE_POWDER.item(), (byte) 1),
                        Chance.of(Items.PINK_CONCRETE_POWDER.item(), (byte) 1)));
        var colorfulWool = ItemRandomizer.customize(
                name("colorful_wool"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_WOOL.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_WOOL.item(), (byte) 1),
                        Chance.of(Items.GRAY_WOOL.item(), (byte) 1),
                        Chance.of(Items.BLACK_WOOL.item(), (byte) 1),
                        Chance.of(Items.BROWN_WOOL.item(), (byte) 1),
                        Chance.of(Items.RED_WOOL.item(), (byte) 1),
                        Chance.of(Items.ORANGE_WOOL.item(), (byte) 1),
                        Chance.of(Items.YELLOW_WOOL.item(), (byte) 1),
                        Chance.of(Items.LIME_WOOL.item(), (byte) 1),
                        Chance.of(Items.GREEN_WOOL.item(), (byte) 1),
                        Chance.of(Items.CYAN_WOOL.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_WOOL.item(), (byte) 1),
                        Chance.of(Items.BLUE_WOOL.item(), (byte) 1),
                        Chance.of(Items.PURPLE_WOOL.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_WOOL.item(), (byte) 1),
                        Chance.of(Items.PINK_WOOL.item(), (byte) 1)));
        var colorfulStainedGlass = ItemRandomizer.customize(
                name("colorful_stained_glass"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.GRAY_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.BLACK_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.BROWN_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.RED_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.ORANGE_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.YELLOW_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.LIME_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.GREEN_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.CYAN_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.BLUE_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.PURPLE_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_STAINED_GLASS.item(), (byte) 1),
                        Chance.of(Items.PINK_STAINED_GLASS.item(), (byte) 1)));
        var colorfulStainedGlassPane = ItemRandomizer.customize(
                name("colorful_stained_glass_pane"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.GRAY_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.BLACK_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.BROWN_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.RED_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.ORANGE_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.YELLOW_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.LIME_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.GREEN_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.CYAN_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.BLUE_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.PURPLE_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_STAINED_GLASS_PANE.item(), (byte) 1),
                        Chance.of(Items.PINK_STAINED_GLASS_PANE.item(), (byte) 1)));
        var colorfulTerracotta = ItemRandomizer.customize(
                name("colorful_terracotta"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.GRAY_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.BLACK_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.BROWN_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.RED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.ORANGE_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.YELLOW_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.LIME_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.GREEN_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.CYAN_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.BLUE_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.PURPLE_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.PINK_TERRACOTTA.item(), (byte) 1)));
        var colorfulGlazedTerracotta = ItemRandomizer.customize(
                name("colorful_glazed_terracotta"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.GRAY_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.BLACK_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.BROWN_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.RED_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.ORANGE_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.YELLOW_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.LIME_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.GREEN_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.CYAN_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.BLUE_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.PURPLE_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_GLAZED_TERRACOTTA.item(), (byte) 1),
                        Chance.of(Items.PINK_GLAZED_TERRACOTTA.item(), (byte) 1)));
        var colorfulShulkerBox = ItemRandomizer.customize(
                name("colorful_shulker_box"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.GRAY_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.BLACK_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.BROWN_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.RED_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.ORANGE_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.YELLOW_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.LIME_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.GREEN_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.CYAN_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.BLUE_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.PURPLE_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_SHULKER_BOX.item(), (byte) 1),
                        Chance.of(Items.PINK_SHULKER_BOX.item(), (byte) 1)));
        var colorfulBed = ItemRandomizer.customize(
                name("colorful_bed"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_BED.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_BED.item(), (byte) 1),
                        Chance.of(Items.GRAY_BED.item(), (byte) 1),
                        Chance.of(Items.BLACK_BED.item(), (byte) 1),
                        Chance.of(Items.BROWN_BED.item(), (byte) 1),
                        Chance.of(Items.RED_BED.item(), (byte) 1),
                        Chance.of(Items.ORANGE_BED.item(), (byte) 1),
                        Chance.of(Items.YELLOW_BED.item(), (byte) 1),
                        Chance.of(Items.LIME_BED.item(), (byte) 1),
                        Chance.of(Items.GREEN_BED.item(), (byte) 1),
                        Chance.of(Items.CYAN_BED.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_BED.item(), (byte) 1),
                        Chance.of(Items.BLUE_BED.item(), (byte) 1),
                        Chance.of(Items.PURPLE_BED.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_BED.item(), (byte) 1),
                        Chance.of(Items.PINK_BED.item(), (byte) 1)));
        var colorfulBanner = ItemRandomizer.customize(
                name("colorful_banner"), Order.SEQUENCE, Target.SINGLE,
                List.of(
                        Chance.of(Items.WHITE_BANNER.item(), (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_BANNER.item(), (byte) 1),
                        Chance.of(Items.GRAY_BANNER.item(), (byte) 1),
                        Chance.of(Items.BLACK_BANNER.item(), (byte) 1),
                        Chance.of(Items.BROWN_BANNER.item(), (byte) 1),
                        Chance.of(Items.RED_BANNER.item(), (byte) 1),
                        Chance.of(Items.ORANGE_BANNER.item(), (byte) 1),
                        Chance.of(Items.YELLOW_BANNER.item(), (byte) 1),
                        Chance.of(Items.LIME_BANNER.item(), (byte) 1),
                        Chance.of(Items.GREEN_BANNER.item(), (byte) 1),
                        Chance.of(Items.CYAN_BANNER.item(), (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_BANNER.item(), (byte) 1),
                        Chance.of(Items.BLUE_BANNER.item(), (byte) 1),
                        Chance.of(Items.PURPLE_BANNER.item(), (byte) 1),
                        Chance.of(Items.MAGENTA_BANNER.item(), (byte) 1),
                        Chance.of(Items.PINK_BANNER.item(), (byte) 1)));

        return Stream.of(
                ItemRandomizer.EMPTY,
                ItemRandomizer.RANDOM_INVENTORY,
                ItemRandomizer.RANDOM_HOTBAR,
                ItemRandomizer.RANDOM_HANDS,
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

    public ItemRandomizer withOrder(Order order) {
        return new ItemRandomizer(id, name, order, target, source, chances);
    }

    public ItemRandomizer withTarget(Target target) {
        return new ItemRandomizer(id, name, order, target, source, chances);
    }

    public ItemRandomizer withSource(Source source) {
        return new ItemRandomizer(id, name, order, target, source, chances);
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public Target getTarget() {
        return target;
    }

    @Override
    public Category getCategory() {
        return Category.ITEM;
    }

    public Source getSource() {
        return source;
    }

    @Override
    public Collection<Chance<Item>> getChances() {
        return chances;
    }

    @Override
    public Producer<Item> asProducer(long seed, boolean limitedProducer) {
        return switch (order) {
            case SEQUENCE -> Producer.createSequence(this, seed, limitedProducer && getSource() != Source.CUSTOMIZE);
            case RANDOM -> Producer.createUnordered(this, seed, limitedProducer && getSource() != Source.CUSTOMIZE);
        };
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        if (!isValid()) {
            return new DeferredBatchOperation(operation.getContext(), () -> Stream.of(operation));
        }
        var source = asProducer(operation.getContext().patternParams().seed(), operation.getContext().patternParams().limitedProducer());
        if (operation instanceof DeferredBatchOperation deferredBatchOperation) {
            return switch (target) {
                case SINGLE -> deferredBatchOperation.mapEach(o -> o.refactor(RefactorContext.of(source.next())));
                case GROUP -> deferredBatchOperation.map(o -> o.refactor(RefactorContext.of(source.next())));
            };
        } else {
            return new DeferredBatchOperation(operation.getContext(), () -> Stream.of(operation.refactor(RefactorContext.of(source.next()))));
        }
    }

    @Override
    public Transformers getType() {
        return Transformers.ITEM_RANDOMIZER;
    }

    @Override
    public Stream<Text> getSearchableTags() {
        return Stream.concat(
                Stream.of(
                        getName(),
                        order.getDisplayName(),
                        target.getDisplayName(),
                        category.getDisplayName()),
                chances.stream().map(Chance::content).map(item -> item.getDefaultStack().getHoverName())
        );
    }

    @Override
    public boolean isValid() {
        return !getChances().isEmpty();
    }

    @Override
    public boolean isIntermediate() {
        return false;
    }

    @Override
    public ItemRandomizer withRandomId() {
        return withId(UUID.randomUUID());
    }

    @Override
    public ItemRandomizer withName(Text name) {
        return new ItemRandomizer(id, name, order, target, source, chances);
    }

    @Override
    public ItemRandomizer withId(UUID id) {
        return new ItemRandomizer(id, name, order, target, source, chances);
    }

    public ItemRandomizer withChances(Collection<Chance<Item>> items) {
        return new ItemRandomizer(id, name, order, target, source, items.stream().filter(chance -> chance.content() instanceof BlockItem).toList());
    }

    @Override
    public ItemRandomizer finalize(Player player, BuildStage stage) {

        switch (stage) {
            case TICK, UPDATE_CONTEXT, INTERACT -> {
                if (getSource() != Source.CUSTOMIZE) {
                    var itemStacks = switch (getSource()) {
                        case INVENTORY -> Stream.of(player.getInventory().getItems(), player.getInventory().getOffhandItems()).flatMap(List::stream).toList();
                        case HOTBAR -> player.getInventory().getHotbarItems();
                        case HANDS -> List.of(player.getInventory().getSelectedItem(), player.getInventory().getOffhandItem());
                        case CUSTOMIZE -> List.of(ItemStack.empty());
                    };
                    return withChances(itemStacks.stream().filter(itemStack -> !itemStack.isAir()).map(itemStack -> Chance.of(itemStack.getItem(), itemStack.getCount())).toList());
                }
            }
        }
        return this;
    }

    public enum Source {
        INVENTORY("inventory"),
        HOTBAR("hotbar"),
        HANDS("hands"),
        CUSTOMIZE("customize"),;

        private final String name;

        Source(String name) {
            this.name = name;
        }

        public Text getDisplayName() {
            return Text.translate("effortless.randomizer.source.%s".formatted(name));
        }

        public String getName() {
            return name;
        }
    }


}
