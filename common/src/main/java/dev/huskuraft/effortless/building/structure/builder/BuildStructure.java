package dev.huskuraft.effortless.building.structure.builder;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildFeatures;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.CircleStart;
import dev.huskuraft.effortless.building.structure.CubeFilling;
import dev.huskuraft.effortless.building.structure.LineDirection;
import dev.huskuraft.effortless.building.structure.PlaneFacing;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.RaisedEdge;
import dev.huskuraft.effortless.building.structure.builder.standard.Disable;

public interface BuildStructure {

    BuildStructure DISABLED = new Disable();

    int volume(Context context);

    int traceSize(Context context);

    BlockInteraction trace(Player player, Context context);

    Stream<BlockPosition> collect(Context context);

    BuildMode getMode();

    default Set<BuildFeatures> getSupportedFeatures() {
        return Sets.newHashSet(getMode().getSupportedFeatures());
    }

    default CircleStart circleStart() {
        return null;
    }

    default CubeFilling cubeFilling() {
        return null;
    }

    default PlaneFilling planeFilling() {
        return null;
    }

    default PlaneFacing planeFacing() {
        return null;
    }

    default RaisedEdge raisedEdge() {
        return null;
    }

    default LineDirection lineDirection() {
        return null;
    }

    default Set<BuildFeature> getFeatures() {
        return Stream.of(circleStart(), cubeFilling(), planeFilling(), planeFacing(), raisedEdge(), lineDirection()).filter(Objects::nonNull).collect(Collectors.toSet());
    }

}
