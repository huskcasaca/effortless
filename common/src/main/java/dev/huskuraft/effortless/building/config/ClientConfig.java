package dev.huskuraft.effortless.building.config;

import java.util.List;

import dev.huskuraft.effortless.building.structure.BuildMode;

public record ClientConfig(
        RenderConfig renderConfig,
        TransformerPresets transformerPresets,
        Boolean passiveMode
) {
    public static ClientConfig getDefault() {
        return new ClientConfig(
                new RenderConfig(),
                new TransformerPresets(),
                false
        );
    }

    @Deprecated
    public PatternConfig patternConfig() {
        return new PatternConfig(List.of());
    }

    public ClientConfig(
            RenderConfig renderConfig,
            PatternConfig patternConfig,
            TransformerPresets transformerPresets,
            Boolean passiveMode
    ) {

        this(renderConfig, transformerPresets, passiveMode);
    }

    public record BuildModeConfig(
            BuildMode buildMode
    ) { }
}
