package dev.huskuraft.effortless.building.pattern.randomize;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RefactorContext;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.core.ItemStack;
import dev.huskuraft.effortless.text.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ItemRandomizer implements Randomizer<ItemStack> {

    public static final ItemRandomizer EMPTY = ItemRandomizer.create("empty", Order.SEQUENCE, Target.SINGLE, Category.ITEM, Collections.emptyList());

    private final String name;
    private final Order order;
    private final Target target;
    private final Category category;

    private final Collection<Chance<ItemStack>> chances;

    public ItemRandomizer(String name, Order order, Target target, Category category, Collection<Chance<ItemStack>> chances) {
        this.name = name;
        this.order = order;
        this.target = target;
        this.category = category;
        this.chances = chances;
    }

    public static ItemRandomizer create(String name, Order order, Target target, Category category, Collection<Chance<ItemStack>> chances) {
        for (var chance : chances) {
            if (category != Randomizer.extract(chance.content())) {
                throw new IllegalArgumentException("All chances must be of the same category");
            }
        }
        return new ItemRandomizer(name, order, target, category, chances);
    }

    public static ItemRandomizer create(String name, ItemStack content) {
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
    public Collection<Chance<ItemStack>> getChances() {
        return chances;
    }

    @Override
    public Source<ItemStack> asSource(long seed) {
        return switch (order) {
            case SEQUENCE -> Source.createSequence(this);
            case RANDOM -> Source.createUnordered(this, seed);
        };
    }

    @Override
    public ItemRandomizer rename(String name) {
        return new ItemRandomizer(name, order, target, category, chances);
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
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
    public String getName() {
        return name;
    }

    @Override
    public Transformers getType() {
        return Transformers.ITEM_RANDOM;
    }

    @Override
    public Stream<Text> getSearchableTags() {
        return Stream.concat(
                Stream.of(
                        Text.text(name),
                        Text.translate(order.getNameKey()),
                        Text.translate(target.getNameKey()),
                        Text.translate(category.getNameKey())),
                chances.stream().map(Chance::content).map(itemStack -> itemStack.getHoverName())
        );
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
