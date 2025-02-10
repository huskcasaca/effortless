package dev.huskuraft.effortless;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.Interaction;
import dev.huskuraft.universal.api.core.InteractionHand;
import dev.huskuraft.universal.api.core.InteractionType;
import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.core.Items;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.core.Tuple2;
import dev.huskuraft.universal.api.events.EventResult;
import dev.huskuraft.universal.api.events.lifecycle.ClientTick;
import dev.huskuraft.universal.api.math.BoundingBox3d;
import dev.huskuraft.universal.api.math.MathUtils;
import dev.huskuraft.universal.api.math.Vector3i;
import dev.huskuraft.universal.api.platform.Client;
import dev.huskuraft.universal.api.renderer.LightTexture;
import dev.huskuraft.universal.api.sound.SoundInstance;
import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.BuildResult;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.SingleCommand;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.building.clipboard.SnapshotTransform;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.operation.ItemSummary;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.OperationTooltip;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockOperation;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.Replace;
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
            setContext(player, getContext(getPlayer()).newInteraction());

            var finalizedContext = context.finalize(player, BuildStage.INTERACT);
            var clientContext = finalizedContext.withBuildType(BuildType.BUILD_CLIENT);
            var result = new BatchBuildSession(getEntrance(), player, clientContext).commit();
            getEntrance().getChannel().sendPacket(new PlayerBuildPacket(getPlayer().getId(), finalizedContext));
            showContext(context.id(), 1024, player, context, result);

            playSoundInBatch(player, result);
            showTooltip(context.id(), 1024, player, result.getTooltip());
            getEntrance().getClientManager().getTooltipRenderer().hideEntry(generateId(player.getId(), Context.class), 0, true);

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
        for (var operationResult : batchOperationResult.getResults()) {
            if (soundMap.size() >= 4) {
                break;
            }
            if (operationResult instanceof BlockOperationResult blockOperationResult) {
                if (blockOperationResult.getBlockStateForRenderer() == null) {
                    continue;
                }
                switch (blockOperationResult.getOperation().getType()) {
                    case UPDATE -> {
                        if (!blockOperationResult.getBlockStateForRenderer().isAir()) {
                            if (blockOperationResult.result().success()) {
                                soundMap.compute(TypedBlockSound.placeSound(blockOperationResult.getBlockStateForRenderer()), (o, i) -> i == null ? 1 : i + 1);
                            } else {
                                soundMap.compute(TypedBlockSound.failSound(blockOperationResult.getBlockStateForRenderer()), (o, i) -> i == null ? 1 : i + 1);
                            }
                        } else {
                            if (blockOperationResult.result().success()) {
                                soundMap.compute(TypedBlockSound.breakSound(blockOperationResult.getBlockStateForRenderer()), (o, i) -> i == null ? 1 : i + 1);
                            } else {
                                soundMap.compute(TypedBlockSound.failSound(blockOperationResult.getBlockStateForRenderer()), (o, i) -> i == null ? 1 : i + 1);
                            }
                        }
                    }
                    case INTERACT -> {
                        if (blockOperationResult.result().success()) {
                            soundMap.compute(TypedBlockSound.hitSound(blockOperationResult.getBlockStateForRenderer()), (o, i) -> i == null ? 1 : i + 1);
                        } else {
                            soundMap.compute(TypedBlockSound.failSound(blockOperationResult.getBlockStateForRenderer()), (o, i) -> i == null ? 1 : i + 1);
                        }
                    }
                    case COPY -> {
                        soundMap.compute(TypedBlockSound.hitSound(blockOperationResult.getBlockStateForRenderer()), (o, i) -> i == null ? 1 : i + 1);
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
        var constraintConfig = getEntrance().getSessionManager().getServerSessionConfig();
        var builderConfig = getEntrance().getConfigStorage().get().builderConfig();
        if (constraintConfig == null) {
            return Context.defaultSet().withConstraintConfig(ConstraintConfig.EMPTY).withBuilderConfig(builderConfig);
        }
        return Context.defaultSet().withConstraintConfig(constraintConfig.getByPlayer(player)).withBuilderConfig(builderConfig);
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
            if (context.clipboard().enabled()) {
                if (context.clipboard().isEmpty()) {
                    context = context.withBuildState(BuildState.COPY_STRUCTURE);
                } else {
                    context = context.withBuildState(BuildState.PASTE_STRUCTURE);
                }
            } else {
                if (player.getItemStack(InteractionHand.MAIN).isBlock()) {
                    context = context.withBuildState(BuildState.PLACE_BLOCK);
                } else if (player.getItemStack(InteractionHand.MAIN).isDamageableItem()) {
                    context = context.withBuildState(BuildState.BREAK_BLOCK);
                } else {
                    context = context.withBuildState(BuildState.INTERACT_BLOCK);
                }
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
            player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.no_permission")));
            return false;
        }
        return true;
    }

    @Override
    public boolean setStructure(Player player, Structure structure) {
        if (!checkPermission(player)) {
            return false;
        }
        updateContext(player, context -> context.withNoInteraction().withStructure(structure).withEmptyClipboard());
        if (structure.getMode().isDisabled()) {
            getEntrance().getClientManager().getTooltipRenderer().hideAllEntries(false);
            updateContext(player, context -> context);
        }
        return true;
    }

    @Override
    public boolean setClipboard(Player player, Clipboard clipboard) {
        if (!checkPermission(player)) {
            return false;
        }
        updateContext(player, context -> context.newInteraction().withClipboard(clipboard));
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
    public boolean setReplace(Player player, Replace replace) {
        if (!checkPermission(player)) {
            return false;
        }
        updateContext(player, context -> context.withReplace(replace));
        return true;
    }

    @Override
    public void resetAll() {
        lastClientPlayerLevel.set(null);
        contexts.clear();
        undoRedoStacks.clear();
        getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), config.patternConfig(), config.clipboardConfig()));
    }

    public EventResult onPlayerInteract(Player player, InteractionType type, InteractionHand hand) {
        if (getEntrance().getConfigStorage().get().builderConfig().passiveMode())
            if (!EffortlessKeys.PASSIVE_BUILD_MODIFIER.getKeyBinding().isDown() && !getContext(player).isBuilding()) {
                return EventResult.pass();
            }

        if (type == InteractionType.UNKNOWN) {
            return EventResult.pass();
        }

        var buildResult = updateContext(player, context -> {

            var state = switch (type) {
                case ATTACK -> {
                    if (context.clipboard().enabled()) {
                        yield BuildState.COPY_STRUCTURE;
                    } else {
                        yield BuildState.BREAK_BLOCK;
                    }
                }
                case USE_ITEM -> {
                    if (context.clipboard().enabled()) {
                        yield BuildState.PASTE_STRUCTURE;
                    } else {
                        if (player.getItemStack(hand).isEmpty() || !player.getItemStack(hand).isBlock()) {
                            yield BuildState.INTERACT_BLOCK;
                        }
                        yield BuildState.PLACE_BLOCK;
                    }
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
                player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.cannot_reach_target").append(message)));
//                player.sendClientMessage(Text.translate("effortless.message.building.client.cannot_reach_target").append(message), true);
                return context.newInteraction();
            }
            if (interaction.getTarget() == Interaction.Target.ENTITY) {
                player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.cannot_reach_entity")));
//                player.sendClientMessage(Text.translate("effortless.message.building.client.cannot_reach_entity"), true);
                return context.newInteraction();
            }
            if (context.isBuilding() && context.buildState() != state) {
                switch (context.buildState()) {
                    case BREAK_BLOCK -> player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.block_breaking_canceled")));
                    case PLACE_BLOCK -> player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.block_placing_canceled")));
                    case INTERACT_BLOCK -> player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.block_interacting_canceled")));
                    case COPY_STRUCTURE -> player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.structure_copying_canceled")));
                    case PASTE_STRUCTURE -> player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.structure_pasting_canceled")));
                }
                return context.newInteraction();
            }
            if (context.buildState() == BuildState.IDLE && state == BuildState.COPY_STRUCTURE && !context.clipboard().snapshot().isEmpty()) {
                player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.structure_pasting_canceled")));
//                player.sendClientMessage(Text.translate("effortless.message.building.client.structure_pasting_canceled"), true);
                return context.newInteraction().withEmptyClipboard();
            }

            if (!context.withBuildState(state).hasPermission()) {
                if (state == BuildState.BREAK_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.no_block_break_permission")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.no_block_break_permission"), true);
                }
                if (state == BuildState.PLACE_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.no_block_place_permission")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.no_block_place_permission"), true);
                }
                if (state == BuildState.INTERACT_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.no_block_interact_permission")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.no_block_interact_permission"), true);
                }
                if (state == BuildState.COPY_STRUCTURE) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.no_structure_copy_permission")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.no_structure_copy_permission"), true);
                }
                if (state == BuildState.PASTE_STRUCTURE) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.no_structure_paste_permission")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.no_structure_paste_permission"), true);
                }
                return context.newInteraction();
            }

            if (!nextContext.isVolumeInBounds()) {
                if (nextContext.buildState() == BuildState.BREAK_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.block_break_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxVolume())).append(")")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.block_break_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
                }
                if (nextContext.buildState() == BuildState.PLACE_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.block_place_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxVolume())).append(")")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.block_place_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
                }
                if (nextContext.buildState() == BuildState.INTERACT_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.block_interact_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxVolume())).append(")")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.block_interact_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
                }
                if (nextContext.buildState() == BuildState.COPY_STRUCTURE) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.structure_copy_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxVolume())).append(")")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.structure_copy_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
                }
                if (nextContext.buildState() == BuildState.PASTE_STRUCTURE) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.server.structure_paste_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxVolume())).append(")")));
