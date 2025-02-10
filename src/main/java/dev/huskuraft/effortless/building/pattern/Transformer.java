package dev.huskuraft.effortless.building.pattern;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.Range1d;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public interface Transformer {

    Range1d POSITION_RANGE = new Range1d(-30000000, 30000000);
    int MAX_NAME_LENGTH = 255;

    static double roundHalf(double value) {
        return Math.round(value * 2) / 2.0;
    }

    static double floorHalf(double value) {
        return Math.floor(value) + 0.5;
    }

    static double round(double value) {
        return Math.round(value);
    }

    static Vector3d roundAllHalf(Vector3d vector3d) {
        if (roundHalf(vector3d.x()) % 1 != 0 || roundHalf(vector3d.y()) % 1 != 0 || roundHalf(vector3d.z()) % 1 != 0) {
            return new Vector3d(floorHalf(vector3d.x()), floorHalf(vector3d.y()), floorHalf(vector3d.z()));
        } else {
            return new Vector3d(round(vector3d.x()), round(vector3d.y()), round(vector3d.z()));
        }
    }


    static boolean isCenter(Vector3d vector3d) {
        var x = roundHalf(vector3d.x());
        var y = roundHalf(vector3d.y());
        var z = roundHalf(vector3d.z());
        return x % 1 != 0 || y % 1 != 0 || z % 1 != 0;
    }

    static Vector3d roundHalf(Vector3d vector3d) {
        var x = roundHalf(vector3d.x());
        var y = roundHalf(vector3d.y());
        var z = roundHalf(vector3d.z());
        if (x % 1 != 0 || y % 1 != 0 || z % 1 != 0) {
            return new Vector3d(Math.floor(vector3d.x()), Math.floor(vector3d.y()), Math.floor(vector3d.z()));
        } else {
            return new Vector3d(floorHalf(vector3d.x()), floorHalf(vector3d.y()), floorHalf(vector3d.z()));
        }
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

    Text getName();

    Operation transform(Operation operation);

    Transformers getType();

    Stream<Text> getSearchableTags();

    boolean isValid();

    Transformer withName(Text name);

    Transformer withId(UUID id);

    float volumeMultiplier();

    default boolean isBuiltIn() {
        return !name().getString().isEmpty();
    }

    default Transformer withRandomId() {
        return withId(UUID.randomUUID());
    }

    default Transformer finalize(Player player, BuildStage stage) {
        return this;
    }

    default List<Text> getDescriptions() {
        return List.of();
    }

}
