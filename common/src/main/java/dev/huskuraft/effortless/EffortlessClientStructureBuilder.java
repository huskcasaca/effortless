package dev.huskuraft.effortless;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.core.Tuple2;
import dev.huskuraft.effortless.api.events.EventResult;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.renderer.LightTexture;
import dev.huskuraft.effortless.api.sound.SoundInstance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildResult;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.SingleCommand;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.history.BuildTooltip;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.OperationSummaryType;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockOperation;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.session.BatchBuildSession;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.Structure;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.renderer.opertaion.children.BlockOperationRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderLayers;
import dev.huskuraft.effortless.screen.wheel.AbstractWheelScreen;
import dev.huskuraft.effortless.session.config.ConstraintConfig;
import dev.huskuraft.effortless.session.config.SessionConfig;

public final class EffortlessClientStructureBuilder extends StructureBuilder {

    private final EffortlessClient entrance;

    private final Map<UUID, Context> contexts = new HashMap<>();
    private final Map<UUID, Context> historyContexts = new HashMap<>();
    private final Map<UUID, OperationResultStack> undoRedoStacks = new HashMap<>();
    private final AtomicReference<ResourceLocation> lastClientPlayerLevel = new AtomicReference<>();

    public EffortlessClientStructureBuilder(EffortlessClient entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getClientTickEvent().register(this::onClientTick);
    }

    private EffortlessClient getEntrance() {
        return entrance;
    }

    private Player getPlayer() {
        return getEntrance().getClient().getPlayer();
    }

    @Override
    public BuildResult updateContext(Player player, UnaryOperator<Context> updater) {
        var context = updater.apply(getContext(player));
        if (context.isFulfilled()) {

            var finalizedContext = context.finalize(player, BuildStage.INTERACT);
            var previewContext = finalizedContext.withBuildType(BuildType.PREVIEW_ONCE);
            var result = new BatchBuildSession(player, previewContext).commit();
            getEntrance().getChannel().sendPacket(new PlayerBuildPacket(finalizedContext));
            showBuildContextResult(context.id(), 1024, player, context, result);

            playSoundInBatch(player, result);
//            showBuildTooltip(context.id(), 1024, player, previewContext, result);
            getEntrance().getClientManager().getTooltipRenderer().hideEntry(generateId(player.getId(), Context.class), 0, true);
            setContext(player, context.newInteraction());

            return BuildResult.COMPLETED;
        } else {
            setContext(player, context);
            if (context.isIdle()) {
                return BuildResult.CANCELED;
            } else {
                return BuildResult.PARTIAL;
            }
        }
    }

    record TypedBlockSound(
            SoundType soundType,
            BlockState blockState
    ) {
        enum SoundType {
            BREAK,
            PLACE,
            HIT,
            FAIL,
        }

        static TypedBlockSound breakSound(BlockState blockState) {
            return new TypedBlockSound(SoundType.BREAK, blockState);
        }

        static TypedBlockSound failSound(BlockState blockState) {
            return new TypedBlockSound(SoundType.FAIL, blockState);
        }

        static TypedBlockSound placeSound(BlockState blockState) {
            return new TypedBlockSound(SoundType.PLACE, blockState);
        }

        static TypedBlockSound hitSound(BlockState blockState) {
            return new TypedBlockSound(SoundType.HIT, blockState);
        }
    }

