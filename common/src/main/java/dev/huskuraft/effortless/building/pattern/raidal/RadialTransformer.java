package dev.huskuraft.effortless.building.pattern.raidal;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
import dev.huskuraft.effortless.building.session.BatchBuildSession;

public record RadialTransformer(UUID id, Text name, Vector3d position, PositionType[] positionType, int slices) implements Transformer {

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
        this(UUID.randomUUID(), Text.translate("effortless.transformer.radial"), position, new PositionType[]{PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE}, slice);
    }

    public RadialTransformer(UUID id, Text name, Vector3d position, PositionType positionTypeX, PositionType positionTypeY, PositionType positionTypeZ, int slice) {
        this(id, name, position, new PositionType[]{positionTypeX, positionTypeY, positionTypeZ}, slice);
//        this.axis = axis;
//        this.count = count;
//        this.step = step;
//        this.clockwise = clockwise;
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

    public RadialTransformer withPositionX(double x) {
        return new RadialTransformer(id, name, position.withX(x), positionType, slices);
    }

    public RadialTransformer withPositionY(double y) {
        return new RadialTransformer(id, name, position.withY(y), positionType, slices);
    }

    public RadialTransformer withPositionZ(double z) {
        return new RadialTransformer(id, name, position.withZ(z), positionType, slices);
    }

    public RadialTransformer withPositionType(PositionType positionType) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionType, positionType, positionType}, slices);
    }

    public RadialTransformer withPositionType(PositionType[] positionType) {
        return new RadialTransformer(id, name, position, positionType, slices);
    }

    public RadialTransformer withPositionTypeX(PositionType positionTypeX) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionTypeX, positionType[1], positionType[2]}, slices);
    }

    public RadialTransformer withPositionTypeY(PositionType positionTypeY) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionType[0], positionTypeY, positionType[2]}, slices);
    }

    public RadialTransformer withPositionTypeZ(PositionType positionTypeZ) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionType[0], positionType[1], positionTypeZ}, slices);
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
        return positionType[0] != PositionType.ABSOLUTE || positionType[1] != PositionType.ABSOLUTE || positionType[2] != PositionType.ABSOLUTE;
    }


    @Override
    public RadialTransformer finalize(BatchBuildSession session, BuildStage stage) {
        return switch (stage) {
            case NONE -> this;
            case UPDATE_CONTEXT, INTERACT -> new RadialTransformer(id, name, new Vector3d(
                    positionType[0].getStage() == stage ? position.x() + session.getPlayer().getPosition().toVector3i().x() : position.x(),
                    positionType[1].getStage() == stage ? position.y() + session.getPlayer().getPosition().toVector3i().y() : position.y(),
                    positionType[2].getStage() == stage ? position.z() + session.getPlayer().getPosition().toVector3i().z() : position.z()
            ), new PositionType[]{
                    positionType[0].getStage() == stage ? PositionType.ABSOLUTE : positionType[0],
                    positionType[1].getStage() == stage ? PositionType.ABSOLUTE : positionType[1],
                    positionType[2].getStage() == stage ? PositionType.ABSOLUTE : positionType[2]
            }, slices);
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

    public PositionType[] getPositionType() {
        return positionType;
    }

    public PositionType getPositionTypeX() {
        return positionType[0];
    }

    public PositionType getPositionTypeY() {
        return positionType[1];
    }

    public PositionType getPositionTypeZ() {
        return positionType[2];
    }

}
