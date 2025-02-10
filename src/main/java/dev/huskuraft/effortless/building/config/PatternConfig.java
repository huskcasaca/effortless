package dev.huskuraft.effortless.building.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public record PatternConfig(
        List<? extends Transformer> transformerPreset
) {

    public static PatternConfig DEFAULT = new PatternConfig();

    public PatternConfig() {
        this(List.of());
    }


    public PatternConfig(
            List<ArrayTransformer> arrayTransformers,
            List<MirrorTransformer> mirrorTransformers,
            List<RadialTransformer> radialTransformers,
            List<ItemRandomizer> itemRandomizers
    ) {
        this(Stream.of(arrayTransformers, mirrorTransformers, radialTransformers, itemRandomizers).flatMap(List::stream).collect(Collectors.toList()));
    }

    public List<ArrayTransformer> arrayTransformers() {
        return transformerPreset().stream().filter(ArrayTransformer.class::isInstance).map(ArrayTransformer.class::cast).collect(Collectors.toList());
    }

    public List<MirrorTransformer> mirrorTransformers() {
        return transformerPreset().stream().filter(MirrorTransformer.class::isInstance).map(MirrorTransformer.class::cast).collect(Collectors.toList());
    }

    public List<RadialTransformer> radialTransformers() {
        return transformerPreset().stream().filter(RadialTransformer.class::isInstance).map(RadialTransformer.class::cast).collect(Collectors.toList());
    }

    public List<ItemRandomizer> itemRandomizers() {
        return transformerPreset().stream().filter(ItemRandomizer.class::isInstance).map(ItemRandomizer.class::cast).collect(Collectors.toList());
    }

    public List<? extends Transformer> getByType(Transformers type) {
        return transformerPreset().stream().filter(t -> t.getType() == type).toList();
    }

    public Map<Transformers, List<? extends Transformer>> getByType() {
        return Arrays.stream(Transformers.values()).collect(Collectors.toMap(Function.identity(), this::getByType));
    }

    public static PatternConfig getBuiltInPresets() {
        return new PatternConfig(
            Transformer.getDefaultTransformers()
        );
    }
}
