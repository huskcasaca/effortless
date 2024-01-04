package dev.huskuraft.effortless.building.structure.builder.singles;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.SingleClickBuilder;

import java.util.stream.Stream;

public class Disable extends SingleClickBuilder {

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return player.raytrace(10, 1f, false);
    }

    @Override
    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return Stream.empty();
    }

}