//                    player.sendClientMessage(Text.translate("effortless.message.building.client.structure_paste_volume_too_large").append(" (").append(Text.text(String.valueOf(nextContext.getBoxVolume())).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(nextContext.getMaxBoxVolume())).append(")"), true);
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
            return; // handle on server, will never happen
        }
        var result = new BatchBuildSession(getEntrance(), player, context).commit();

        showContext(player.getId(), 1024, player, context, result);
        showTooltip(context.id(), 1024, player, result.getTooltip());

        if (context.isBuildClientType()) {
            playSoundInBatch(player, result);
        }
    }

    public void onTooltipReceived(Player player, OperationTooltip operationTooltip) {
        switch (operationTooltip.type()) {
            case BUILD -> {
                showTooltip(operationTooltip.context().id(), 1024, player, operationTooltip);
            }
            default -> {
                if (operationTooltip.context().buildMode() == BuildMode.DISABLED) { // nothing
                    var entries = new ArrayList<>();
                    entries.add(operationTooltip.itemSummary().values().stream().flatMap(List::stream).toList());
                    entries.add(Text.translate("effortless.history." + operationTooltip.type().getName()));
                    entries.add(operationTooltip.context().buildMode().getIcon());
                    getEntrance().getClientManager().getTooltipRenderer().showGroupEntry(UUID.randomUUID(), 1024 + 1, entries, true);
                } else {
                    showTooltip(operationTooltip.context().id(), 1024, player, operationTooltip);
                }
            }
        }
    }

    public void onSnapshotCaptured(Player player, Snapshot snapshot) {
        updateContext(player, context -> context.withClipboard(context.clipboard().withSnapshot(snapshot)));
    }

    public void updateClipboard(Player player, SnapshotTransform action) {
        updateContext(player, context -> context.withClipboard(context.clipboard().withSnapshot(context.clipboard().snapshot().update(action))));
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
            resetInteractions(player);
            return;
        }

        if (!player.getWorld().getDimensionId().location().equals(lastClientPlayerLevel.get())) {
            resetInteractions(player);
            lastClientPlayerLevel.set(player.getWorld().getDimensionId().location());
            return;
        }

        if (getContext(player).isDisabled()) {
            clearBuildMessage(player);
            return;
        }

        if (getEntrance().getConfigStorage().get().builderConfig().passiveMode() && !EffortlessKeys.PASSIVE_BUILD_MODIFIER.getKeyBinding().isDown() && !getContext(player).isBuilding()) {
            getEntrance().getClientManager().getTooltipRenderer().hideEntry(generateId(player.getId(), Context.class), 0, false);
            return;
        }

        reloadContext(player);

        var context1 = getContextTraced(player);
        var context = context1.withBuildType(BuildType.PREVIEW);

        if (context.getVolume() > getEntrance().getConfigStorage().get().renderConfig().maxRenderVolume()) {
            showContext(player.getId(), 0, player, context, null);
            showTooltip(player.getId(), 0, player, OperationTooltip.build(context));
        } else {
            var result = new BatchBuildSession(getEntrance(), player, context.withBuildType(BuildType.PREVIEW)).commit();
            showContext(player.getId(), 0, player, context, result);
            showTooltip(player.getId(), 0, player, result.getTooltip());
        }

        showBuildMessage(player, context);

        if (getHistoryContext(player).getVolume() != context.getVolume()) {
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

        getEntrance().getChannel().sendPacket(new PlayerBuildPacket(getPlayer().getId(), context));
    }

    private void reloadContext(Player player) {
        setContext(player, getContext(player).finalize(player, BuildStage.TICK));


//        if (Keys.KEY_LEFT_CONTROL.getKeyBinding().isKeyDown()) {
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

    public void showContext(UUID uuid, int priority, Player player, Context context, OperationResult result) {
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
                    .stroke(1 / 32f);
        }

        if (result instanceof BatchOperationResult batchOperationResult) {
            getEntrance().getClientManager().getOperationsRenderer().showResult(uuid, result);

            var resultMap = batchOperationResult.getResults().stream().filter(BlockOperationResult.class::isInstance).map(BlockOperationResult.class::cast).filter(blockOperationResult -> BlockOperationRenderer.getColorByOpResult(blockOperationResult) != null).collect(Collectors.groupingBy(BlockOperationRenderer::getColorByOpResult));

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
                        .stroke(1 / 32f);
            }
        } else {
            getEntrance().getClientManager().getOperationsRenderer().remove(uuid);
            for (var allColor : BlockOperationRenderer.getAllColors()) {
                getEntrance().getClientManager().getOutlineRenderer().remove(generateId(uuid, allColor));
            }
        }
    }

    public void showTooltip(UUID id, int priority, Player player, OperationTooltip tooltip) {
        var context = tooltip.context();

        if (player == null) {
            player = getPlayer();
        }

        if (player.getId() != getPlayer().getId()) {
            if (!getEntrance().getConfigStorage().get().renderConfig().showOtherPlayersBuildTooltips()) {
                return;
            }
        }
        if (player.getGameMode().isSpectator()) {
            getEntrance().getClientManager().getTooltipRenderer().hideEntry(generateId(id, Context.class), priority, false);
            return;
        }
        var entries = new ArrayList<>();

        var blockStateSummary = tooltip.itemSummary();
        if (!blockStateSummary.isEmpty()) {
            var allProducts = new ArrayList<ItemStack>();
            for (var summary : ItemSummary.values()) {
                var items = blockStateSummary.getOrDefault(summary, List.of());
                if (items.isEmpty()) {
                    continue;
                }
                var color = switch (summary) {
                    case BLOCKS_PLACED -> ChatFormatting.WHITE;
                    case BLOCKS_DESTROYED -> ChatFormatting.RED;
                    case BLOCKS_INTERACTED -> ChatFormatting.YELLOW;
                    case BLOCKS_COPIED -> ChatFormatting.GREEN;
                    case BLOCKS_NOT_REPLACEABLE -> ChatFormatting.GRAY;
                    case BLOCKS_NOT_BREAKABLE -> ChatFormatting.GRAY;
                    case BLOCKS_NOT_INTERACTABLE -> ChatFormatting.GRAY;
                    case BLOCKS_NOT_COPYABLE -> ChatFormatting.GRAY;
                    case BLOCKS_ITEMS_INSUFFICIENT -> ChatFormatting.RED;
                    case BLOCKS_TOOLS_INSUFFICIENT -> ChatFormatting.GRAY;
                    case BLOCKS_BLACKLISTED -> ChatFormatting.GRAY;
                    case BLOCKS_NO_PERMISSION -> ChatFormatting.GRAY;

                    case CONTAINER_CONSUMED -> ChatFormatting.WHITE;
                    case CONTAINER_DROPPED -> ChatFormatting.WHITE;
                };
                entries.add(new Tuple2<>(items, color.getColor()));
                entries.add(Text.translate("effortless.build.summary." + summary.name().toLowerCase(Locale.ROOT)).withStyle(color));
                allProducts.addAll(items);
            }
            if (allProducts.isEmpty()) {
                entries.add(Text.translate("effortless.build.summary.no_item_summary").withStyle(ChatFormatting.GRAY));
            }
        } else {
            entries.add(Text.translate("effortless.build.summary.pending_item_summary").withStyle(ChatFormatting.GRAY));
        }


        var texts = new ArrayList<Tuple2<Text, Text>>();
        texts.add(new Tuple2<>(Text.translate("effortless.build.summary.structure").withStyle(ChatFormatting.WHITE), context.buildMode().getDisplayName().withStyle(ChatFormatting.GOLD)));
        texts.add(new Tuple2<>(AbstractWheelScreen.button(context.replaceStrategy()).getCategory().withStyle(ChatFormatting.WHITE), AbstractWheelScreen.button(context.replaceStrategy()).getName().withStyle(ChatFormatting.GOLD)));

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
        getEntrance().getClientManager().getTooltipRenderer().showGroupEntry(generateId(id, Context.class), priority, entries, context.isBuildType());

    }

    private boolean isBuildMessageVisible = false;

    public void clearBuildMessage(Player player) {
        if (this.isBuildMessageVisible) {
            player.sendMessage(Text.empty(), true);
            this.isBuildMessageVisible = false;
        }

    }

    public void showBuildMessage(Player player, Context context) {
        var dimensions = Stream.of(context.getInteractionBox().x(), context.getInteractionBox().y(), context.getInteractionBox().z()).filter(i -> i > 1).toList();
        if (dimensions.isEmpty()) {
            dimensions = List.of(1);
        }
        var message = Text.empty();
        if (context.tracingResult().isSuccess()) {
            message = message.append(context.buildState().getDisplayName(context.buildMode()))
                    .append(" ")
                    .append("(")
                    .append(dimensions.stream().map(String::valueOf).collect(Collectors.joining("x")))
                    .append("=")
                    .append(Text.text(String.valueOf(context.getVolume())).withStyle(!context.isVolumeInBounds() ? ChatFormatting.RED : ChatFormatting.WHITE))
                    .append(")");
        } else {
            message = Text.empty();
//            switch (context.tracingResult()) {
//                case SUCCESS_FULFILLED -> {
//                }
//                case SUCCESS_PARTIAL -> {
//                }
//                case PASS -> {
//                }
//                case FAILED -> {
//                    message = message.append(Text.translate("effortless.message.building.client.cannot_reach_target").withStyle(ChatFormatting.WHITE));
//                    var interaction = context.interactions().results().stream().filter(result -> result != null && result.getTarget() == Interaction.Target.MISS).findAny();
//                    if (interaction.isPresent()) {
//                        message = message.append(" (").append(Text.text(String.valueOf(MathUtils.round(interaction.get().getBlockPosition().toVector3i().distance(player.getPosition().toVector3i())))).withStyle(ChatFormatting.RED)).append("/").append(String.valueOf(context.configs().constraintConfig().maxReachDistance())).append(")");
//                    }
//                }
//            }
        }

        player.sendMessage(message, true);
        this.isBuildMessageVisible = true;


    }

}
