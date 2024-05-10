package dev.huskuraft.effortless.building.pattern;

import java.util.List;
import java.util.UUID;

import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.session.BatchBuildSession;

public record Pattern(
        UUID id,
        boolean enabled,
        List<Transformer> transformers
) {

    public static final Pattern DISABLED = new Pattern(false, List.of());
    public static final Pattern EMPTY = new Pattern(true, List.of());

    public Pattern(boolean enabled, List<Transformer> transformers) {
        this(UUID.randomUUID(), enabled, transformers);
    }

    public Pattern withEnabled(boolean enabled) {
        return new Pattern(id, enabled, transformers);
    }

    public Pattern withRandomId() {
        return new Pattern(UUID.randomUUID(), enabled, transformers);
    }

    public Pattern withTransformers(List<Transformer> transformers) {
        return new Pattern(id, enabled, transformers);
    }

    public Pattern finalize(BatchBuildSession session, BuildStage stage) {
        return withTransformers(
                transformers().stream().map(transformer -> transformer.finalize(session, stage)).toList()
        );
    }

}
