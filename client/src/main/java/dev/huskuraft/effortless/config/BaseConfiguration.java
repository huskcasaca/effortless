package dev.huskuraft.effortless.config;

public class BaseConfiguration implements Configuration {

    private final PreviewConfiguration previewConfig;

    private final TransformerConfiguration transformerConfig;

    private final PatternConfiguration patternConfig;

    public BaseConfiguration(
            PreviewConfiguration previewConfig,
            TransformerConfiguration transformerConfig,
            PatternConfiguration patternConfig
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

    public PreviewConfiguration getPreviewConfig() {
        return previewConfig;
    }

    public TransformerConfiguration getTransformerConfig() {
        return transformerConfig;
    }

    public PatternConfiguration getPatternConfig() {
        return patternConfig;
    }

}
