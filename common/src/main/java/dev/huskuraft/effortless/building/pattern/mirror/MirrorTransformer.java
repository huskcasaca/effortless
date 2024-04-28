package dev.huskuraft.effortless.building.pattern.mirror;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BatchBuildSession;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;

public class MirrorTransformer extends Transformer {

    public static final MirrorTransformer ZERO_X = new MirrorTransformer(Vector3d.ZERO, Axis.X);
    public static final MirrorTransformer ZERO_Y = new MirrorTransformer(Vector3d.ZERO, Axis.Y);
    public static final MirrorTransformer ZERO_Z = new MirrorTransformer(Vector3d.ZERO, Axis.Z);

    public static final MirrorTransformer DEFAULT_X = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.X);
    public static final MirrorTransformer DEFAULT_Y = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.Y);
    public static final MirrorTransformer DEFAULT_Z = new MirrorTransformer(new Vector3d(0, 0, 0), Axis.Z);



    //    private final boolean enabled;

    private final Vector3d position;
    private final PositionType[] positionType;

    //    private final boolean drawLines;
//    private final boolean drawPlanes;
//    private final int radius;
    private final Axis axis;

    public MirrorTransformer(Vector3d position, Axis axis) {
        this(UUID.randomUUID(), Text.translate("effortless.transformer.mirror"), position, new PositionType[]{PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE}, axis);
    }

    public MirrorTransformer(UUID id, Text name, Vector3d position, PositionType[] positionType, Axis axis) {
        super(id, name);
        this.position = position;
        this.positionType = positionType;
        this.axis = axis;
    }

    public MirrorTransformer(UUID id, Text name, Vector3d position, PositionType positionTypeX, PositionType positionTypeY, PositionType positionTypeZ, Axis axis) {
        super(id, name);
        this.position = position;
        this.positionType = new PositionType[]{positionTypeX, positionTypeY, positionTypeZ};
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
        return Stream.of(getName(), Text.text(position.toString()), Text.text(String.valueOf(axis)));
    }

    @Override
    public boolean isValid() {
        return POSITION_BOUND.containsIn(position);
    }

    @Override
    public boolean isIntermediate() {
        return positionType[0].isIntermediate() || positionType[1].isIntermediate() || positionType[2].isIntermediate();
    }

    @Override
    public MirrorTransformer finalize(BatchBuildSession session, BuildStage stage) {
        return switch (stage) {
            case NONE -> this;
            case UPDATE_CONTEXT, INTERACT -> new MirrorTransformer(id, name, new Vector3d(
                    positionType[0].getStage() == stage ? position.x() + session.getPlayer().getPosition().toVector3i().x() : position.x(),
                    positionType[1].getStage() == stage ? position.y() + session.getPlayer().getPosition().toVector3i().y() : position.y(),
                    positionType[2].getStage() == stage ? position.z() + session.getPlayer().getPosition().toVector3i().z() : position.z()
            ), new PositionType[]{
                    positionType[0].getStage() == stage ? PositionType.ABSOLUTE : positionType[0],
                    positionType[1].getStage() == stage ? PositionType.ABSOLUTE : positionType[1],
                    positionType[2].getStage() == stage ? PositionType.ABSOLUTE : positionType[2]
            }, axis);
        };
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

    public PositionType[] getPositionType() {
        return positionType;
    }

    public PositionType getPositionType(Axis axis) {
        return positionType[axis.ordinal()];
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

    public Axis axis() {
        return axis;
    }

    public MirrorTransformer withPosition(Vector3d offset) {
        return new MirrorTransformer(id, name, offset, positionType, axis);
    }

    public MirrorTransformer withPositionX(double x) {
        return new MirrorTransformer(id, name, position.withX(x), positionType, axis);
    }

    public MirrorTransformer withPositionY(double y) {
        return new MirrorTransformer(id, name, position.withY(y), positionType, axis);
    }

    public MirrorTransformer withPositionZ(double z) {
        return new MirrorTransformer(id, name, position.withZ(z), positionType, axis);
    }

    public MirrorTransformer withPositionType(PositionType positionType) {
        return new MirrorTransformer(id, name, position, new PositionType[]{positionType, positionType, positionType}, axis);
    }

    public MirrorTransformer withPositionType(PositionType[] positionType) {
        return new MirrorTransformer(id, name, position, positionType, axis);
    }

    public MirrorTransformer withPositionTypeX(PositionType positionTypeX) {
        return new MirrorTransformer(id, name, position, new PositionType[]{positionTypeX, positionType[1], positionType[2]}, axis);
    }

    public MirrorTransformer withPositionTypeY(PositionType positionTypeY) {
        return new MirrorTransformer(id, name, position, new PositionType[]{positionType[0], positionTypeY, positionType[2]}, axis);
    }

    public MirrorTransformer withPositionTypeZ(PositionType positionTypeZ) {
        return new MirrorTransformer(id, name, position, new PositionType[]{positionType[0], positionType[1], positionTypeZ}, axis);
    }

    public MirrorTransformer withAxis(Axis axis) {
        return new MirrorTransformer(id, name, position, positionType, axis);
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
