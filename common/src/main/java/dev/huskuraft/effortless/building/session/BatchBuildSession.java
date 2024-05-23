package dev.huskuraft.effortless.building.session;

import java.util.Objects;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.operation.OperationFilter;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.operation.block.BlockBreakOperation;
import dev.huskuraft.effortless.building.operation.block.BlockInteractOperation;
import dev.huskuraft.effortless.building.operation.block.BlockOperation;
import dev.huskuraft.effortless.building.operation.block.BlockPlaceOperation;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class BatchBuildSession implements BuildSession {

    private final World world;
    private final Player player;
    private final Context context;

    public BatchBuildSession(Player player, Context context) {
        this.world = player.getWorld();
        this.player = player;
        this.context = context;
    }

    protected BlockPlaceOperation createBlockPlaceOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockPlaceOperation(world, player, context, storage, interaction, Items.AIR.item().getBlock().getDefaultBlockState(), context.extras().entityState());
    }

    protected BlockBreakOperation createBlockBreakOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockBreakOperation(world, player, context, storage, interaction, context.extras().entityState());
    }

    protected BlockInteractOperation createBlockInteractOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockInteractOperation(world, player, context, storage, interaction, context.extras().entityState());
    }

    protected BatchOperation create(World world, Player player, Context context) {
        var storage = Storage.create(player, context.isPreviewType()); // TODO: 21/5/24 use storage from context
        var inHandTransformer = ItemRandomizer.single(null, player.getItemStack(InteractionHand.MAIN).getItem());
        var operations = (BatchOperation) new DeferredBatchOperation(context, () -> switch (context.buildState()) {
            case IDLE -> Stream.<BlockOperation>empty();
            case BREAK_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockBreakOperationFromHit(world, player, context, storage, interaction));
            case PLACE_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockPlaceOperationFromHit(world, player, context, storage, interaction));
            case INTERACT_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockInteractOperationFromHit(world, player, context, storage, interaction));
        });
        operations = (BatchOperation) inHandTransformer.transform(operations);

        if (context.pattern().enabled()) {
            for (var transformer : context.pattern().transformers()) {
                if (transformer.isValid()) {
                    operations = (BatchOperation) transformer.transform(operations);
                }
            }
        }
        operations = operations.flatten().filter(Objects::nonNull).filter(OperationFilter.distinctBlockOperations());

        return operations;
    }

    @Override
    public BatchOperationResult commit() {
        return create(world, player, context).commit();
    }

}
