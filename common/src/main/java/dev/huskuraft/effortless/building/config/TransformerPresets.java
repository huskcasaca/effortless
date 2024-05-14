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

public record TransformerPresets(
        List<Transformer> transformers
) {

    public static TransformerPresets getDefault() {
        return new TransformerPresets();
    }

    public TransformerPresets() {
        this(List.of());
    }


    public TransformerPresets(
            List<ArrayTransformer> arrayTransformers,
            List<MirrorTransformer> mirrorTransformers,
            List<RadialTransformer> radialTransformers,
            List<ItemRandomizer> itemRandomizers
    ) {
        this(Stream.of(arrayTransformers, mirrorTransformers, radialTransformers, itemRandomizers).flatMap(List::stream).collect(Collectors.toList()));
    }

    public List<ArrayTransformer> arrayTransformers() {
        return transformers().stream().filter(ArrayTransformer.class::isInstance).map(ArrayTransformer.class::cast).collect(Collectors.toList());
    }

    public List<MirrorTransformer> mirrorTransformers() {
        return transformers().stream().filter(MirrorTransformer.class::isInstance).map(MirrorTransformer.class::cast).collect(Collectors.toList());
    }

    public List<RadialTransformer> radialTransformers() {
        return transformers().stream().filter(RadialTransformer.class::isInstance).map(RadialTransformer.class::cast).collect(Collectors.toList());
    }

    public List<ItemRandomizer> itemRandomizers() {
        return transformers().stream().filter(ItemRandomizer.class::isInstance).map(ItemRandomizer.class::cast).collect(Collectors.toList());
    }

    public List<Transformer> getByType(Transformers type) {
        return transformers().stream().filter(t -> t.getType() == type).toList();
    }

    public Map<Transformers, List<Transformer>> getByType() {
        return Arrays.stream(Transformers.values()).collect(Collectors.toMap(Function.identity(), this::getByType));
    }

    public static TransformerPresets getBuiltInPresets() {
        return new TransformerPresets(
            Transformer.getDefaultTransformers()
        );
    }
}
