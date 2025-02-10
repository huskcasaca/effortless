package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;

public record DiagonalLine() implements BlockStructure {

    private static Set<BlockPosition> getDiagonalLine(List<BlockPosition> positions, int radius, boolean hollow) {
        Set<BlockPosition> result = Sets.newLinkedHashSet();

        for (int i = 0; !positions.isEmpty() && i < positions.size() - 1; i++) {
            var pos1 = positions.get(i);
            var pos2 = positions.get(i + 1);

            var x1 = pos1.x();
            var y1 = pos1.y();
            var z1 = pos1.z();
            var x2 = pos2.x();
            var y2 = pos2.y();
            var z2 = pos2.z();
            var tipx = x1;
            var tipy = y1;
            var tipz = z1;
            var dx = MathUtils.abs(x2 - x1);
            var dy = MathUtils.abs(y2 - y1);
            var dz = MathUtils.abs(z2 - z1);

            if (dx + dy + dz == 0) {
                result.add(BlockPosition.at(tipx, tipy, tipz));
                continue;
            }

            int dm = MathUtils.max(MathUtils.max(dx, dy), dz);
            if (dm == dx) {
                for (var domstep = 0; domstep <= dx; domstep++) {
                    tipx = x1 + domstep * (x2 - x1 > 0 ? 1 : -1);
                    tipy = (int) MathUtils.round(y1 + domstep * ((double) dy) / ((double) dx) * (y2 - y1 > 0 ? 1 : -1));
                    tipz = (int) MathUtils.round(z1 + domstep * ((double) dz) / ((double) dx) * (z2 - z1 > 0 ? 1 : -1));

                    result.add(BlockPosition.at(tipx, tipy, tipz));
                }
            } else if (dm == dy) {
                for (var domstep = 0; domstep <= dy; domstep++) {
                    tipy = y1 + domstep * (y2 - y1 > 0 ? 1 : -1);
                    tipx = (int) MathUtils.round(x1 + domstep * ((double) dx) / ((double) dy) * (x2 - x1 > 0 ? 1 : -1));
                    tipz = (int) MathUtils.round(z1 + domstep * ((double) dz) / ((double) dy) * (z2 - z1 > 0 ? 1 : -1));

                    result.add(BlockPosition.at(tipx, tipy, tipz));
                }
            } else /* if (dm == dz) */ {
                for (var domstep = 0; domstep <= dz; domstep++) {
                    tipz = z1 + domstep * (z2 - z1 > 0 ? 1 : -1);
                    tipy = (int) MathUtils.round(y1 + domstep * ((double) dy) / ((double) dz) * (y2 - y1 > 0 ? 1 : -1));
                    tipx = (int) MathUtils.round(x1 + domstep * ((double) dx) / ((double) dz) * (x2 - x1 > 0 ? 1 : -1));

                    result.add(BlockPosition.at(tipx, tipy, tipz));
                }
            }
        }

        result = BlockStructure.getBallooned(result, radius);
        if (hollow) result = BlockStructure.getHollowed(result);
        return result;
    }

    protected static Stream<BlockPosition> collectDiagonalLine(Context context) {
        return getDiagonalLine(context.getPositions(), 0, false).stream();
    }

    public static Stream<BlockPosition> collectDiagonalLine(List<BlockPosition> positions, int radius, boolean hollow) {
        return getDiagonalLine(positions, 0, false).stream();
    }

    public static Stream<BlockPosition> collectDiagonalLine(BlockPosition pos1, BlockPosition pos2, int radius, boolean hollow) {
        return getDiagonalLine(List.of(pos1, pos2), 0, false).stream();
    }

    public BlockInteraction trace(Player player, Context context, int index) {
        return switch (index) {
            case 0 -> Single.traceSingle(player, context);
            case 1 -> Single.traceSingle(player, context);
            default -> null;
        };
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return switch (index) {
            case 1 -> Single.collectSingleBlocks(context);
            case 2 -> DiagonalLine.collectDiagonalLine(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int traceSize(Context context) {
        return 2;
    }

    @Override
    public BuildMode getMode() {
        return BuildMode.DIAGONAL_LINE;
    }
}
