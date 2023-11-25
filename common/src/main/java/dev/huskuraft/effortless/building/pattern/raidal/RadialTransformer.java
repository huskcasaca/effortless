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

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RadialTransformer implements Transformer {

    public static final RadialTransformer EMPTY = new RadialTransformer(null, null);
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
    private final Integer slice;

    public RadialTransformer(Vector3d position, Integer slice) {
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
    public String getName() {
        return "radial";
    }

    @Override
    public Transformers getType() {
        return Transformers.RADIAL;
    }

    @Override
    public Stream<Text> getSearchableTags() {
        if (isValid()) {
            return Stream.of(Text.text("radial"), Text.text(position.toString()), Text.text(String.valueOf(slice)));
        } else {
            return Stream.of(Text.text("radial"));
        }
    }

    @Override
    public boolean isValid() {
        return position != null && slice > 0;
    }

    public Vector3d position() {
        return position;
    }

    public Integer slice() {
        return slice;
    }
}
