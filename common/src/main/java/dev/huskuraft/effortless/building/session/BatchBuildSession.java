package dev.huskuraft.effortless.building.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.building.clipboard.BlockSnapshot;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.building.operation.OperationFilter;
import dev.huskuraft.effortless.building.operation.batch.BatchOperation;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.batch.DeferredBatchOperation;
import dev.huskuraft.effortless.building.operation.block.BlockInteractOperation;
import dev.huskuraft.effortless.building.operation.block.BlockOperation;
import dev.huskuraft.effortless.building.operation.block.BlockStateCopyOperation;
import dev.huskuraft.effortless.building.operation.block.BlockStateCopyOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockStateUpdateOperation;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class BatchBuildSession implements BuildSession {

    private final StructureBuilder builder;
    private final World world;
    private final Player player;
    private final Context context;
    private BatchOperationResult lastResult;

    public BatchBuildSession(StructureBuilder builder, Player player, Context context) {
        this.builder = builder;
        this.world = player.getWorld();
        this.player = player;
        this.context = context;
    }

    private StructureBuilder getStructureBuilder() {
        return builder;
    }

    protected BlockOperation createBlockPlaceOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction, BlockState blockState) {
        return new BlockStateUpdateOperation(world, player, context, storage, interaction, blockState, context.extras().entityState());
    }

    protected BlockOperation createBlockBreakOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockStateUpdateOperation(world, player, context, storage, interaction, Items.AIR.item().getBlock().getDefaultBlockState(), context.extras().entityState());
    }

    protected BlockOperation createBlockInteractOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockInteractOperation(world, player, context, storage, interaction, context.extras().entityState());
    }

    protected BlockOperation createBlockCopyOperationFromHit(World world, Player player, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockStateCopyOperation(world, player, context, storage, interaction, context.extras().entityState());
    }

    protected BatchOperation create(World world, Player player, Context context) {
        var storage = Storage.create(player, context.isPreviewType()); // TODO: 21/5/24 use storage from context
        var inHandTransformer = ItemRandomizer.single(null, player.getItemStack(InteractionHand.MAIN).getItem());
        var operations = (BatchOperation) new DeferredBatchOperation(context, () -> switch (context.buildState()) {
            case IDLE -> Stream.<BlockOperation>empty();
            case BREAK_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockBreakOperationFromHit(world, player, context, storage, interaction));
            case PLACE_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockPlaceOperationFromHit(world, player, context, storage, interaction, Items.AIR.item().getBlock().getDefaultBlockState()));
            case INTERACT_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockInteractOperationFromHit(world, player, context, storage, interaction));
            case COPY_STRUCTURE ->
                    context.collectInteractions().map(interaction -> createBlockCopyOperationFromHit(world, player, context, storage, interaction));
            case PASTE_STRUCTURE -> {
                yield context.clipboard().blockSnapshots().stream().map(blockSnapshot -> {
                    var interaction = context.getInteraction(0).withBlockPosition(context.getInteraction(0).blockPosition().add(blockSnapshot.relativePosition()));
                    return createBlockPlaceOperationFromHit(world, player, context, storage, interaction, blockSnapshot.blockState());
                });
            }
        });
        if (context.buildState() == BuildState.PLACE_BLOCK) {
            operations = (BatchOperation) inHandTransformer.transform(operations);
        }
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
    public synchronized BatchOperationResult commit() {
        if (lastResult == null) {
            lastResult = create(world, player, context).commit();
            saveClipboard();
        }
        return lastResult;
    }

    protected void saveClipboard() {
        if (!world.isClient()) {
            return;
        }
        if (context.buildState() != BuildState.COPY_STRUCTURE) {
            return;
        }
        if (context.buildType() != BuildType.PREVIEW_ONCE) {
            return;
        }
        if (!context.clipboard().enabled()) {
            return;
        }
        var blockSnapshots = new ArrayList<BlockSnapshot>();
        for (var operationResult : lastResult.getResults()) {
            var blockCopyOperationResult = ((BlockStateCopyOperationResult) operationResult);
            blockSnapshots.add(blockCopyOperationResult.getBlockSnapshot());
        }
        getStructureBuilder().setClipboard(player, Clipboard.of(true, Collections.unmodifiableList(blockSnapshots)));
    }

}
