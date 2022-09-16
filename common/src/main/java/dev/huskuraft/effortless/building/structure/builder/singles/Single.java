package dev.huskuraft.effortless.building.structure.builder.singles;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.SingleClickBuilder;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;

import java.util.stream.Stream;

public class Single extends SingleClickBuilder {

    public static BlockInteraction traceSingle(Player player, Context context) {
        var target = player.raytrace(context.maxReachDistance(), 1f, false);

        var startPos = target.getBlockPosition();

        var tracingAbsolute = context.isBreakingBlock() || context.replaceMode().isQuick();
        var replaceable = player.getWorld().isBlockPlaceable(startPos);

        var tracingRelative = !tracingAbsolute && !replaceable;

        if (tracingRelative) {
            startPos = startPos.relative(target.getDirection());
        }
        return target.withPosition(startPos);
    }

    public static Stream<BlockPosition> collectSingleBlocks(Context context) {
        return Stream.of(context.firstBlockPosition());
    }

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return traceSingle(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return collectSingleBlocks(context);
    }

}
