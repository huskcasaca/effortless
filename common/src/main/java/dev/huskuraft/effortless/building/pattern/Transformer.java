package dev.huskuraft.effortless.building.pattern;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.operation.TransformableOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.session.BatchBuildSession;

public interface Transformer {

    int MAX_NAME_LENGTH = 255;

    static List<Transformer> getDefaultTransformers() {
        return Stream.of(
                List.of(ArrayTransformer.DEFAULT),
                List.of(MirrorTransformer.DEFAULT_X, MirrorTransformer.DEFAULT_Y, MirrorTransformer.DEFAULT_Z),
                List.of(RadialTransformer.DEFAULT),
                ItemRandomizer.getDefaultItemRandomizers()
        ).flatMap(List::stream).collect(Collectors.toList());
    }

    BoundingBox3d POSITION_BOUND = new BoundingBox3d(
            Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE,
            Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE
    );

    UUID id();

    Text name();

    default UUID getId() {
        return id();
    }

    default Text getName() {
        return name();
    }

    BatchOperation transform(TransformableOperation operation);

    Transformers getType();

    Stream<Text> getSearchableTags();

    boolean isValid();

    boolean isIntermediate();

    Transformer withName(Text name);

    Transformer withId(UUID id);

    default Transformer withRandomId() {
        return withId(UUID.randomUUID());
    }

    default Transformer finalize(BatchBuildSession session, BuildStage stage) {
        return this;
    }


}
