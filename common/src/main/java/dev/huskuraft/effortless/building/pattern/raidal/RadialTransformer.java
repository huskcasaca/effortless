package dev.huskuraft.effortless.building.pattern.raidal;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RevolveContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RadialTransformer extends Transformer {

    public static final RadialTransformer ZERO = new RadialTransformer(Vector3d.ZERO, 0);
    //    private final boolean enabled;
    private final Vector3d position;
    //    private final double start;
//    private final Axis axis;
//    private final int count;
//    private final double step;
//    private final boolean clockwise;
//    private final boolean alternate;
//    private final boolean drawLines;
//    private final boolean drawPlanes;
    private final int slice;

    public RadialTransformer(Vector3d position, int slice) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.radial"), position, slice);
    }

    public RadialTransformer(UUID id, Text name, Vector3d position, int slice) {
        super(id, name);
        this.position = position;
        this.slice = slice;
//        this.axis = axis;
//        this.count = count;
//        this.step = step;
//        this.clockwise = clockwise;
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        return new DeferredBatchOperation(operation.getContext(), () -> IntStream.range(1, slice).mapToObj(i -> {
            var angle = 2 * MathUtils.PI / slice * i;
            return operation.revolve(RevolveContext.absolute(position, angle));
        }));
    }

    @Override
    public Transformers getType() {
        return Transformers.RADIAL;
    }

    @Override
    public Stream<Text> getSearchableTags() {
        return Stream.of(getName(), Text.text(position.toString()), Text.text(String.valueOf(slice)));
    }

    @Override
    public boolean isValid() {
        return position != null && slice > 0;
    }

    public Vector3d position() {
        return position;
    }

    public int slices() {
        return slice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RadialTransformer that)) return false;
        if (!super.equals(o)) return false;

        if (slice != that.slice) return false;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + slice;
        return result;
    }
}
