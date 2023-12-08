package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.building.operation.OperationFilter;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOperation;
import dev.huskuraft.effortless.building.operation.block.BlockOperation;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOperation;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.InteractionHand;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.World;

import java.util.stream.Stream;

public final class BuildSession implements Session {

    private final World world;
    private final Player player;
    private final Context context;

    public BuildSession(World world, Player player, Context context) {
        this.world = world;
        this.player = player;
        this.context = context;
    }

    private static BlockPlaceOperation createBlockPlaceOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockPlaceOperation(world, player, context, storage, interaction, null);
    }

    private static BlockBreakOperation createBlockBreakOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockBreakOperation(world, player, context, storage, interaction);
    }

    private static BatchOperation createBaseDeferred(World world, Player player, Context context, Storage storage) {
        var operations = new DeferredBatchOperation(context, () -> switch (context.state()) {
            case IDLE -> Stream.<BlockOperation>empty();
            case PLACE_BLOCK ->
                    context.collect().map(interaction -> createBlockPlaceOperationFromHit(world, player, context, storage, interaction));
            case BREAK_BLOCK ->
                    context.collect().map(interaction -> createBlockBreakOperationFromHit(world, player, context, storage, interaction));
        });
        return ItemRandomizer.create(null, player.getItemStack(InteractionHand.MAIN).getItem()).transform(operations);
    }

    private static BatchOperation create(World world, Player player, Context context) {
        var storage = player.getStorage();
        if (context.isPreview()) {
            storage = storage.clone();
        }
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

}