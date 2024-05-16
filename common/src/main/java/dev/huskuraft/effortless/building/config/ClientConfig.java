package dev.huskuraft.effortless.building.config;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.BuildStructure;

public record ClientConfig(
        RenderConfig renderConfig,
        PatternConfig patternConfig,
        Boolean passiveMode,
        Map<BuildMode, BuildStructure> buildStructures,
        BuildMode buildMode
) {

    public ClientConfig(
            RenderConfig renderConfig,
            PatternConfig patternConfig
    ) {
        this(renderConfig, patternConfig, Boolean.FALSE, BuildStructure.DEFAULTS, BuildMode.DISABLED);
    }

    public static ClientConfig DEFAULT = new ClientConfig(
            RenderConfig.DEFAULT,
            PatternConfig.DEFAULT,
            Boolean.FALSE,
            BuildStructure.DEFAULTS,
            BuildMode.DISABLED
    );

    public BuildStructure buildStructure() {
        return buildStructures.get(buildMode);
    }

    public ClientConfig withRenderConfig(RenderConfig renderConfig) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, buildStructures, buildMode);
    }

    public ClientConfig withPatternConfig(PatternConfig patternConfig) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, buildStructures, buildMode);
    }


    public ClientConfig withPassiveMode(Boolean passiveMode) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, buildStructures, buildMode);
    }

    public ClientConfig withBuildStructure(BuildStructure buildStructure) {
        var buildStructures = Maps.newLinkedHashMap(this.buildStructures);
        buildStructures.put(buildStructure.getMode(), buildStructure);
        return new ClientConfig(renderConfig, patternConfig, passiveMode, Collections.unmodifiableMap(buildStructures), buildStructure.getMode());
    }

    public ClientConfig withBuildMode(BuildMode buildMode) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, buildStructures, buildMode);
    }

}
