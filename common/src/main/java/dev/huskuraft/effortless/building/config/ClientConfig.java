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
        Map<BuildMode, BuildStructure> buildStructures
) {

    public ClientConfig(
            RenderConfig renderConfig,
            PatternConfig patternConfig
    ) {
        this(renderConfig, patternConfig, Boolean.FALSE, BuildStructure.DEFAULTS);
    }

    public static ClientConfig DEFAULT = new ClientConfig(
            RenderConfig.DEFAULT,
            PatternConfig.DEFAULT,
            Boolean.FALSE,
            BuildStructure.DEFAULTS
    );

    public ClientConfig withRenderConfig(RenderConfig renderConfig) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, buildStructures);
    }

    public ClientConfig withPatternConfig(PatternConfig patternConfig) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, buildStructures);
    }


    public ClientConfig withPassiveMode(Boolean passiveMode) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, buildStructures);
    }

    public ClientConfig withBuildStructure(BuildStructure buildStructure) {
        var buildStructures = Maps.newLinkedHashMap(this.buildStructures);
        buildStructures.put(buildStructure.getMode(), buildStructure);
        return new ClientConfig(renderConfig, patternConfig, passiveMode, Collections.unmodifiableMap(buildStructures));
    }

    public BuildStructure getBuildStructure(BuildMode buildMode) {
        return buildStructures.get(buildMode);
    }

}
