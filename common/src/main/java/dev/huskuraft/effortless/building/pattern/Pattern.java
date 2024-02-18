package dev.huskuraft.effortless.building.pattern;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import dev.huskuraft.effortless.building.BatchBuildSession;
import dev.huskuraft.effortless.building.BuildStage;

public final class Pattern {

    public static final Pattern DISABLED = new Pattern(Text.translate("effortless.pattern.disabled").withStyle(TextStyle.GRAY), List.of());
    public static final Pattern EMPTY = new Pattern(Text.translate("effortless.pattern.empty").withStyle(TextStyle.GRAY), List.of());
    public static final Pattern DEFAULT = new Pattern(Text.translate("effortless.pattern.default"), List.of());

    public static final int MAX_NAME_LENGTH = 255;

    private final UUID id;
    private final Text name;
    private final List<Transformer> transformers;

    public Pattern(UUID id, Text name, List<Transformer> transformers) {
        this.id = id;
        this.name = name;
        this.transformers = transformers;
    }

    public Pattern(Text name, List<Transformer> transformers) {
        this(UUID.randomUUID(), name, transformers);
    }

    public UUID id() {
        return id;
    }

    public Text name() {
        return name;
    }

    public List<Transformer> transformers() {
        return transformers;
    }

    public Pattern withName(Text name) {
        return new Pattern(id, name, transformers);
    }

    public Pattern withId(UUID id) {
        return new Pattern(id, name, transformers);
    }

    public Pattern withRandomId() {
        return new Pattern(UUID.randomUUID(), name, transformers);
    }

    public Pattern withTransformers(List<Transformer> transformers) {
        return new Pattern(id, name, transformers);
    }

    public Pattern finalize(BatchBuildSession session, BuildStage stage) {
        return withTransformers(
                transformers().stream().map(transformer -> transformer.finalize(session, stage)).toList()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Pattern) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.transformers, that.transformers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transformers);
    }

    @Override
    public String toString() {
        return "Pattern[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "transformers=" + transformers + ']';
    }

    public static List<Pattern> getDefaultPatterns() {
        return List.of(
                Pattern.DEFAULT
        );
    }


}
