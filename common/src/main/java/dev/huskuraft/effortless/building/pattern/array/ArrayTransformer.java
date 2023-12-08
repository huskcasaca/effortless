package dev.huskuraft.effortless.building.pattern.array;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArrayTransformer extends Transformer {

    public static final ArrayTransformer ZERO = new ArrayTransformer(new Vector3d(0, 0, 0), 0);

    private final double x;
    private final double y;
    private final double z;
    private final int count;

    public ArrayTransformer(Vector3d offset, int count) {
        this(offset.getX(), offset.getY(), offset.getZ(), count);
    }

    public ArrayTransformer(UUID id, Text name, Vector3d offset, int count) {
        this(id, name, offset.getX(), offset.getY(), offset.getZ(), count);
    }

    public ArrayTransformer(double x, double y, double z, int count) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.array"), x, y, z, count);
    }

    public ArrayTransformer(UUID id, Text name, double x, double y, double z, int count) {
        super(id, name);
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        return new DeferredBatchOperation(operation.getContext(), () -> IntStream.range(0, count).mapToObj(i -> {
            return operation.move(MoveContext.relative(x * i, y * i, z * i));
        }));
    }

    @Override
    public Transformers getType() {
        return Transformers.ARRAY;
    }

    @Override
    public Stream<Text> getSearchableTags() {
        if (isValid()) {
            return Stream.of(Text.text("array"), Text.text(offset().toString()), Text.text(String.valueOf(count)));
        } else {
            return Stream.of(Text.text("array"));
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public Vector3d offset() {
        return new Vector3d(x, y, z);
    }

    public Double x() {
        return x;
    }

    public Double y() {
        return y;
    }

    public Double z() {
        return z;
    }

    public Integer count() {
        return count;
    }

    public ArrayTransformer withOffsetX(double x) {
        return new ArrayTransformer(x, y, z, count);
    }

    public ArrayTransformer withOffsetY(double y) {
        return new ArrayTransformer(x, y, z, count);
    }

    public ArrayTransformer withOffsetZ(double z) {
        return new ArrayTransformer(x, y, z, count);
    }

    public ArrayTransformer withCount(int count) {
        return new ArrayTransformer(x, y, z, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayTransformer that)) return false;
        if (!super.equals(o)) return false;

        if (Double.compare(that.x, x) != 0) return false;
        if (Double.compare(that.y, y) != 0) return false;
        if (Double.compare(that.z, z) != 0) return false;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + count;
        return result;
    }
}
