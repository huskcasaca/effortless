package dev.huskuraft.effortless.building.pattern;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.Range1d;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public interface Transformer {

    Range1d POSITION_RANGE = new Range1d(-30000000, 30000000);
    int MAX_NAME_LENGTH = 255;

    static double roundToHalf(double value) {
        return Math.round(value * 2) / 2.0;
    }

    static Vector3d roundToHalf(Vector3d vector3d) {
        return new Vector3d(roundToHalf(vector3d.x()), roundToHalf(vector3d.y()), roundToHalf(vector3d.z()));
    }

    static List<Transformer> getDefaultTransformers() {
        return Stream.of(
                List.of(ArrayTransformer.DEFAULT),
                List.of(MirrorTransformer.DEFAULT_X, MirrorTransformer.DEFAULT_Y, MirrorTransformer.DEFAULT_Z),
                List.of(RadialTransformer.DEFAULT),
                ItemRandomizer.getDefaultItemRandomizers()
        ).flatMap(List::stream).collect(Collectors.toList());
    }

    BoundingBox3d POSITION_BOUND = BoundingBox3d.of(
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

    Operation transform(Operation operation);

    Transformers getType();

    Stream<Text> getSearchableTags();

    boolean isValid();

    Transformer withName(Text name);

    Transformer withId(UUID id);

    default boolean isBuiltIn() {
        return !name().getString().isEmpty();
    }

    default Transformer withRandomId() {
        return withId(UUID.randomUUID());
    }

    default Transformer finalize(Player player, BuildStage stage) {
        return this;
    }

}
