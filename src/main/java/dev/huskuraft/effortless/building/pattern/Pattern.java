package dev.huskuraft.effortless.building.pattern;

import java.util.List;

import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.Option;

public record Pattern(
        boolean enabled,
        List<Transformer> transformers
) implements Option {

    public static final Pattern DISABLED = new Pattern(false, List.of());
    public static final Pattern EMPTY = new Pattern(true, List.of());

    public Pattern withEnabled(boolean enabled) {
        return new Pattern(enabled, transformers);
    }

    public Pattern withRandomId() {
        return new Pattern(enabled, transformers);
    }

    public Pattern withTransformers(List<Transformer> transformers) {
        return new Pattern(enabled, transformers);
    }

    public Pattern toggled() {
        return new Pattern(!enabled, transformers);
    }

    public Pattern finalize(Player player, BuildStage stage) {
        return withTransformers(
                transformers().stream().map(transformer -> transformer.finalize(player, stage)).toList()
        );
    }

    public float volumeMultiplier() {
        var multiplier = 1f;
        for (var transformer : transformers()) {
            multiplier = multiplier * transformer.volumeMultiplier();
        }
        return multiplier;
    }

    @Override
    public String getName() {
        if (enabled) {
            return Patterns.ENABLED.getName();
        } else {
            return Patterns.DISABLED.getName();
        }
    }

    @Override
    public String getCategory() {
        if (enabled) {
            return Patterns.ENABLED.getCategory();
        } else {
            return Patterns.DISABLED.getCategory();
        }
    }
}
