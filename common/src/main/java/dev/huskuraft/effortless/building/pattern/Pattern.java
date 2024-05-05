package dev.huskuraft.effortless.building.pattern;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BatchBuildSession;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public final class Pattern {

    public static final Pattern DISABLED = new Pattern(Text.translate("effortless.pattern.disabled").withStyle(ChatFormatting.GRAY), List.of());
    public static final Pattern EMPTY = new Pattern(Text.translate("effortless.pattern.empty").withStyle(ChatFormatting.GRAY), List.of());
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

    public static Pattern getDefaultPattern() {
        return new Pattern(Text.translate("effortless.pattern.default"), List.of());
    }

    public static List<Pattern> getPatternPresets() {
        return List.of(
                new Pattern(Text.translate("effortless.pattern.array_preset"), List.of(
                        ArrayTransformer.DEFAULT
                )),
                new Pattern(Text.translate("effortless.pattern.mirror_x_preset"), List.of(
                        MirrorTransformer.DEFAULT_X
                )),
                new Pattern(Text.translate("effortless.pattern.mirror_y_preset"), List.of(
                        MirrorTransformer.DEFAULT_Y
                )),
                new Pattern(Text.translate("effortless.pattern.mirror_z_preset"), List.of(
                        MirrorTransformer.DEFAULT_Z
                )),
                new Pattern(Text.translate("effortless.pattern.radial_preset"), List.of(
                        RadialTransformer.DEFAULT
                )),
                new Pattern(Text.translate("effortless.pattern.item_single_random_preset"), List.of(
                        ItemRandomizer.getDefaultItemRandomizers().get(1).withRandomOrder().withSingleTarget()
                )),
                new Pattern(Text.translate("effortless.pattern.item_single_sequence_preset"), List.of(
                        ItemRandomizer.getDefaultItemRandomizers().get(1).withSequenceOrder().withSingleTarget()
                )),
                new Pattern(Text.translate("effortless.pattern.item_group_random_preset"), List.of(
                        ArrayTransformer.DEFAULT,
                        ItemRandomizer.getDefaultItemRandomizers().get(1).withRandomOrder().withGroupTarget()
                )),
                new Pattern(Text.translate("effortless.pattern.item_group_sequence_preset"), List.of(
                        ArrayTransformer.DEFAULT,
                        ItemRandomizer.getDefaultItemRandomizers().get(1).withSequenceOrder().withGroupTarget()
                ))
        );
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


}