    private void playSoundInBatch(Player player, BatchOperationResult batchOperationResult) {
        var soundMap = new HashMap<TypedBlockSound, Integer>();
        for (var operationResult : batchOperationResult.getResult()) {
            if (soundMap.size() >= 4) {
                break;
            }
            if (operationResult instanceof BlockOperationResult blockOperationResult) {
                switch (blockOperationResult.getOperation().getType()) {
                    case BREAK -> {
                        if (blockOperationResult.result().success()) {
                            soundMap.compute(TypedBlockSound.breakSound(blockOperationResult.getOperation().getBlockState()), (o, i) -> i == null ? 1 : i + 1);
                        } else {
                            soundMap.compute(TypedBlockSound.failSound(blockOperationResult.getOperation().getBlockState()), (o, i) -> i == null ? 1 : i + 1);
                        }
                    }
                    case PLACE -> {
                        if (blockOperationResult.result().success()) {
                            soundMap.compute(TypedBlockSound.placeSound(blockOperationResult.getOperation().getBlockState()), (o, i) -> i == null ? 1 : i + 1);
                        } else {
                            soundMap.compute(TypedBlockSound.failSound(blockOperationResult.getOperation().getBlockState()), (o, i) -> i == null ? 1 : i + 1);
                        }
                    }
                    case INTERACT -> {
                        if (blockOperationResult.result().success()) {
                            soundMap.compute(TypedBlockSound.hitSound(blockOperationResult.getOperation().getBlockState()), (o, i) -> i == null ? 1 : i + 1);
                        } else {
                            soundMap.compute(TypedBlockSound.failSound(blockOperationResult.getOperation().getBlockState()), (o, i) -> i == null ? 1 : i + 1);
                        }
                    }
                }
            }
        }
        var context = batchOperationResult.getOperation().getContext();
        var nearestInteraction = context.interactions().results().stream().filter(Objects::nonNull).min(Comparator.comparing(interaction1 -> interaction1.getBlockPosition().getCenter().distance(player.getEyePosition())));
        if (nearestInteraction.isEmpty()) {
            return;
        }
        var distance = player.getEyePosition().distance(nearestInteraction.get().getBlockPosition().getCenter());
        var location = player.getEyePosition().add(player.getEyeDirection().mul(Math.min(distance, 12)));
        for (var entry : soundMap.entrySet()) {
            var typedSound = entry.getKey();
            var count = entry.getValue();
            for (int i = 0; i <= MathUtils.min(count / 2, 4); i++) {
                if (typedSound.blockState() == null) {
                    continue;
                }
                var sound = switch (typedSound.soundType()) {
                    case BREAK ->
                            SoundInstance.createBlock(typedSound.blockState().getSoundSet().breakSound(), (typedSound.blockState().getSoundSet().volume() + 1.0F) / 2.0F, typedSound.blockState().getSoundSet().pitch() * 0.8F, location);
                    case PLACE ->
                            SoundInstance.createBlock(typedSound.blockState().getSoundSet().placeSound(), (typedSound.blockState().getSoundSet().volume() + 1.0F) / 2.0F, typedSound.blockState().getSoundSet().pitch() * 0.8F, location);
                    case HIT ->
                            SoundInstance.createBlock(typedSound.blockState().getSoundSet().hitSound(), (typedSound.blockState().getSoundSet().volume() + 1.0F) / 2.0F, typedSound.blockState().getSoundSet().pitch() * 0.8F, location);
                    case FAIL ->
                            SoundInstance.createBlock(typedSound.blockState().getSoundSet().hitSound(), (typedSound.blockState().getSoundSet().volume() + 1.0F) / 3.0F, typedSound.blockState().getSoundSet().pitch() * 0.5F, location);
                };
                getPlayer().getClient().getSoundManager().playDelayed(sound, i);
            }

        }

    }

    public void onSessionConfig(SessionConfig sessionConfig) {
        for (var uuid : getAllContexts().keySet()) {
            var config = sessionConfig.getByPlayer(uuid);
            getAllContexts().computeIfPresent(uuid, (uuid1, context) -> context.withConstraintConfig(config));
        }
    }

    @Override
    public Context getDefaultContext(Player player) {
        var serverSessionConfig = getEntrance().getSessionManager().getServerSessionConfig();
        if (serverSessionConfig == null) {
            return Context.defaultSet().withConstraintConfig(ConstraintConfig.EMPTY);
        }
        return Context.defaultSet().withConstraintConfig(serverSessionConfig.getByPlayer(player));
    }

    @Override
    public Context getContext(Player player) {
        return contexts.computeIfAbsent(player.getId(), uuid -> getDefaultContext(player));
    }

