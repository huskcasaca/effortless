package dev.huskuraft.effortless.building.pattern.raidal;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Range1i;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RotateContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public record RadialTransformer(UUID id, Text name, Vector3d position, PositionType positionType, int slices) implements Transformer {

    public static final RadialTransformer ZERO = new RadialTransformer(Vector3d.ZERO, 0);

    public static final RadialTransformer DEFAULT = new RadialTransformer(Vector3d.ZERO, 4);

    public static final Range1i SLICE_RANGE = new Range1i(0, 720);
    //    private final double start;
//    private final Axis axis;
//    private final int count;
//    private final double step;
//    private final boolean clockwise;
//    private final boolean alternate;
//    private final boolean drawLines;
//    private final boolean drawPlanes;

    public RadialTransformer(Vector3d position, int slice) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.radial"), position, PositionType.RELATIVE, slice);
    }

    @Override
    public BatchOperation transform(TransformableOperation operation) {
        return new DeferredBatchOperation(operation.getContext(), () -> IntStream.range(0, slices).mapToObj(i -> {
            var angle = 2 * MathUtils.PI / slices * i;
            return operation.rotate(RotateContext.absolute(position, angle));
        }));
    }

    @Override
    public Transformers getType() {
        return Transformers.RADIAL;
    }

    public RadialTransformer withPosition(Vector3d position) {
        return new RadialTransformer(id, name, position, positionType, slices);
    }

    public RadialTransformer withPositionType(PositionType positionType) {
        return new RadialTransformer(id, name, position, positionType, slices);
    }

    public RadialTransformer withSlice(int slice) {
        return new RadialTransformer(id, name, position, positionType, slice);
    }

    @Override
    public Stream<Text> getSearchableTags() {
        return Stream.of(getName(), Text.text(position.toString()), Text.text(String.valueOf(slices)));
    }

    @Override
    public boolean isValid() {
        return POSITION_BOUND.containsIn(position) && SLICE_RANGE.contains(slices);
    }

    @Override
    public boolean isIntermediate() {
        return positionType.isIntermediate();
    }

    @Override
    public RadialTransformer finalize(Player player, BuildStage stage) {
        return switch (stage) {
            case TICK -> this;
            case UPDATE_CONTEXT, INTERACT -> {
                if (positionType.getStage() == stage) {
                    yield new RadialTransformer(id, name, position.add(player.getPosition()), PositionType.ABSOLUTE, slices);
                }
                yield this;
            }
        };
    }

    @Override
    public RadialTransformer withName(Text name) {
        return new RadialTransformer(id, name, position, positionType, slices);
    }

    @Override
    public RadialTransformer withId(UUID id) {
        return new RadialTransformer(id, name, position, positionType, slices);
    }

    public PositionType getPositionType() {
        return positionType;
    }

}
