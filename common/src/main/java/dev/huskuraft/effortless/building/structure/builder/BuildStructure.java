package dev.huskuraft.effortless.building.structure.builder;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.RaisedEdge;
import dev.huskuraft.effortless.building.structure.builder.standard.Disable;

public interface BuildStructure {

    BuildStructure DISABLED = new Disable();

    Map<BuildMode, BuildStructure> DEFAULTS = Arrays.stream(BuildMode.values()).collect(Collectors.toMap(Function.identity(), BuildMode::getDefaultStructure, (e1, e2) -> e1, LinkedHashMap::new));

    int volume(Context context);

    int traceSize(Context context);

    BlockInteraction trace(Player player, Context context);

    Stream<BlockPosition> collect(Context context);

    BuildMode getMode();

    default Set<BuildFeatures> getSupportedFeatures() {
        return Sets.newHashSet(getMode().getSupportedFeatures());
    }

    @Deprecated
    default CircleStart circleStart() {
        return null;
    }

    @Deprecated
    default CubeFilling cubeFilling() {
        return null;
    }

    @Deprecated
    default PlaneFilling planeFilling() {
        return null;
    }

    @Deprecated
    default PlaneFacing planeFacing() {
        return null;
    }

    @Deprecated
    default PlaneLength planeLength() {
        return null;
    }

    @Deprecated
    default RaisedEdge raisedEdge() {
        return null;
    }

    @Deprecated
    default LineDirection lineDirection() {
        return null;
    }

    default Set<BuildFeature> getFeatures() {
        return Stream.of(circleStart(), cubeFilling(), planeFilling(), planeFacing(), planeLength(), raisedEdge(), lineDirection()).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    default BuildFeature getFeatureByType(BuildFeatures buildFeatures) {
        return getFeatures().stream().filter(f -> f.getType() == buildFeatures).findFirst().orElse(null);
    }

    default BuildStructure withFeature(BuildFeature feature) {
        return this;
    }

    default BuildStructure withFeatures(List<BuildFeature> features) {
        var buildStructure = this;
        for (var feature : features) {
            buildStructure = buildStructure.withFeature(feature);
        }
        return buildStructure;
    }


//    default BuildStructure withCircleStart(CircleStart circleStart) {
//        return this;
//    }
//
//    default BuildStructure withCubeFilling(CubeFilling cubeFilling) {
//        return this;
//    }
//
//    default BuildStructure withPlaneFilling(PlaneFilling planeFilling) {
//        return this;
//    }
//
//    default BuildStructure withPlaneFacing(PlaneFacing planeFacing) {
//        return this;
//    }
//
//    default BuildStructure withRaisedEdge(RaisedEdge raisedEdge) {
//        return this;
//    }
//
//    default BuildStructure withLineDirection(LineDirection lineDirection) {
//        return this;
//    }
//
//    default BuildStructure withPlaneLength(PlaneLength planeLength) {
//        return this;
//    }
//    default BuildStructure withFeature(BuildFeature feature) {
//        return switch (feature.getType()) {
//            case CIRCLE_START -> withCircleStart((CircleStart) feature);
//            case CUBE_FILLING -> withCubeFilling((CubeFilling) feature);
//            case CUBE_LENGTH -> this;
//            case PLANE_FACING -> withPlaneFacing((PlaneFacing) feature);
//            case PLANE_FILLING -> withPlaneFilling((PlaneFilling) feature);
//            case PLANE_LENGTH -> withPlaneLength((PlaneLength) feature);
//            case LINE_DIRECTION -> withLineDirection((LineDirection) feature);
//            case RAISED_EDGE -> withRaisedEdge((RaisedEdge) feature);
//        };
//    }

}
