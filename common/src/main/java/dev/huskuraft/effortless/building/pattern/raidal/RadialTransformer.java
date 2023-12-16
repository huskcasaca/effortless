package dev.huskuraft.effortless.building.pattern.raidal;

import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.RevolveContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.math.Range1i;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.text.Text;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RadialTransformer extends Transformer {

    public static final RadialTransformer ZERO = new RadialTransformer(Vector3d.ZERO, 0);

    public static final Range1i SLICE_RANGE = new Range1i(0, 720);

    //    private final boolean enabled;
    private final Vector3d position;
    private final PositionType[] positionType;


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
        this(UUID.randomUUID(), Text.translate("effortless.transformer.radial"), position, new PositionType[]{PositionType.ABSOLUTE, PositionType.ABSOLUTE, PositionType.ABSOLUTE}, slice);
    }

    public RadialTransformer(UUID id, Text name, Vector3d position, PositionType[] positionType, int slice) {
        super(id, name);
        this.position = position;
        this.positionType = positionType;
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

    public RadialTransformer withPositionX(double x) {
        return new RadialTransformer(id, name, position.withX(x), positionType, slice);
    }

    public RadialTransformer withPositionY(double y) {
        return new RadialTransformer(id, name, position.withY(y), positionType, slice);
    }

    public RadialTransformer withPositionZ(double z) {
        return new RadialTransformer(id, name, position.withZ(z), positionType, slice);
    }

    public RadialTransformer withPositionType(PositionType positionType) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionType, positionType, positionType}, slice);
    }

    public RadialTransformer withPositionType(PositionType[] positionType) {
        return new RadialTransformer(id, name, position, positionType, slice);
    }

    public RadialTransformer withPositionTypeX(PositionType positionTypeX) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionTypeX, positionType[1], positionType[2]}, slice);
    }

    public RadialTransformer withPositionTypeY(PositionType positionTypeY) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionType[0], positionTypeY, positionType[2]}, slice);
    }

    public RadialTransformer withPositionTypeZ(PositionType positionTypeZ) {
        return new RadialTransformer(id, name, position, new PositionType[]{positionType[0], positionType[1], positionTypeZ}, slice);
    }

    public RadialTransformer withSlice(int slice) {
        return new RadialTransformer(id, name, position, positionType, slice);
    }

    @Override
    public Stream<Text> getSearchableTags() {
        return Stream.of(getName(), Text.text(position.toString()), Text.text(String.valueOf(slice)));
    }

    @Override
    public boolean isValid() {
        return POSITION_BOUND.containsIn(position) && SLICE_RANGE.contains(slice);
    }

    @Override
    public boolean isIntermediate() {
        return positionType[0] != PositionType.ABSOLUTE || positionType[1] != PositionType.ABSOLUTE || positionType[2] != PositionType.ABSOLUTE;
    }

    public Vector3d position() {
        return position;
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
