package dev.huskuraft.effortless.building.pattern.randomize;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.Transformers;

public class ItemRandomizer extends Randomizer<Item> {

    public static final int MAX_CHANCE_SIZE = 36; // Inventory.INVENTORY_SIZE // FIXME: 23/10/23 move

    public static final ItemRandomizer EMPTY = ItemRandomizer.create(Text.translate("effortless.transformer.empty"), Order.SEQUENCE, Target.SINGLE, Category.ITEM, Collections.emptyList());

    private final Order order;
    private final Target target;
    private final Category category;

    private final Collection<Chance<Item>> chances;

    public ItemRandomizer(UUID uuid, Text name, Order order, Target target, Category category, Collection<Chance<Item>> chances) {
        super(uuid, name);
        this.order = order;
        this.target = target;
        this.category = category;
        this.chances = chances;
    }

    public static ItemRandomizer create(Text name, Order order, Target target, Category category, Collection<Chance<Item>> chances) {
        for (var chance : chances) {
            if (category != Randomizer.extract(chance.content())) {
                throw new IllegalArgumentException("All chances must be of the same category");
            }
        }
        return new ItemRandomizer(UUID.randomUUID(), name, order, target, category, chances);
    }

    public static ItemRandomizer create(Text name, Item content) {
        return create(name, Order.SEQUENCE, Target.SINGLE, Category.ITEM, List.of(Chance.of(content)));
    }

    private static Text name(String key) {
        return Text.translate("effortless.transformer.randomize.example.%s".formatted(key));
    }

