package dev.huskuraft.effortless.building.pattern.mirror;

import java.util.UUID;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.Range1i;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.GroupOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public record MirrorTransformer(UUID id, Text name, Vector3d position, Axis axis, int size) implements Transformer {

    public static final MirrorTransformer ZERO_X = new MirrorTransformer(Vector3d.ZERO, Axis.X);
    public static final MirrorTransformer ZERO_Y = new MirrorTransformer(Vector3d.ZERO, Axis.Y);
    public static final MirrorTransformer ZERO_Z = new MirrorTransformer(Vector3d.ZERO, Axis.Z);

    public static final MirrorTransformer DEFAULT_X = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.X);
    public static final MirrorTransformer DEFAULT_Y = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.Y);
    public static final MirrorTransformer DEFAULT_Z = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.Z);

    public static final Range1i SIZE_RANGE = new Range1i(1, 1024);
    public static final int DEFAULT_SIZE = 16;

    public MirrorTransformer(Vector3d position, Axis axis) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.mirror"), position, axis, DEFAULT_SIZE);
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
//        if (!isInBounds(operation.locate().getCenter())) {
//            return
//        }
        return new GroupOperation(operation.getContext(), Stream.of(
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
        return Stream.of(getName(), Text.text(position.toString()), Text.text(String.valueOf(axis)));
    }

    @Override
    public boolean isValid() {
        return POSITION_BOUND.containsIn(position) && SIZE_RANGE.contains(size);
    }

    @Override
    public MirrorTransformer finalize(Player player, BuildStage stage) {
        return this;
    }

    @Override
    public MirrorTransformer withName(Text name) {
        return new MirrorTransformer(id, name, position, axis, size);
    }

    @Override
    public MirrorTransformer withId(UUID id) {
        return new MirrorTransformer(id, name, position, axis, size);
    }

    public Vector3d position() {
        return position;
    }

    public double getPosition(Axis axis) {
        return switch (axis) {
            case X -> position.x();
            case Y -> position.y();
            case Z -> position.z();
        };
    }

    public Axis axis() {
        return axis;
    }

    public MirrorTransformer withPosition(Vector3d offset) {
        return new MirrorTransformer(id, name, offset, axis, size);
    }

    public MirrorTransformer withPositionX(double x) {
        return new MirrorTransformer(id, name, new Vector3d(x, position.y(), position.z()), axis, size);
    }

    public MirrorTransformer withPositionY(double y) {
        return new MirrorTransformer(id, name, new Vector3d(position.x(), y, position.z()), axis, size);
    }

    public MirrorTransformer withPositionZ(double z) {
        return new MirrorTransformer(id, name, new Vector3d(position.x(), position.y(), z), axis, size);
    }

    public MirrorTransformer withAxis(Axis axis) {
        return new MirrorTransformer(id, name, position, axis, size);
    }

    public MirrorTransformer withSize(int size) {
        return new MirrorTransformer(id, name, position, axis, size);
    }

}
