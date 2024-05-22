package dev.huskuraft.effortless.building.pattern.raidal;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Range1i;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RotateContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public record RadialTransformer(UUID id, Text name, Vector3d position, Axis axis, int slices, int radius) implements Transformer {

    public static final int DEFAULT_SLICE = 4;
    public static final int DEFAULT_RADIUS = 16;
    public static final Range1i SLICE_RANGE = new Range1i(1, 720);
    public static final Range1i RADIUS_RANGE = new Range1i(1, 1024);

    public static final RadialTransformer ZERO = new RadialTransformer(Vector3d.ZERO, 0, 128);
    public static final RadialTransformer DEFAULT = new RadialTransformer(Vector3d.ZERO, DEFAULT_SLICE, DEFAULT_RADIUS);


    //    private final double start;
//    private final Axis axis;
//    private final int count;
//    private final double step;
//    private final boolean clockwise;
//    private final boolean alternate;
//    private final boolean drawLines;
//    private final boolean drawPlanes;

    public RadialTransformer(Vector3d position, int slice, int radius) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.radial"), position, slice, radius);
    }

    public RadialTransformer(UUID id, Text name, Vector3d position, int slices, int radius) {
        this(id, name, position, Axis.Y, slices, radius);
    }

    @Override
    public BatchOperation transform(Operation operation) {
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
        return new RadialTransformer(id, name, position, axis, slices, radius);
    }

    public RadialTransformer withPositionX(double x) {
        return new RadialTransformer(id, name, new Vector3d(x, position.y(), position.z()), axis, slices, radius);
    }

    public RadialTransformer withPositionY(double y) {
        return new RadialTransformer(id, name, new Vector3d(position.x(), y, position.z()), axis, slices, radius);
    }

    public RadialTransformer withPositionZ(double z) {
        return new RadialTransformer(id, name, new Vector3d(position.x(), position.y(), z), axis, slices, radius);
    }

    public RadialTransformer withSlice(int slice) {
        return new RadialTransformer(id, name, position, axis, slice, radius);
    }

    public RadialTransformer withRadius(int radius) {
        return new RadialTransformer(id, name, position, axis, slices, radius);
    }

    @Override
    public Stream<Text> getSearchableTags() {
        return Stream.of(getName(), Text.text(position.toString()), Text.text(String.valueOf(slices)));
    }

    @Override
    public boolean isValid() {
        return POSITION_BOUND.containsIn(position) && SLICE_RANGE.contains(slices) && RADIUS_RANGE.contains(radius);
    }

    @Override
    public RadialTransformer finalize(Player player, BuildStage stage) {
        return this;
    }

    @Override
    public RadialTransformer withName(Text name) {
        return new RadialTransformer(id, name, position, axis, slices, radius);
    }

    @Override
    public RadialTransformer withId(UUID id) {
        return new RadialTransformer(id, name, position, axis, slices, radius);
    }


}