    public static List<ItemRandomizer> getDefaultItemRandomizers() {

        var variantCobblestone = ItemRandomizer.create(
                name("variant_cobblestone"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.COBBLESTONE, (byte) 1),
                        Chance.of(Items.MOSSY_COBBLESTONE, (byte) 1)));
        var variantStoneBrick = ItemRandomizer.create(
                name("variant_stone_brick"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.STONE_BRICKS, (byte) 1),
                        Chance.of(Items.MOSSY_STONE_BRICKS, (byte) 1),
                        Chance.of(Items.CRACKED_STONE_BRICKS, (byte) 1)));
        var allOres = ItemRandomizer.create(
                name("all_ores"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.COAL_ORE, (byte) 1),
                        Chance.of(Items.COPPER_ORE, (byte) 1),
                        Chance.of(Items.LAPIS_ORE, (byte) 1),
                        Chance.of(Items.IRON_ORE, (byte) 1),
                        Chance.of(Items.REDSTONE_ORE, (byte) 1),
                        Chance.of(Items.GOLD_ORE, (byte) 1),
                        Chance.of(Items.DIAMOND_ORE, (byte) 1),
                        Chance.of(Items.EMERALD_ORE, (byte) 1)));
        var allDeepslateOres = ItemRandomizer.create(
                name("all_deepslate_ores"), Randomizer.Order.RANDOM, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.DEEPSLATE_COAL_ORE, (byte) 1),
                        Chance.of(Items.DEEPSLATE_COPPER_ORE, (byte) 1),
                        Chance.of(Items.DEEPSLATE_LAPIS_ORE, (byte) 1),
                        Chance.of(Items.DEEPSLATE_IRON_ORE, (byte) 1),
                        Chance.of(Items.DEEPSLATE_REDSTONE_ORE, (byte) 1),
                        Chance.of(Items.DEEPSLATE_GOLD_ORE, (byte) 1),
                        Chance.of(Items.DEEPSLATE_DIAMOND_ORE, (byte) 1),
                        Chance.of(Items.DEEPSLATE_EMERALD_ORE, (byte) 1)));
        var colorfulCarpet = ItemRandomizer.create(
                name("colorful_carpet"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_CARPET, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_CARPET, (byte) 1),
                        Chance.of(Items.GRAY_CARPET, (byte) 1),
                        Chance.of(Items.BLACK_CARPET, (byte) 1),
                        Chance.of(Items.BROWN_CARPET, (byte) 1),
                        Chance.of(Items.RED_CARPET, (byte) 1),
                        Chance.of(Items.ORANGE_CARPET, (byte) 1),
                        Chance.of(Items.YELLOW_CARPET, (byte) 1),
                        Chance.of(Items.LIME_CARPET, (byte) 1),
                        Chance.of(Items.GREEN_CARPET, (byte) 1),
                        Chance.of(Items.CYAN_CARPET, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_CARPET, (byte) 1),
                        Chance.of(Items.BLUE_CARPET, (byte) 1),
                        Chance.of(Items.PURPLE_CARPET, (byte) 1),
                        Chance.of(Items.MAGENTA_CARPET, (byte) 1),
                        Chance.of(Items.PINK_CARPET, (byte) 1)));
        var colorfulConcrete = ItemRandomizer.create(
                name("colorful_concrete"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_CONCRETE, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_CONCRETE, (byte) 1),
                        Chance.of(Items.GRAY_CONCRETE, (byte) 1),
                        Chance.of(Items.BLACK_CONCRETE, (byte) 1),
                        Chance.of(Items.BROWN_CONCRETE, (byte) 1),
                        Chance.of(Items.RED_CONCRETE, (byte) 1),
                        Chance.of(Items.ORANGE_CONCRETE, (byte) 1),
                        Chance.of(Items.YELLOW_CONCRETE, (byte) 1),
                        Chance.of(Items.LIME_CONCRETE, (byte) 1),
                        Chance.of(Items.GREEN_CONCRETE, (byte) 1),
                        Chance.of(Items.CYAN_CONCRETE, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_CONCRETE, (byte) 1),
                        Chance.of(Items.BLUE_CONCRETE, (byte) 1),
                        Chance.of(Items.PURPLE_CONCRETE, (byte) 1),
                        Chance.of(Items.MAGENTA_CONCRETE, (byte) 1),
                        Chance.of(Items.PINK_CONCRETE, (byte) 1)));
        var colorfulConcretePowder = ItemRandomizer.create(
                name("colorful_concrete_powder"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.GRAY_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.BLACK_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.BROWN_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.RED_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.ORANGE_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.YELLOW_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.LIME_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.GREEN_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.CYAN_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.BLUE_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.PURPLE_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.MAGENTA_CONCRETE_POWDER, (byte) 1),
                        Chance.of(Items.PINK_CONCRETE_POWDER, (byte) 1)));
        var colorfulWool = ItemRandomizer.create(
                name("colorful_wool"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_WOOL, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_WOOL, (byte) 1),
                        Chance.of(Items.GRAY_WOOL, (byte) 1),
                        Chance.of(Items.BLACK_WOOL, (byte) 1),
                        Chance.of(Items.BROWN_WOOL, (byte) 1),
                        Chance.of(Items.RED_WOOL, (byte) 1),
                        Chance.of(Items.ORANGE_WOOL, (byte) 1),
                        Chance.of(Items.YELLOW_WOOL, (byte) 1),
                        Chance.of(Items.LIME_WOOL, (byte) 1),
                        Chance.of(Items.GREEN_WOOL, (byte) 1),
                        Chance.of(Items.CYAN_WOOL, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_WOOL, (byte) 1),
                        Chance.of(Items.BLUE_WOOL, (byte) 1),
                        Chance.of(Items.PURPLE_WOOL, (byte) 1),
                        Chance.of(Items.MAGENTA_WOOL, (byte) 1),
                        Chance.of(Items.PINK_WOOL, (byte) 1)));
        var colorfulStainedGlass = ItemRandomizer.create(
                name("colorful_stained_glass"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.GRAY_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.BLACK_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.BROWN_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.RED_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.ORANGE_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.YELLOW_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.LIME_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.GREEN_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.CYAN_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.BLUE_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.PURPLE_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.MAGENTA_STAINED_GLASS, (byte) 1),
                        Chance.of(Items.PINK_STAINED_GLASS, (byte) 1)));
        var colorfulStainedGlassPane = ItemRandomizer.create(
                name("colorful_stained_glass_pane"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.GRAY_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.BLACK_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.BROWN_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.RED_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.ORANGE_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.YELLOW_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.LIME_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.GREEN_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.CYAN_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.BLUE_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.PURPLE_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.MAGENTA_STAINED_GLASS_PANE, (byte) 1),
                        Chance.of(Items.PINK_STAINED_GLASS_PANE, (byte) 1)));
        var colorfulTerracotta = ItemRandomizer.create(
                name("colorful_terracotta"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_TERRACOTTA, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_TERRACOTTA, (byte) 1),
                        Chance.of(Items.GRAY_TERRACOTTA, (byte) 1),
                        Chance.of(Items.BLACK_TERRACOTTA, (byte) 1),
                        Chance.of(Items.BROWN_TERRACOTTA, (byte) 1),
                        Chance.of(Items.RED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.ORANGE_TERRACOTTA, (byte) 1),
                        Chance.of(Items.YELLOW_TERRACOTTA, (byte) 1),
                        Chance.of(Items.LIME_TERRACOTTA, (byte) 1),
                        Chance.of(Items.GREEN_TERRACOTTA, (byte) 1),
                        Chance.of(Items.CYAN_TERRACOTTA, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_TERRACOTTA, (byte) 1),
                        Chance.of(Items.BLUE_TERRACOTTA, (byte) 1),
                        Chance.of(Items.PURPLE_TERRACOTTA, (byte) 1),
                        Chance.of(Items.MAGENTA_TERRACOTTA, (byte) 1),
                        Chance.of(Items.PINK_TERRACOTTA, (byte) 1)));
        var colorfulGlazedTerracotta = ItemRandomizer.create(
                name("colorful_glazed_terracotta"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.GRAY_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.BLACK_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.BROWN_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.RED_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.ORANGE_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.YELLOW_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.LIME_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.GREEN_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.CYAN_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.BLUE_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.PURPLE_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.MAGENTA_GLAZED_TERRACOTTA, (byte) 1),
                        Chance.of(Items.PINK_GLAZED_TERRACOTTA, (byte) 1)));
        var colorfulShulkerBox = ItemRandomizer.create(
                name("colorful_shulker_box"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.GRAY_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.BLACK_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.BROWN_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.RED_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.ORANGE_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.YELLOW_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.LIME_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.GREEN_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.CYAN_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.BLUE_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.PURPLE_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.MAGENTA_SHULKER_BOX, (byte) 1),
                        Chance.of(Items.PINK_SHULKER_BOX, (byte) 1)));
        var colorfulBed = ItemRandomizer.create(
                name("colorful_bed"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_BED, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_BED, (byte) 1),
                        Chance.of(Items.GRAY_BED, (byte) 1),
                        Chance.of(Items.BLACK_BED, (byte) 1),
                        Chance.of(Items.BROWN_BED, (byte) 1),
                        Chance.of(Items.RED_BED, (byte) 1),
                        Chance.of(Items.ORANGE_BED, (byte) 1),
                        Chance.of(Items.YELLOW_BED, (byte) 1),
                        Chance.of(Items.LIME_BED, (byte) 1),
                        Chance.of(Items.GREEN_BED, (byte) 1),
                        Chance.of(Items.CYAN_BED, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_BED, (byte) 1),
                        Chance.of(Items.BLUE_BED, (byte) 1),
                        Chance.of(Items.PURPLE_BED, (byte) 1),
                        Chance.of(Items.MAGENTA_BED, (byte) 1),
                        Chance.of(Items.PINK_BED, (byte) 1)));
        var colorfulBanner = ItemRandomizer.create(
                name("colorful_banner"), Randomizer.Order.SEQUENCE, Randomizer.Target.SINGLE, Randomizer.Category.ITEM,
                List.of(
                        Chance.of(Items.WHITE_BANNER, (byte) 1),
                        Chance.of(Items.LIGHT_GRAY_BANNER, (byte) 1),
                        Chance.of(Items.GRAY_BANNER, (byte) 1),
                        Chance.of(Items.BLACK_BANNER, (byte) 1),
                        Chance.of(Items.BROWN_BANNER, (byte) 1),
                        Chance.of(Items.RED_BANNER, (byte) 1),
                        Chance.of(Items.ORANGE_BANNER, (byte) 1),
                        Chance.of(Items.YELLOW_BANNER, (byte) 1),
                        Chance.of(Items.LIME_BANNER, (byte) 1),
                        Chance.of(Items.GREEN_BANNER, (byte) 1),
                        Chance.of(Items.CYAN_BANNER, (byte) 1),
                        Chance.of(Items.LIGHT_BLUE_BANNER, (byte) 1),
                        Chance.of(Items.BLUE_BANNER, (byte) 1),
                        Chance.of(Items.PURPLE_BANNER, (byte) 1),
                        Chance.of(Items.MAGENTA_BANNER, (byte) 1),
                        Chance.of(Items.PINK_BANNER, (byte) 1)));

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

    public ItemRandomizer withSequenceOrder() {
        return new ItemRandomizer(id, name, Order.SEQUENCE, target, category, chances);
    }

    public ItemRandomizer withRandomOrder() {
        return new ItemRandomizer(id, name, Order.RANDOM, target, category, chances);
    }

    public ItemRandomizer withSingleTarget() {
        return new ItemRandomizer(id, name, order, Target.SINGLE, category, chances);
    }

    public ItemRandomizer withGroupTarget() {
        return new ItemRandomizer(id, name, order, Target.GROUP, category, chances);
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
        return category;
    }

    @Override
    public Collection<Chance<Item>> getChances() {
        return chances;
    }

    @Override
    public Source<Item> asSource(long seed) {
        return switch (order) {
            case SEQUENCE -> Source.createSequence(this);
            case RANDOM -> Source.createUnordered(this, seed);
        };
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        if (!isValid()) {
            return new DeferredBatchOperation(operation.getContext(), () -> Stream.of(operation));
        }
        var source = asSource(operation.getContext().patternSeed());
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
        return Transformers.ITEM_RAND;
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
    public ItemRandomizer withName(Text name) {
        return new ItemRandomizer(id, name, order, target, category, chances);
    }

    @Override
    public ItemRandomizer withId(UUID id) {
        return new ItemRandomizer(id, name, order, target, category, chances);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRandomizer that)) return false;
        if (!super.equals(o)) return false;

        if (order != that.order) return false;
        if (target != that.target) return false;
        if (category != that.category) return false;
        return Objects.equals(chances, that.chances);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (chances != null ? chances.hashCode() : 0);
        return result;
    }
}
