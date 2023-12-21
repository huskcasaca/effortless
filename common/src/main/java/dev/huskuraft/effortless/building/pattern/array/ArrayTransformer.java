package dev.huskuraft.effortless.building.pattern.array;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.math.BoundingBox3d;
import dev.huskuraft.effortless.math.Range1i;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayTransformer extends Transformer {

    public static final ArrayTransformer ZERO = new ArrayTransformer(new Vector3d(0, 0, 0), 0);

    public static final BoundingBox3d OFFSET_BOUND = new BoundingBox3d(
            Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
            Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE
    );

    public static final Range1i COUNT_RANGE = new Range1i(0, Short.MAX_VALUE);

    public static final int MIN_COUNT = 0;
    public static final int MAX_COUNT = Short.MAX_VALUE;

    private final Vector3d offset;
    private final int count;

    public ArrayTransformer(Vector3d offset, int count) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.array"), offset, count);
    }

    public ArrayTransformer(UUID id, Text name, Vector3d offset, int count) {
        super(id, name);
        this.offset = offset;
        this.count = count;
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        return new DeferredBatchOperation(operation.getContext(), () -> IntStream.range(0, count).mapToObj(i -> {
            return operation.move(MoveContext.relative(offset.mul(i)));
        }));
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
        return OFFSET_BOUND.containsIn(offset) && COUNT_RANGE.contains(count);
    }

    @Override
    public boolean isIntermediate() {
        return false;
    }

    @Override
    public ArrayTransformer withName(Text name) {
        return new ArrayTransformer(id, name, offset, count);
    }

    @Override
    public ArrayTransformer withId(UUID id) {
        return new ArrayTransformer(id, name, offset, count);
    }

    public Vector3d offset() {
        return offset;
    }

    public Integer count() {
        return count;
    }

    public Integer copyCount() {
        return Math.max(0, count - 1);
    }

    public ArrayTransformer withOffset(Vector3d offset) {
        return new ArrayTransformer(id, name, offset, count);
    }

    public ArrayTransformer withOffsetX(double x) {
        return new ArrayTransformer(id, name, offset.withX(x), count);
    }

    public ArrayTransformer withOffsetY(double y) {
        return new ArrayTransformer(id, name, offset.withY(y), count);
    }

    public ArrayTransformer withOffsetZ(double z) {
        return new ArrayTransformer(id, name, offset.withZ(z), count);
    }

    public ArrayTransformer withCount(int count) {
        return new ArrayTransformer(id, name, offset, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayTransformer that)) return false;
        if (!super.equals(o)) return false;

        if (count != that.count) return false;
        return Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (offset != null ? offset.hashCode() : 0);
        result = 31 * result + count;
        return result;
    }

}
