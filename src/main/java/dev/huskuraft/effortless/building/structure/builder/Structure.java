package dev.huskuraft.effortless.building.structure.builder;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
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

public interface Structure {

    Structure DISABLED = new Disable();

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

    default Structure withFeature(BuildFeature feature) {
        return this;
    }

    default Structure withFeatures(List<BuildFeature> features) {
        var structure = this;
        for (var feature : features) {
            structure = structure.withFeature(feature);
        }
        return structure;
    }

//    default BuildFeature getFeatureByType(BuildFeatures buildFeatures) {
//        return getFeatures().stream().filter(f -> f.getType() == buildFeatures).findFirst().orElse(null);
//    }
//
//    default Structure withCircleStart(CircleStart circleStart) {
//        return this;
//    }
//
//    default Structure withCubeFilling(CubeFilling cubeFilling) {
//        return this;
//    }
//
//    default Structure withPlaneFilling(PlaneFilling planeFilling) {
//        return this;
//    }
//
//    default Structure withPlaneFacing(PlaneFacing planeFacing) {
//        return this;
//    }
//
//    default Structure withRaisedEdge(RaisedEdge raisedEdge) {
//        return this;
//    }
//
//    default Structure withLineDirection(LineDirection lineDirection) {
//        return this;
//    }
//
//    default Structure withPlaneLength(PlaneLength planeLength) {
//        return this;
//    }
//    default Structure withFeature(BuildFeature feature) {
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
