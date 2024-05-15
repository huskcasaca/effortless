package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.PlaneFilling;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.BlockBuildStructure;
import dev.huskuraft.effortless.building.structure.builder.BuildStructure;

public record Wall(
        PlaneFilling planeFilling,
        PlaneLength planeLength
) implements BlockBuildStructure {

    public Wall() {
        this(PlaneFilling.PLANE_FULL, PlaneLength.VARIABLE);
    }

    @Override
    public BuildStructure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case PLANE_FILLING -> new Wall((PlaneFilling) feature, planeLength);
            case PLANE_LENGTH -> new Wall(planeFilling, (PlaneLength) feature);
            default -> this;
        };
    }

    protected static BlockInteraction traceWall(Player player, Context context, PlaneLength planeLength) {
        return traceWall(player, context.getInteraction(0), planeLength == PlaneLength.EQUAL);
    }

    protected static BlockInteraction traceWall(Player player, BlockInteraction start, boolean uniformLength) {
        var center = start.getBlockPosition().getCenter();
        var reach = 1024;
        var skipRaytrace = false;

        var result = Stream.of(
                        new WallCriteria(Axis.X, player, center, reach, skipRaytrace),
                        new WallCriteria(Axis.Z, player, center, reach, skipRaytrace)
                )
                .filter(AxisCriteria::isInRange)
                .min(Comparator.comparing(WallCriteria::angle))
                .map(AxisCriteria::tracePlane)
                .orElse(null);

        return BlockBuildStructure.transformUniformLengthInteraction(start, result, uniformLength);
    }


    public static int sign(int a) {
        return ((int) Math.signum(a)) == 0 ? 1 : (int) Math.signum(a);
    }

    public static Stream<BlockPosition> collectWallBlocks(Context context, PlaneFilling planeFilling) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();
        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        switch (BlockBuildStructure.getShape(pos1, pos2)) {
            case SINGLE -> Single.addSingleBlock(set, x1, y1, z1);
            case LINE_X, LINE_Y, LINE_Z -> Line.addLineBlocks(set, x1, y1, z1, x2, y2, z2);
            case PLANE_Z -> {
                switch (planeFilling) {
                    case PLANE_FULL -> Square.addFullSquareBlocksZ(set, x1, x2, y1, y2, z1);
                    case PLANE_HOLLOW -> Square.addHollowSquareBlocksZ(set, x1, x2, y1, y2, z1);
                }
            }
            case PLANE_X -> {
                switch (planeFilling) {
                    case PLANE_FULL -> Square.addFullSquareBlocksX(set, x1, y1, y2, z1, z2);
                    case PLANE_HOLLOW -> Square.addHollowSquareBlocksX(set, x1, y1, y2, z1, z2);
                }
            }
        }

        return set.stream();
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Wall.traceWall(player, context, planeLength);
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> Wall.collectWallBlocks(context, planeFilling);
            default -> Stream.empty();
        };
    }


    @Override
    public int traceSize(Context context) {
        return 2;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.WALL;
    }

    protected static class WallCriteria extends AxisCriteria {

        public WallCriteria(Axis axis, Player player, Vector3d center, int reach, boolean skipRaytrace) {
            super(axis, player, center, reach, skipRaytrace);
        }

        public double angle() {
            var wall = planeVec().sub(startVec());
            return MathUtils.abs(wall.x() * look.x()) + Math.abs(wall.z() * look.z());
        }

        public double distanceAngle() {
            return distanceToEyeSqr() * angle();
        }

    }

}
