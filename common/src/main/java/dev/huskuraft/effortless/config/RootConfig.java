package dev.huskuraft.effortless.config;

public final class RootConfig implements ModConfig {

    private final PreviewConfig previewConfig;

    private final TransformerConfig transformerConfig;

    private final PatternConfig patternConfig;

    public RootConfig(
            PreviewConfig previewConfig,
            TransformerConfig transformerConfig,
            PatternConfig patternConfig
    ) {
        this.previewConfig = previewConfig;
        this.transformerConfig = transformerConfig;
        this.patternConfig = patternConfig;
    }

    @Override
    public boolean isValid() {
        return previewConfig.isValid() && transformerConfig.isValid() && patternConfig.isValid();
    }

    @Override
    public void validate() {
        previewConfig.validate();
        transformerConfig.validate();
        patternConfig.validate();
    }

    public PreviewConfig getPreviewConfig() {
        return previewConfig;
    }

    public TransformerConfig getTransformerConfig() {
        return transformerConfig;
    }

    public PatternConfig getPatternConfig() {
        return patternConfig;
    }

}
