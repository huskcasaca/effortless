package dev.huskuraft.effortless.building.structure.builder.triples;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.TripleClickBuilder;
import dev.huskuraft.effortless.building.structure.builder.doubles.Floor;
import dev.huskuraft.effortless.building.structure.builder.singles.Single;

public class Pyramid extends TripleClickBuilder {

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return Single.traceSingle(player, context);
    }

    @Override
    protected BlockInteraction traceSecondInteraction(Player player, Context context) {
        return Floor.traceFloor(player, context);
    }

    @Override
    protected BlockInteraction traceThirdInteraction(Player player, Context context) {
        return traceLineY(player, context);
    }

    @Override
    protected Stream<BlockPosition> collectStartBlocks(Context context) {
        return Single.collectSingleBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectInterBlocks(Context context) {
        return Floor.collectFloorBlocks(context);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        // TODO
        return SlopeFloor.collectSlopeFloorBlocks(context);
    }
}
