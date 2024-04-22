package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Disable extends AbstractBlockStructure {

    @Override
    protected BlockInteraction traceFirstInteraction(Player player, Context context) {
        return player.raytrace(10, 1f, false);
    }

    @Override
    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return Stream.empty();
    }

    @Override
    public int totalClicks(Context context) {
        return 1;
    }

}
