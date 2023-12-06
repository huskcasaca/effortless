package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.text.Text;

import java.util.List;

public final class Pattern {

    public static final Pattern DISABLED = new Pattern(Text.translate("effortless.pattern.disabled"), List.of());
    public static final Pattern EMPTY = new Pattern(Text.translate("effortless.pattern.empty"), List.of());
    public static final Pattern DEFAULT = new Pattern(Text.translate("effortless.pattern.default"), List.of());

    private final Text name;
    private final List<Transformer> transformers;

    public Pattern(Text name, List<Transformer> transformers) {
        this.name = name;
        this.transformers = transformers;
    }

    public Text name() {
        return name;
    }

    public List<Transformer> transformers() {
        return transformers;
    }

}
