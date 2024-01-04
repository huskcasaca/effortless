package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;

import java.util.stream.Stream;

public abstract class SingleClickBuilder extends AbstractClickBuilder {

    protected abstract BlockInteraction traceFirstInteraction(Player player, Context context);

    protected abstract Stream<BlockPosition> collectFinalBlocks(Context context);

    @Override
    public BlockInteraction trace(Player player, Context context) {
        return switch (context.interactionsSize()) {
            case 0 -> traceFirstInteraction(player, context);
            default -> null;
        };
    }

    @Override
    public Stream<BlockPosition> collect(Context context) {
        return collectFinalBlocks(context);
    }

    @Override
    public int totalClicks(Context context) {
        return 1;
    }

}
