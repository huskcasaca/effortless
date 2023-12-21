package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockPosition;
import dev.huskuraft.effortless.core.Player;

import java.util.stream.Stream;

public abstract class DoubleClickBuilder extends AbstractClickBuilder {

    protected abstract BlockInteraction traceFirstInteraction(Player player, Context context);

    protected abstract BlockInteraction traceSecondInteraction(Player player, Context context);

    protected Stream<BlockPosition> collectFirstBlocks(Context context) {
        return Stream.of(context.firstBlockPosition());
    }

    protected Stream<BlockPosition> collectFinalBlocks(Context context) {
        return Stream.empty();
    }

    @Override
    public BlockInteraction trace(Player player, Context context) {
        return switch (context.interactionsSize()) {
            case 0 -> traceFirstInteraction(player, context);
            case 1 -> traceSecondInteraction(player, context);
            default -> null;
        };
    }

    @Override
    public Stream<BlockPosition> collect(Context context) {
        return switch (context.interactionsSize()) {
            case 1 -> collectFirstBlocks(context);
            case 2 -> collectFinalBlocks(context);
            default -> Stream.empty();
        };
    }

    @Override
    public int totalClicks(Context context) {
        return 2;
    }
}
