package dev.huskuraft.effortless.building.pattern.array;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import dev.huskuraft.universal.api.math.BoundingBox3i;
import dev.huskuraft.universal.api.math.Range1i;
import dev.huskuraft.universal.api.math.Vector3i;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public record ArrayTransformer(UUID id, Text name, Vector3i offset, int count) implements Transformer {

    public static final ArrayTransformer ZERO = new ArrayTransformer(new Vector3i(0, 0, 0), 0);
    public static final ArrayTransformer DEFAULT = new ArrayTransformer(new Vector3i(1, 1, 1), 4);

    public static final BoundingBox3i OFFSET_BOUND = BoundingBox3i.of(
            Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
            Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE
    );

    public static final Range1i COUNT_RANGE = new Range1i(1, Short.MAX_VALUE);

    public ArrayTransformer(Vector3i offset, int count) {
        this(UUID.randomUUID(), Text.empty(), offset, count);
    }

    @Override
    public Operation transform(Operation operation) {
        return new DeferredBatchOperation(operation.getContext(), () -> IntStream.range(0, count).mapToObj(i -> {
            return operation.move(MoveContext.relative(offset.mul(i)));
        }));
    }

    @Override
    public Text getName() {
        if (!name().getString().isEmpty()) {
            return name();
        }
        return Text.translate("effortless.transformer.array.no_name");
    }

    @Override
    public Transformers getType() {
        return Transformers.ARRAY;
    }

    @Override
    public Stream<Text> getSearchableTags() {
        return Stream.of(getName(), Text.text(offset().toString()), Text.text(String.valueOf(count)));
    }

    @Override
    public boolean isValid() {
        return OFFSET_BOUND.contains(offset) && COUNT_RANGE.contains(count);
    }

    @Override
    public ArrayTransformer withName(Text name) {
        return new ArrayTransformer(id, name, offset, count);
    }

    @Override
    public ArrayTransformer withId(UUID id) {
        return new ArrayTransformer(id, name, offset, count);
    }

    public Vector3i offset() {
        return offset;
    }

    public int count() {
        return count;
    }

    public Integer copyCount() {
        return Math.max(0, count - 1);
    }

    public ArrayTransformer withOffset(Vector3i offset) {
        return new ArrayTransformer(id, name, offset, count);
    }

    public ArrayTransformer withOffsetX(int x) {
        return new ArrayTransformer(id, name, offset.withX(x), count);
    }

    public ArrayTransformer withOffsetY(int y) {
        return new ArrayTransformer(id, name, offset.withY(y), count);
    }

    public ArrayTransformer withOffsetZ(int z) {
        return new ArrayTransformer(id, name, offset.withZ(z), count);
    }

    public ArrayTransformer withCount(int count) {
        return new ArrayTransformer(id, name, offset, count);
    }

    @Override
    public float volumeMultiplier() {
        return count;
    }

    @Override
    public List<Text> getDescriptions() {
        return List.of(Text.text("Offset " + offset.x() + " " + offset.y() + " " + offset.z()), Text.text("Count " + count));
    }
}
