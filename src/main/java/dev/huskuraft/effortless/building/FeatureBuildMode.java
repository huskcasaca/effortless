package dev.huskuraft.effortless.building;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildFeatures;
import dev.huskuraft.effortless.building.structure.BuildMode;

record FeatureBuildMode(
        BuildMode buildMode,
        Set<BuildFeature> features
) {

    public FeatureBuildMode {

    }

    public static FeatureBuildMode createDefault(BuildMode buildMode) {
        return new FeatureBuildMode(buildMode, Arrays.stream(buildMode.getSupportedFeatures()).map(BuildFeatures::getDefaultFeature).collect(Collectors.toSet()));
    }


    public FeatureBuildMode withBuildFeature(BuildFeature feature) {
        return this;
    }


}
