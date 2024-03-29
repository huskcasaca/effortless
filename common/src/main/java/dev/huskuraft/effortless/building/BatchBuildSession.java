package dev.huskuraft.effortless.building;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.operation.OperationFilter;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOperation;
import dev.huskuraft.effortless.building.operation.block.BlockOperation;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOperation;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class BatchBuildSession implements BuildSession {

    private final World world;
    private final Player player;
    private final Context context;

    public BatchBuildSession(World world, Player player, Context context) {
        this.world = world;
        this.player = player;
        this.context = context;
    }

    protected BlockPlaceOperation createBlockPlaceOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockPlaceOperation(world, player, context, storage, interaction, null);
    }

    protected BlockBreakOperation createBlockBreakOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockBreakOperation(world, player, context, storage, interaction);
    }

    protected BatchOperation createBaseDeferred(World world, Player player, Context context, Storage storage) {
        var operations = new DeferredBatchOperation(context, () -> switch (context.state()) {
            case IDLE -> Stream.<BlockOperation>empty();
            case PLACE_BLOCK ->
                    context.collect().map(interaction -> createBlockPlaceOperationFromHit(world, player, context, storage, interaction));
            case BREAK_BLOCK ->
                    context.collect().map(interaction -> createBlockBreakOperationFromHit(world, player, context, storage, interaction));
        });
        return ItemRandomizer.create(null, player.getItemStack(InteractionHand.MAIN).getItem()).transform(operations);
    }

    protected BatchOperation create(World world, Player player, Context context) {
        var storage = Storage.create(player, context.isPreview());
        var operations = createBaseDeferred(world, player, context, storage);

        for (var transformer : context.pattern().transformers()) {
            if (transformer.isValid()) {
                operations = transformer.transform(operations);
            }
        }
        operations = operations.flatten();
        operations = operations.filter(OperationFilter.distinctByLocation());

        return operations;
    }

    @Override
    public BatchOperation build() {
        return create(world, player, context);
    }

    public Player getPlayer() {
        return player;
    }
}
