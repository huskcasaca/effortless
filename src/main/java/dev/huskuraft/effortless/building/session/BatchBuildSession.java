package dev.huskuraft.effortless.building.session;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.InteractionHand;
import dev.huskuraft.universal.api.core.Items;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.core.World;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.Storage;
import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.building.interceptor.BuildInterceptor;
import dev.huskuraft.effortless.building.interceptor.FtbChunksInterceptor;
import dev.huskuraft.effortless.building.interceptor.OpenPacInterceptor;
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
import dev.huskuraft.effortless.networking.packets.player.PlayerSnapshotCapturePacket;

public class BatchBuildSession implements Session {

    private final Entrance entrance;
    private final Player player;
    private final World world;
    private final Context context;
    private final List<BuildInterceptor> interceptors;

    private BatchOperationResult lastResult;

    public BatchBuildSession(Entrance entrance, Player player, Context context) {
        this.entrance = entrance;
        this.world = player.getWorld();
        this.player = player;
        this.context = context;
        this.interceptors = createInterceptors(entrance, player.getWorld(), player, context);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    private static List<BuildInterceptor> createInterceptors(Entrance entrance, World world, Player player, Context context) {
        return Stream.of(
                new OpenPacInterceptor(entrance),
                new FtbChunksInterceptor(entrance)
        ).filter(BuildInterceptor::isEnabled).collect(Collectors.toUnmodifiableList());
    }

    public Entrance getEntrance() {
        return entrance;
    }

    protected BlockOperation createBlockPlaceOperationFromInteraction(Player player, World world, Context context, Storage storage, BlockInteraction interaction, BlockState blockState, RecordTag entityTag) {
        return new BlockStateUpdateOperation(this, context, storage, interaction, blockState, entityTag, context.extras().extras());
    }

    protected BlockOperation createBlockBreakOperationFromInteraction(Player player, World world, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockStateUpdateOperation(this, context, storage, interaction, Items.AIR.item().getBlock().getDefaultBlockState(), null, context.extras().extras());
    }

    protected BlockOperation createBlockInteractOperationFromInteraction(Player player, World world, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockInteractOperation(this, context, storage, interaction, context.extras().extras());
    }

    protected BlockOperation createBlockCopyOperationFromInteraction(Player player, World world, Context context, Storage storage, BlockInteraction interaction) {
        return new BlockStateCopyOperation(this, context, storage, interaction, context.extras().extras());
    }

    protected BatchOperation create(World world, Player player, Context context) {
        var storage = Storage.create(player, context.isPreviewType() || context.isBuildClientType()); // TODO: 21/5/24 use storage from context
        var inHandTransformer = ItemRandomizer.single(null, player.getItemStack(InteractionHand.MAIN).getItem());
        var operations = (BatchOperation) new DeferredBatchOperation(context, () -> switch (context.buildState()) {
            case IDLE -> Stream.<BlockOperation>empty();
            case BREAK_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockBreakOperationFromInteraction(player, world, context, storage, interaction));
            case PLACE_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockPlaceOperationFromInteraction(player, world, context, storage, interaction, Items.AIR.item().getBlock().getDefaultBlockState(), null));
            case INTERACT_BLOCK ->
                    context.collectInteractions().map(interaction -> createBlockInteractOperationFromInteraction(player, world, context, storage, interaction));
            case COPY_STRUCTURE ->
                    context.collectInteractions().map(interaction -> createBlockCopyOperationFromInteraction(player, world, context, storage, interaction));
            case PASTE_STRUCTURE -> {
                yield context.clipboard().snapshot().blockData().stream().map(blockSnapshot -> {
                    var interaction = context.getInteraction(0).withBlockPosition(context.getInteraction(0).blockPosition().add(blockSnapshot.blockPosition()));
                    return createBlockPlaceOperationFromInteraction(player, world, context, storage, interaction, blockSnapshot.blockState(), blockSnapshot.entityTag());
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

    public List<BuildInterceptor> getInterceptors() {
        return interceptors;
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
        if (world.isClient()) {
            return;
        }
        if (context.buildState() != BuildState.COPY_STRUCTURE) {
            return;
        }
        if (context.buildType() != BuildType.BUILD) {
            return;
        }
        if (!context.clipboard().enabled()) {
            return;
        }
        var snapshot = new Snapshot("", System.currentTimeMillis(), lastResult.getResults().stream().map(BlockStateCopyOperationResult.class::cast).map(BlockStateCopyOperationResult::getBlockData).filter(blockData -> context.clipboard().copyAir() || (blockData.blockState() != null && !blockData.blockState().isAir())).toList());
        getEntrance().getChannel().sendPacket(new PlayerSnapshotCapturePacket(player.getId(), snapshot), player);
    }

}
