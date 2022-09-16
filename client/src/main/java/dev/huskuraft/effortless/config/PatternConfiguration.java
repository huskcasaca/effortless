package dev.huskuraft.effortless.config;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.settings.PatternSettings;

import java.util.List;

public class PatternConfiguration implements Configuration {

    private List<Pattern> patterns;

    public PatternConfiguration(
            List<Pattern> patterns
    ) {
        this.patterns = patterns;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() {
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public PatternSettings getPatternSettings() {
        return new PatternSettings(patterns);
    }

    public void setPatternSettings(PatternSettings settings) {
        patterns = settings.patterns();
    }

}
