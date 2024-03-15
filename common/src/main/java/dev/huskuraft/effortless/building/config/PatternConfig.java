package dev.huskuraft.effortless.building.config;

import java.util.List;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.settings.PatternSettings;

public class PatternConfig implements ModConfig {

    private List<Pattern> patterns;

    public PatternConfig(
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