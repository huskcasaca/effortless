package dev.huskuraft.effortless.building.pattern.mirror;

import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class MirrorTransformer extends Transformer {

    public static final MirrorTransformer ZERO_X = new MirrorTransformer(Vector3d.ZERO, Axis.X);
    public static final MirrorTransformer ZERO_Y = new MirrorTransformer(Vector3d.ZERO, Axis.Y);
    public static final MirrorTransformer ZERO_Z = new MirrorTransformer(Vector3d.ZERO, Axis.Z);
    //    private final boolean enabled;

    private final Vector3d position;
    //    private final boolean drawLines;
//    private final boolean drawPlanes;
//    private final int radius;
    private final Axis axis;

    public MirrorTransformer(Vector3d position, Axis axis) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.mirror"), position, axis);
    }

    public MirrorTransformer(UUID id, Text name, Vector3d position, Axis axis) {
        super(id, name);
        this.position = position;
        this.axis = axis;
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        return new DeferredBatchOperation(operation.getContext(), () -> Stream.of(
                operation,
                operation.mirror(MirrorContext.of(position, axis))
        ));
    }

    @Override
    public Transformers getType() {
        return Transformers.MIRROR;
    }

    @Override
    public Stream<Text> getSearchableTags() {
        if (isValid()) {
            return Stream.of(Text.text("mirror"), Text.text(position.toString()), Text.text(String.valueOf(axis))); // TODO: 28/8/23
        } else {
            return Stream.of(Text.text("mirror"));
        }
    }

    @Override
    public boolean isValid() {
        return position != null && axis != null;
    }

    public Vector3d position() {
        return position;
    }

    public Axis axis() {
        return axis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MirrorTransformer that)) return false;
        if (!super.equals(o)) return false;

        if (!Objects.equals(position, that.position)) return false;
        return axis == that.axis;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (axis != null ? axis.hashCode() : 0);
        return result;
    }
}
