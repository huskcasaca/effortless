package dev.huskuraft.effortless.building.config;

import java.util.List;

import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public record TransformerPresets(
        List<ArrayTransformer> arrayTransformers,
        List<MirrorTransformer> mirrorTransformers,
        List<RadialTransformer> radialTransformers,
        List<ItemRandomizer> itemRandomizers
) {
}
