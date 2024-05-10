package dev.huskuraft.effortless.building.config;

import java.util.List;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public record ClientConfig(
        RenderConfig renderConfig,
        TransformerPresets transformerPresets,
        Boolean passiveMode
) {
    public static ClientConfig getDefault() {
        return new ClientConfig(
                new RenderConfig(),
                new TransformerPresets(
                        List.of(),
                        List.of(),
                        List.of(),
                        ItemRandomizer.getDefaultItemRandomizers()),
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

}
