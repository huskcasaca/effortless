package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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


}
