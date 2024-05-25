package dev.huskuraft.effortless.building.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record ClientConfig(
        BuilderConfig builderConfig,
        PatternConfig patternConfig,
        RenderConfig renderConfig,
        Map<BuildMode, Structure> structureMap
) {

    public ClientConfig(
            RenderConfig renderConfig,
            PatternConfig patternConfig
    ) {
        this(BuilderConfig.DEFAULT, patternConfig, renderConfig, DEFAULT.structureMap());
    }

    public static ClientConfig DEFAULT = new ClientConfig(
            BuilderConfig.DEFAULT, PatternConfig.DEFAULT, RenderConfig.DEFAULT, Arrays.stream(BuildMode.values()).collect(Collectors.toMap(Function.identity(), BuildMode::getDefaultStructure, (e1, e2) -> e1, LinkedHashMap::new))
    );

    public ClientConfig withRenderConfig(RenderConfig renderConfig) {
        return new ClientConfig(builderConfig, patternConfig, renderConfig, structureMap);
    }

    public ClientConfig withPatternConfig(PatternConfig patternConfig) {
        return new ClientConfig(builderConfig, patternConfig, renderConfig, structureMap);
    }

    public ClientConfig withBuilderConfig(BuilderConfig builderConfig) {
        return new ClientConfig(builderConfig, patternConfig, renderConfig, structureMap);
    }

    public ClientConfig withPassiveMode(boolean passiveMode) {
        return new ClientConfig(builderConfig.withPassiveMode(passiveMode), patternConfig, renderConfig, structureMap);
    }

    public ClientConfig withStructure(Structure structure) {
        var structureMap = Maps.newLinkedHashMap(this.structureMap);
        structureMap.put(structure.getMode(), structure);
        return new ClientConfig(builderConfig, patternConfig, renderConfig, Collections.unmodifiableMap(structureMap));
    }

    public Structure getStructure(BuildMode buildMode) {
        return structureMap.get(buildMode);
    }

}
