package dev.huskuraft.effortless.building.config;

import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ClipboardConfig(
        List<Snapshot> collections,
        List<Snapshot> history
) {

    public static ClipboardConfig DEFAULT = new ClipboardConfig();

    public ClipboardConfig() {
        this(List.of(), List.of());
    }

    public ClipboardConfig withCollections(List<Snapshot> collections) {
        return new ClipboardConfig(collections, history);
    }

    public ClipboardConfig withHistory(List<Snapshot> history) {
        return new ClipboardConfig(collections, history);
    }

    public ClipboardConfig appendHistory(Snapshot snapshot) {
        return new ClipboardConfig(collections, Stream.of(List.of(snapshot), history).flatMap(List::stream).toList());
    }

}
