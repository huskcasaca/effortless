package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildFeature;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.PlaneLength;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;
import dev.huskuraft.effortless.building.structure.builder.Structure;

public record DiagonalWall(
        PlaneLength planeLength
) implements BlockStructure {

    public DiagonalWall() {
        this(PlaneLength.VARIABLE);
    }

    @Override
    public Structure withFeature(BuildFeature feature) {
        return switch (feature.getType()) {
            case PLANE_LENGTH -> new DiagonalWall((PlaneLength) feature);
            default -> this;
        };
    }

    // add diagonal wall from first to second
    public static Stream<BlockPosition> collectDiagonalWallBlocks(Context context) {
        Set<BlockPosition> set = Sets.newLinkedHashSet();

        var pos1 = context.getPosition(0);
        var pos2 = context.getPosition(1);
        var pos3 = context.getPosition(2);

        var x1 = pos1.x();
        var y1 = pos1.y();
        var z1 = pos1.z();

        var x2 = pos2.x();
        var y2 = pos2.y();
        var z2 = pos2.z();

        var x3 = pos3.x();
        var y3 = pos3.y();
        var z3 = pos3.z();

        // get diagonal line blocks
        var diagonalLineBlocks = DiagonalLine.collectDiagonalLine(context).toList();

        int lowest = MathUtils.min(y1, y3);
        int highest = MathUtils.max(y1, y3);

        // copy diagonal line on y axis
        for (int y = lowest; y <= highest; y++) {
            for (BlockPosition blockPosition : diagonalLineBlocks) {
                set.add(new BlockPosition(blockPosition.x(), y, blockPosition.z()));
            }
        }

        return set.stream();
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Floor.traceFloor(player, context, planeLength);
            case 2 -> Line.traceLineY(player, context.getPosition(1));
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> DiagonalLine.collectDiagonalLine(context);
            case 3 -> DiagonalWall.collectDiagonalWallBlocks(context);
            default -> Stream.empty();
        };
    }


    @Override
    public int traceSize(Context context) {
        return 3;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.DIAGONAL_WALL;
    }
}