    private Context getHistoryContext(Player player) {
        return historyContexts.computeIfAbsent(player.getId(), uuid -> getDefaultContext(player));
    }

    private Context putHistoryContext(Player player, Context context) {
        return historyContexts.put(player.getId(), context);
    }

    @Override
    public Context getContextTraced(Player player) {
        var context = getContext(player).finalize(player, BuildStage.INTERACT);
        if (context.isInteractionEmpty()) {
            if (player.getItemStack(InteractionHand.MAIN).isEmpty() || !player.getItemStack(InteractionHand.MAIN).isBlock()) {
                context = context.withBuildState(BuildState.INTERACT_BLOCK);
            } else {
                context = context.withBuildState(BuildState.PLACE_BLOCK);
            }
        }
        return context.withNextInteraction(context.trace(player));
    }

    @Override
    public Map<UUID, Context> getAllContexts() {
        return contexts;
    }

    @Override
    public boolean setContext(Player player, Context context) {
        contexts.put(player.getId(), context);
        return true;
    }

    public boolean checkPermission(Player player) {
        if (!isSessionValid(player)) {
            getEntrance().getSessionManager().notifyPlayer();
            return false;
        }
        if (!isPermissionGranted(player)) {
            player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_permission")));
            return false;
        }
        return true;
    }

    @Override
    public boolean setStructure(Player player, Structure structure) {
        if (!checkPermission(player)) {
            return false;
        }
        updateContext(player, context -> context.withNoInteraction().withStructure(structure));
        if (structure.getMode().isDisabled()) {
            getEntrance().getClientManager().getTooltipRenderer().hideAllEntries(false);
            updateContext(player, context -> context.withPattern(context.pattern().withEnabled(false)));
        }
        return true;
    }

    @Override
    public boolean setReplaceMode(Player player, ReplaceMode replaceMode) {
        if (!checkPermission(player)) {
            return false;
        }
        updateContext(player, context -> context.withReplaceMode(replaceMode));
        return true;
    }

    @Override
    public boolean setPattern(Player player, Pattern pattern) {
        if (!checkPermission(player)) {
            return false;
        }
        updateContext(player, context -> context.withPattern(pattern).finalize(player, BuildStage.SET_PATTERN));
        return true;
    }

