package dev.huskuraft.effortless.building.pattern.mirror;

import java.util.UUID;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public record MirrorTransformer(
        UUID id, Text name, Vector3d position, PositionType positionType, Axis axis
) implements Transformer {

    public static final MirrorTransformer ZERO_X = new MirrorTransformer(Vector3d.ZERO, Axis.X);
    public static final MirrorTransformer ZERO_Y = new MirrorTransformer(Vector3d.ZERO, Axis.Y);
    public static final MirrorTransformer ZERO_Z = new MirrorTransformer(Vector3d.ZERO, Axis.Z);

    public static final MirrorTransformer DEFAULT_X = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.X);
    public static final MirrorTransformer DEFAULT_Y = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.Y);
    public static final MirrorTransformer DEFAULT_Z = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.Z);

    public MirrorTransformer(Vector3d position, Axis axis) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.mirror"), position, PositionType.RELATIVE, axis);
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        var realPosition = switch (positionType) {
            case ABSOLUTE -> position;
            case RELATIVE -> position.add(operation.getContext().patternParams().activeState().position()); // relative to player
        };
        return new DeferredBatchOperation(operation.getContext(), () -> Stream.of(
                operation,
                operation.mirror(MirrorContext.of(realPosition, axis))
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
        return POSITION_BOUND.containsIn(position);
    }

    @Override
    public boolean isIntermediate() {
        return positionType.isIntermediate();
    }

    @Override
    public MirrorTransformer finalize(Player player, BuildStage stage) {
        return this;
    }

    @Override
    public MirrorTransformer withName(Text name) {
        return new MirrorTransformer(id, name, position, positionType, axis);
    }

    @Override
    public MirrorTransformer withId(UUID id) {
        return new MirrorTransformer(id, name, position, positionType, axis);
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

    public PositionType getPositionType() {
        return positionType;
    }

    public Axis axis() {
        return axis;
    }

    public MirrorTransformer withPosition(Vector3d offset) {
        return new MirrorTransformer(id, name, offset, positionType, axis);
    }

    public MirrorTransformer withPositionType(PositionType positionType) {
        return new MirrorTransformer(id, name, position, positionType, axis);
    }

    public MirrorTransformer withAxis(Axis axis) {
        return new MirrorTransformer(id, name, position, positionType, axis);
    }

}
