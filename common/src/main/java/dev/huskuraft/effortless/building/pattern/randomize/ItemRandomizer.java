package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.text.Text;

import java.util.*;
import java.util.stream.Stream;

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
        var source = asSource(operation.getContext().uuid().getMostSignificantBits());
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
                        Text.translate(order.getNameKey()),
                        Text.translate(target.getNameKey()),
                        Text.translate(category.getNameKey())),
                chances.stream().map(Chance::content).map(item -> item.getDefaultStack().getHoverName())
        );
    }

    @Override
    public boolean isValid() {
        return !getChances().isEmpty();
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
