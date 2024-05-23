package dev.huskuraft.effortless.building.config;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record ClientConfig(
        RenderConfig renderConfig,
        PatternConfig patternConfig,
        Boolean passiveMode,
        Map<BuildMode, Structure> structureMap
) {

    public ClientConfig(
            RenderConfig renderConfig,
            PatternConfig patternConfig
    ) {
        this(renderConfig, patternConfig, Boolean.FALSE, Structure.DEFAULTS);
    }

    public static ClientConfig DEFAULT = new ClientConfig(
            RenderConfig.DEFAULT,
            PatternConfig.DEFAULT,
            Boolean.FALSE,
            Structure.DEFAULTS
    );

    public ClientConfig withRenderConfig(RenderConfig renderConfig) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, structureMap);
    }

    public ClientConfig withPatternConfig(PatternConfig patternConfig) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, structureMap);
    }


    public ClientConfig withPassiveMode(Boolean passiveMode) {
        return new ClientConfig(renderConfig, patternConfig, passiveMode, structureMap);
    }

    public ClientConfig withStructure(Structure structure) {
        var structureMap = Maps.newLinkedHashMap(this.structureMap);
        structureMap.put(structure.getMode(), structure);
        return new ClientConfig(renderConfig, patternConfig, passiveMode, Collections.unmodifiableMap(structureMap));
    }

    public Structure getStructure(BuildMode buildMode) {
        return structureMap.get(buildMode);
    }

}