    @Override
    public void resetAll() {
        lastClientPlayerLevel.set(null);
        contexts.clear();
        undoRedoStacks.clear();
        getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), config.patternConfig()));
    }

    public EventResult onPlayerInteract(Player player, InteractionType type, InteractionHand hand) {
        if (getEntrance().getConfigStorage().get().builderConfig().passiveMode())
            if (!EffortlessKeys.PASSIVE_BUILD_MODIFIER.getBinding().isDown() && !getContext(player).isBuilding()) {
                return EventResult.pass();
            }

        var buildResult = updateContext(player, context -> {

            var state = switch (type) {
                case ATTACK -> BuildState.BREAK_BLOCK;
                case USE_ITEM -> {
                    if (player.getItemStack(hand).isEmpty() || !player.getItemStack(hand).isBlock()) {
                        yield BuildState.INTERACT_BLOCK;
                    }
                    yield BuildState.PLACE_BLOCK;
                }
                case UNKNOWN -> BuildState.IDLE;
            };

            var interaction = context.withBuildState(state).trace(player);
            var nextContext = context.withBuildState(state).withNextInteraction(interaction);

            if (interaction == null) {
                return context.newInteraction();
            }
            if (interaction.getTarget() == Interaction.Target.MISS) {
                var traced = player.raytrace(Short.MAX_VALUE, 0f, false);
                var message = Text.empty().append(" (").append(Text.text(String.valueOf(MathUtils.round(traced.getPosition().distance(player.getEyePosition())))).withStyle(ChatFormatting.RED)).append(Text.text("/")).append(Text.text(String.valueOf(context.configs().constraintConfig().maxReachDistance()))).append(Text.text(")"));
                player.sendClientMessage(Text.translate("effortless.message.building.cannot_reach_target").append(message), true);
                return context.newInteraction();
            }
            if (interaction.getTarget() == Interaction.Target.ENTITY) {
                player.sendClientMessage(Text.translate("effortless.message.building.cannot_reach_entity"), true);
                return context.newInteraction();
            }
            if (context.isBuilding() && context.buildState() != state) {
                player.sendClientMessage(Text.translate("effortless.message.building.build_canceled"), true);
                return context.newInteraction();
            }

            if (!context.withBuildState(state).hasPermission()) {
                if (state == BuildState.BREAK_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_break_permission")));
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_break_blocks_no_permissio"), true);
                }
                if (state == BuildState.PLACE_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_place_permission")));
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_place_blocks_no_permission"), true);
                }
                if (state == BuildState.INTERACT_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_interact_permission")));
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_interact_blocks_no_permission"), true);
                }
                return context.newInteraction();
            }

            if (!nextContext.isBoxVolumeInBounds()) {
                if (nextContext.buildState() == BuildState.BREAK_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_break_blocks_box_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
                }
                if (nextContext.buildState() == BuildState.PLACE_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_place_blocks_box_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
                }
                if (nextContext.buildState() == BuildState.PLACE_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_interact_blocks_box_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
                }
                return context.newInteraction();
            }

            return nextContext;
        });

        if (buildResult.isSuccess()) {
            player.swing(hand);
        }

        return EventResult.interrupt(buildResult.isSuccess());
    }

    @Override
    public void onContextReceived(Player player, Context context) {
        if (context.isBuildType()) {
            return; // handle on server
        }
        var result = new BatchBuildSession(player, context).commit();

        showBuildContextResult(player.getId(), 1024, player, context, result);
        showBuildTooltip(context.id(), 1024, player, context, result);

        if (context.isPreviewOnceType()) {
            playSoundInBatch(player, result);
        }

    }

    public void onHistoryResultReceived(Player player, BuildTooltip buildTooltip) {
        switch (buildTooltip.type()) {
            case BUILD_SUCCESS -> {
//        showBuildContextResult(player.getId(), 1024, player, context, result);
                showBuildTooltip(buildTooltip.context().id(), 1024, player, buildTooltip.context(), buildTooltip.itemSummary());

            }
            default -> {

                if (buildTooltip.context().buildMode() == BuildMode.DISABLED) { // nothing
                    var entries = new ArrayList<>();
                    entries.add(buildTooltip.itemSummary().values().stream().flatMap(List::stream).toList());
                    entries.add(Text.translate("effortless.history." + buildTooltip.type().getName()));
                    entries.add(buildTooltip.context().buildMode().getIcon());
                    getEntrance().getClientManager().getTooltipRenderer().showGroupEntry(UUID.randomUUID(), 1024 + 1, entries, true);
                } else {
                    showBuildTooltip(buildTooltip.context().id(), 1024, player, buildTooltip.context(), buildTooltip.itemSummary());
                }


            }
        }

    }

    @Override
    public OperationResultStack getOperationResultStack(Player player) {
        return null;
    }

    @Override
    public void undo(Player player) {
        if (!checkPermission(player)) {
            return;
        }
        getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.UNDO));
    }

    @Override
    public void redo(Player player) {
        if (!checkPermission(player)) {
            return;
        }
        getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.REDO));
    }

    public void onClientTick(Client client, ClientTick.Phase phase) {
        if (phase == ClientTick.Phase.END) {
            return;
        }
        if (getEntrance().getClient() == null || getPlayer() == null) {
            resetAll();
            return;
        }

        var player = getPlayer();

        if (!isSessionValid(player)) {
            resetContext(player);
            return;
        }

        if (!isPermissionGranted(player)) {
            resetContext(player);
            return;
        }

        if (player.isDeadOrDying()) {
            resetContextInteractions(player);
            return;
        }

        if (!player.getWorld().getDimensionId().location().equals(lastClientPlayerLevel.get())) {
            resetContextInteractions(player);
            lastClientPlayerLevel.set(player.getWorld().getDimensionId().location());
            return;
        }

        if (getContext(player).isDisabled()) {
            return;
        }

        if (getEntrance().getConfigStorage().get().builderConfig().passiveMode() && !EffortlessKeys.PASSIVE_BUILD_MODIFIER.getBinding().isDown() && !getContext(player).isBuilding()) {
            getEntrance().getClientManager().getTooltipRenderer().hideEntry(generateId(player.getId(), Context.class), 0, false);
            return;
        }

        reloadContext(player);

        var context1 = getContextTraced(player);
        var context = context1.withBuildType(BuildType.PREVIEW);

        if (context.getBoxVolume() > getEntrance().getConfigStorage().get().renderConfig().maxRenderVolume()) {
            showBuildContextResult(player.getId(), 0, player, context, null);
            showBuildTooltip(player.getId(), 0, player, context, Map.of());
        } else {
            var result = new BatchBuildSession(player, context.withBuildType(BuildType.PREVIEW)).commit();
            showBuildContextResult(player.getId(), 0, player, context, result);
            showBuildTooltip(player.getId(), 0, player, context, result);
        }

        if (!getContext(player).isIdle()) {
            showBuildMessage(player, context);
        }

        ;
        if (getHistoryContext(player).getBoxVolume() != context.getBoxVolume()) {
            putHistoryContext(player, context);
            var nearestInteraction = context.interactions().results().stream().filter(Objects::nonNull).min(Comparator.comparing(interaction1 -> interaction1.getBlockPosition().getCenter().distance(player.getEyePosition())));
            if (nearestInteraction.isEmpty()) {
                return;
            }
            var blockState = Items.AIR.item().getBlock().getDefaultBlockState();
            var distance = player.getEyePosition().distance(nearestInteraction.get().getBlockPosition().getCenter());
            var location = player.getEyePosition().add(player.getEyeDirection().mul(Math.min(distance, 3)));
            var sound = SoundInstance.createBlock(blockState.getSoundSet().hitSound(), (blockState.getSoundSet().volume() + 1.0F) / 2.0F * 0.1F, blockState.getSoundSet().pitch() * 0.2F, location);
            getEntrance().getClient().getSoundManager().play(sound);
        }

        getEntrance().getChannel().sendPacket(new PlayerBuildPacket(context));
    }

    private void reloadContext(Player player) {
        setContext(player, getContext(player).finalize(player, BuildStage.TICK));


//        if (Keys.KEY_LEFT_CONTROL.getBinding().isKeyDown()) {
//            setContext(player, getContext(player).withBuildFeature(PlaneLength.EQUAL));
//        } else {
//            setContext(player, getContext(player).withBuildFeature(PlaneLength.VARIABLE));
//        }
    }

    private boolean isSessionValid(Player player) {
        return getEntrance().getSessionManager().isSessionValid();
    }

    private boolean isPermissionGranted(Player player) {
        return getEntrance().getSessionManager().getServerSessionConfig().getByPlayer(player).allowUseMod();
    }

    private UUID generateId(UUID uuid, Object tag) {
        return new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() + tag.hashCode());
    }

    public void showBuildContextResult(UUID uuid, int priority, Player player, Context context, OperationResult result) {
        if (player.getId() != getPlayer().getId()) {
            if (!getEntrance().getConfigStorage().get().renderConfig().showOtherPlayersBuild()) {
                return;
            }
        }
        getEntrance().getClientManager().getPatternRenderer().showPattern(uuid, context);

        if (context.interactions().isEmpty()) {
            getEntrance().getClientManager().getOutlineRenderer().remove(generateId(uuid, BoundingBox3d.class));
        } else {
            var box = BoundingBox3d.fromLowerCornersOf(context.interactions().results().stream().filter(Objects::nonNull).map(BlockInteraction::getBlockPosition).map(BlockPosition::toVector3i).toArray(Vector3i[]::new));
            getEntrance().getClientManager().getOutlineRenderer().showBoundingBox(generateId(uuid, BoundingBox3d.class), box)
                    .texture(OutlineRenderLayers.CHECKERED_THIN_TEXTURE_LOCATION)
                    .lightMap(LightTexture.FULL_BLOCK)
                    .disableNormals()
                    .colored(Color.DARK_GRAY)
                    .stroke(1 / 64f);
        }

        if (result instanceof BatchOperationResult batchOperationResult) {
            getEntrance().getClientManager().getOperationsRenderer().showResult(uuid, result);

            var resultMap = batchOperationResult.getResult().stream().filter(BlockOperationResult.class::isInstance).map(BlockOperationResult.class::cast).filter(blockOperationResult -> BlockOperationRenderer.getColorByOpResult(blockOperationResult) != null).collect(Collectors.groupingBy(BlockOperationRenderer::getColorByOpResult));

            for (var allColor : BlockOperationRenderer.getAllColors()) {
                if (resultMap.get(allColor) == null) {
                    getEntrance().getClientManager().getOutlineRenderer().remove(generateId(uuid, allColor));
                    continue;
                }
                var locations = resultMap.get(allColor).stream().map(BlockOperationResult::getOperation).map(BlockOperation::getBlockPosition).filter(Objects::nonNull).toList();
                getEntrance().getClientManager().getOutlineRenderer().showCluster(generateId(uuid, allColor), locations)
                        .texture(OutlineRenderLayers.CHECKERED_THIN_TEXTURE_LOCATION)
                        .lightMap(LightTexture.FULL_BLOCK)
                        .disableNormals()
                        .colored(allColor)
                        .stroke(1 / 64f);
            }
        } else {
            getEntrance().getClientManager().getOperationsRenderer().remove(uuid);
            for (var allColor : BlockOperationRenderer.getAllColors()) {
                getEntrance().getClientManager().getOutlineRenderer().remove(generateId(uuid, allColor));
            }
        }
    }

    public void showBuildTooltip(UUID id, int priority, Player player, Context context, OperationResult result) {
        var itemSummary = Arrays.stream(OperationSummaryType.values()).collect(Collectors.toMap(Function.identity(), result::getSummary));
        showBuildTooltip(id, priority, player, context, itemSummary);
    }

    public void showBuildTooltip(UUID id, int priority, Player player, Context context, Map<OperationSummaryType, List<ItemStack>> itemSummary) {
        if (player.getId() != getPlayer().getId()) {
            if (!getEntrance().getConfigStorage().get().renderConfig().showOtherPlayersBuildTooltips()) {
                return;
            }
        }
        var entries = new ArrayList<>();

        if (!itemSummary.isEmpty()) {
            var allProducts = new ArrayList<ItemStack>();
            for (var entry : itemSummary.entrySet()) {
                var products = entry.getValue();
                if (!products.isEmpty()) {
                    var color = switch (entry.getKey()) {
                        case BLOCKS_PLACED -> ChatFormatting.WHITE;
                        case BLOCKS_DESTROYED -> ChatFormatting.RED;
                        case BLOCKS_INTERACTED -> ChatFormatting.YELLOW;
                        case BLOCKS_NOT_PLACEABLE -> ChatFormatting.GRAY;
                        case BLOCKS_NOT_BREAKABLE -> ChatFormatting.GRAY;
                        case BLOCKS_NOT_INTERACTABLE -> ChatFormatting.GRAY;
                        case BLOCKS_ITEMS_INSUFFICIENT -> ChatFormatting.RED;
                        case BLOCKS_TOOLS_INSUFFICIENT -> ChatFormatting.GRAY;
                        case BLOCKS_BLACKLISTED -> ChatFormatting.GRAY;
                        case BLOCKS_NO_PERMISSION -> ChatFormatting.GRAY;
                    };
                    products = products.stream().map(stack -> ItemStackUtils.putColorTag(stack, color.getColor())).toList();
                    entries.add(products);
                    entries.add(Text.translate("effortless.build.summary." + entry.getKey().name().toLowerCase(Locale.ROOT)).withStyle(color));
                    allProducts.addAll(products);
                }
            }
            if (allProducts.isEmpty()) {
                entries.add(Text.translate("effortless.build.summary.no_item_summary").withStyle(ChatFormatting.GRAY));
            }
        } else {
            entries.add(Text.translate("effortless.build.summary.pending_item_summary").withStyle(ChatFormatting.GRAY));
        }


        var texts = new ArrayList<Tuple2<Text, Text>>();
        texts.add(new Tuple2<>(Text.translate("effortless.build.summary.structure").withStyle(ChatFormatting.WHITE), context.buildMode().getDisplayName().withStyle(ChatFormatting.GOLD)));
        texts.add(new Tuple2<>(AbstractWheelScreen.button(context.replaceMode()).getCategory().withStyle(ChatFormatting.WHITE), AbstractWheelScreen.button(context.replaceMode()).getName().withStyle(ChatFormatting.GOLD)));

        for (var supportedFeature : context.structure().getSupportedFeatures()) {
            var option = context.buildFeatures().stream().filter(feature -> Objects.equals(feature.getCategory(), supportedFeature.getName())).findFirst();
            if (option.isEmpty()) continue;
            var button = AbstractWheelScreen.button(option.get());
            texts.add(new Tuple2<>(button.getCategory().withStyle(ChatFormatting.WHITE), button.getName().withStyle(ChatFormatting.GOLD)));
        }
        if (context.pattern().enabled()) {
            texts.add(new Tuple2<>(Text.translate("effortless.build.summary.pattern").withStyle(ChatFormatting.WHITE), (context.pattern().enabled() ? Text.translate("effortless.build.summary.pattern_enabled") : Text.translate("effortless.build.summary.pattern_disabled")).withStyle(ChatFormatting.GOLD)));
        }

        entries.add(texts);

        entries.add(context.buildMode().getIcon());
        getEntrance().getClientManager().getTooltipRenderer().showGroupEntry(generateId(id, Context.class), priority, entries, context.buildType() == BuildType.BUILD);

    }

    public void showBuildMessage(Player player, Context context) {
        var message = Text.empty();
        if (context.tracingResult().isSuccess()) {
            message = message.append(context.buildState().getDisplayName())
                    .append(" ")
                    .append(context.buildMode().getDisplayName().withStyle(ChatFormatting.GOLD))
                    .append(" ")
                    .append("(")
                    .append(Text.text(String.valueOf(context.getInteractionBox().x())).withStyle(ChatFormatting.WHITE))
                    .append("x")
                    .append(Text.text(String.valueOf(context.getInteractionBox().y())).withStyle(ChatFormatting.WHITE))
                    .append("x")
                    .append(Text.text(String.valueOf(context.getInteractionBox().z())).withStyle(ChatFormatting.WHITE))
                    .append("=")
                    .append(Text.text(String.valueOf(context.getBoxVolume())).withStyle(!context.isBoxVolumeInBounds() ? ChatFormatting.RED : ChatFormatting.WHITE))
                    .append(")");
        } else {
            switch (context.tracingResult()) {
                case SUCCESS_FULFILLED -> {
                }
                case SUCCESS_PARTIAL -> {
                }
                case PASS -> {
                }
                case FAILED -> {
                    message = message.append(Text.translate("effortless.message.building.cannot_reach_target").withStyle(ChatFormatting.WHITE));
                    var interaction = context.interactions().results().stream().filter(result -> result != null && result.getTarget() == Interaction.Target.MISS).findAny();
                    if (interaction.isPresent()) {
                        message = message.append(" (").append(Text.text(String.valueOf(MathUtils.round(interaction.get().getBlockPosition().toVector3i().distance(player.getPosition().toVector3i())))).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(context.configs().constraintConfig().maxReachDistance())).append(")");
                    }
                }
            }
        }

        player.sendClientMessage(message, true);


    }

}
