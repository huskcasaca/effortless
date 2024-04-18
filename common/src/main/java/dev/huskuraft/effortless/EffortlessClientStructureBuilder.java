package dev.huskuraft.effortless;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.lang.Tuple2;
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.renderer.LightTexture;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import dev.huskuraft.effortless.building.BatchBuildSession;
import dev.huskuraft.effortless.building.BuildResult;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.MultiSelectFeature;
import dev.huskuraft.effortless.building.SingleCommand;
import dev.huskuraft.effortless.building.SingleSelectFeature;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.building.TracingResult;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.operation.BlockPositionLocatable;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.ItemSummaryType;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.renderer.opertaion.children.BlockOperationPreview;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderLayers;
import dev.huskuraft.effortless.screen.radial.AbstractRadialScreen;
import dev.huskuraft.effortless.session.config.GeneralConfig;
import dev.huskuraft.effortless.session.config.SessionConfig;

public final class EffortlessClientStructureBuilder extends StructureBuilder {

    private final EffortlessClient entrance;

    private final Map<UUID, Context> contexts = new HashMap<>();
    private final Map<UUID, OperationResultStack> undoRedoStacks = new HashMap<>();

    public EffortlessClientStructureBuilder(EffortlessClient entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getClientTickEvent().register(this::onClientTick);
    }

    private static String getStateComponent(BuildState state) {
        return Text.translate("effortless.state.%s".formatted(
                switch (state) {
                    case IDLE -> "idle";
                    case PLACE_BLOCK -> "place_block";
                    case BREAK_BLOCK -> "break_block";
                }
        )).getString();
    }

    private static String getTracingComponent(TracingResult result) {
        return Text.translate("effortless.tracing.%s".formatted(
                switch (result) {
                    case SUCCESS_FULFILLED -> "success_fulfilled";
                    case SUCCESS_PARTIAL -> "success_partial";
                    case PASS -> "pass";
                    case FAILED -> "failed";
                }
        )).getString();
    }

    private EffortlessClient getEntrance() {
        return entrance;
    }

    @Override
    public BuildResult build(Player player, BuildState state) {
        return build(player, state, null);
    }

    @Override
    public BuildResult build(Player player, BuildState state, @Nullable BlockInteraction interaction) {
        return updateContext(player, context -> {
            if (interaction == null) {
                return context.newInteraction();
            }
            if (interaction.getTarget() == Interaction.Target.MISS) {
                player.sendClientMessage(Text.translate("effortless.message.building.cannot_reach_target"), true);
                return context.newInteraction();
            }
            if (interaction.getTarget() == Interaction.Target.ENTITY) {
                player.sendClientMessage(Text.translate("effortless.message.building.cannot_reach_entity"), true);
                return context.newInteraction();
            }
            if (context.isBuilding() && context.state() != state) {
                player.sendClientMessage(Text.translate("effortless.message.building.build_canceled"), true);
                return context.newInteraction();
            }
            if (!context.hasPermission()) {
                if (state == BuildState.PLACE_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.cannot_place_blocks_no_permission")));
                    player.sendClientMessage(Text.translate("effortless.message.building.no_place_permission"), true);
                }
                if (state == BuildState.BREAK_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.building.cannot_break_blocks_no_permission")));
                    player.sendClientMessage(Text.translate("effortless.message.building.no_break_permission"), true);
                }
                return context.newInteraction();
            }

            var nextContext = context.withState(state).withNextInteraction(interaction);

            if (!nextContext.isBoxVolumeInBounds()) {
                if (nextContext.state() == BuildState.PLACE_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_place_blocks_box_volume_too_large") + " (" + nextContext.getBoxVolume() + "/" + nextContext.getMaxBoxVolume() + ")", true);
                }
                if (nextContext.state() == BuildState.BREAK_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_break_blocks_box_volume_too_large") + " (" + nextContext.getBoxVolume() + "/" + nextContext.getMaxBoxVolume() + ")", true);
                }
                return context.newInteraction();
            }

            if (!nextContext.isBoxSideLengthInBounds()) {
                if (nextContext.state() == BuildState.PLACE_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_place_blocks_box_side_length_too_large") + " (" + nextContext.getBoxSideLength() + "/" + nextContext.getMaxBoxSideLength() + ")", true);
                }
                if (nextContext.state() == BuildState.BREAK_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_break_blocks_box_side_length_too_large") + " (" + nextContext.getBoxSideLength() + "/" + nextContext.getMaxBoxSideLength() + ")", true);
                }
                return context.newInteraction();
            }

            return nextContext;
        });
    }

    @Override
    public BuildResult updateContext(Player player, UnaryOperator<Context> updater) {
        var context = updater.apply(getContext(player));
        if (context.isFulfilled()) {

            var finalizedContext = context.finalize(player, BuildStage.INTERACT);
            var previewContext = finalizedContext.withPreviewOnceType();
            var result = new BatchBuildSession(player.getWorld(), player, finalizedContext).build().commit();

//            if (finalizedContext.type() != BuildType.COMMAND) {
                getEntrance().getChannel().sendPacket(new PlayerBuildPacket(finalizedContext));
//            }

            showContext(context.getId(), context);
            showOperationResult(context.getId(), result);
//            showOperationResultTooltip(context.getId(), 1024, player, result);
//            showContextTooltip(context.getId(), 1024, context);
            showBuildTooltip(context.getId(), 1024, player, context, result);
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

    public void onSessionConfig(SessionConfig sessionConfig) {
        for (var uuid : getAllContexts().keySet()) {
            var config = sessionConfig.getPlayerConfig(uuid);
            getAllContexts().computeIfPresent(uuid, (uuid1, context) -> context.withGeneralConfig(config));
        }
    }

    @Override
    public Context getDefaultContext(Player player) {
        var serverSessionConfig = getEntrance().getSessionManager().getServerSessionConfig();
        if (serverSessionConfig == null) {
            return Context.defaultSet().withGeneralConfig(GeneralConfig.EMPTY);
        }
        return Context.defaultSet().withGeneralConfig(serverSessionConfig.getPlayerConfig(player));
    }

    @Override
    public Context getContext(Player player) {
        return contexts.computeIfAbsent(player.getId(), uuid -> getDefaultContext(player));
    }

    @Override
    public Context getContextTraced(Player player) {
        var context = getContext(player).finalize(player, BuildStage.INTERACT);
        if (context.isInteractionEmpty()) {
            if (player.getItemStack(InteractionHand.MAIN).isEmpty()) {
                context = context.withBreakingState();
            } else {
                context = context.withPlacingState();
            }
        }
        return context.withNextInteractionTraced(player);
    }

    @Override
    public Map<UUID, Context> getAllContexts() {
        return contexts;
    }

    @Override
    public void setContext(Player player, Context context) {
        contexts.put(player.getId(), context);
    }

    // from settings screen
    @Override
    public void setBuildMode(Player player, BuildMode buildMode) {
        if (!isSessionValid(player)) {
            getEntrance().getSessionManager().notifyPlayer();
            return;
        }
        if (!isPermissionGranted(player)) {
            player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_permission")));
            return;
        }
        updateContext(player, context -> context.withEmptyInteractions().withBuildMode(buildMode));
    }

    @Override
    public void setBuildFeature(Player player, SingleSelectFeature feature) {
        if (!isSessionValid(player)) {
            getEntrance().getSessionManager().notifyPlayer();
            return;
        }
        if (!isPermissionGranted(player)) {
            player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_permission")));
            return;
        }
        updateContext(player, context -> context.withBuildFeature(feature));
    }

    public boolean isSessionValid(Player player) {
        return getEntrance().getSessionManager().isSessionValid();
    }

    public boolean isPermissionGranted(Player player) {
        return getEntrance().getSessionManager().getServerSessionConfig().getPlayerConfig(player).allowUseMod();
    }

    @Override
    public void setBuildFeature(Player player, MultiSelectFeature feature) {
        updateContext(player, context -> {
            var features = context.buildFeatures().stream().filter(f -> f.getClass().equals(feature.getClass())).collect(Collectors.toSet());
            if (features.contains(feature)) {
                if (features.size() > 1) {
                    features.remove(feature);
                }
            } else {
                features.add(feature);
            }
            return context.withBuildFeature(features);
        });
    }

    @Override
    public void setPattern(Player player, Pattern pattern) {
        updateContext(player, context -> {
            return context.withPattern(pattern).finalize(player, BuildStage.UPDATE_CONTEXT);
        });
    }

    @Override
    public void resetAll() {
        lastClientPlayerLevel.set(null);
        contexts.clear();
        undoRedoStacks.clear();
    }

    @Override
    public BuildResult onPlayerBreak(Player player) {
        var context = getContext(player);
        var interaction = context.withBreakingState().trace(player);
        return build(player, BuildState.BREAK_BLOCK, interaction);
    }

    @Override
    public BuildResult onPlayerPlace(Player player) {
        var context = getContext(player);

        if (player.getItemStack(InteractionHand.MAIN).isEmpty()) {
            return BuildResult.CANCELED;
        }

        var interaction = context.withPlacingState().trace(player);
        var result = build(player, BuildState.PLACE_BLOCK, interaction);

        if (result.isSuccess()) {
//            player.swing(PlayerHand.MAIN);
        }
        return result;
    }

    @Override
    public void onContextReceived(Player player, Context context) {
        var result = new BatchBuildSession(player.getWorld(), player, context).build().commit();

        showContext(player.getId(), context);
        showOperationResult(player.getId(), result);

//        showOperationResultTooltip(context.getId(), 1, player, result);

        showBuildTooltip(context.getId(), 1, player, context, result);
    }

    @Override
    public OperationResultStack getOperationResultStack(Player player) {
        return null;
    }

    @Override
    public void undo(Player player) {
        getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.UNDO));
    }

    @Override
    public void redo(Player player) {
        getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.REDO));
    }

    private final AtomicReference<ResourceLocation> lastClientPlayerLevel = new AtomicReference<>();

    public void onClientTick(Client client, ClientTick.Phase phase) {
        if (phase == ClientTick.Phase.END) {
            return;
        }
        if (getEntrance().getClient() == null || getEntrance().getClient().getPlayer() == null) {
            resetAll();
            return;
        }

        var player = getEntrance().getClient().getPlayer();

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

        if (!player.getWorld().getDimension().location().equals(lastClientPlayerLevel.get())) {
            resetContextInteractions(player);
            lastClientPlayerLevel.set(player.getWorld().getDimension().location());
            return;
        }

        if (getContext(player).isDisabled()) {
            return;
        }

        setContext(player, getContext(player).withRandomPatternSeed());
        var context = getContextTraced(player).withPreviewType();

        var result = new BatchBuildSession(player.getWorld(), player, context.withPreviewType()).build().commit();

        showContext(player.getId(), context);
        showOperationResult(player.getId(), result);

//        showOperationResultTooltip(player.getId(), 0, player, result);
//        showContextTooltip(player.getId(), 0, context);

        showBuildTooltip(player.getId(), 0, player, context, result);
        if (!getContext(player).isIdle()) {
            showClientMessage(player, context);
        }

        getEntrance().getChannel().sendPacket(new PlayerBuildPacket(context));
    }

    private UUID nextIdByTag(UUID uuid, Object tag) {
        return new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() + tag.hashCode());
    }

    public void showContext(UUID uuid, Context context) {
        getEntrance().getClientManager().getPatternRenderer().showPattern(uuid, context);
        if (context.interactions().isMissing() || context.interactions().isEmpty()) {
            getEntrance().getClientManager().getOutlineRenderer().showBoundingBox(nextIdByTag(uuid, BoundingBox3d.class), BoundingBox3d.ofSize(Vector3d.ZERO, 0, 0, 0));
        } else {
            var box = BoundingBox3d.fromLowerCornersOf(context.interactions().results().stream().map(BlockInteraction::getBlockPosition).toArray(Vector3i[]::new));
            getEntrance().getClientManager().getOutlineRenderer().showBoundingBox(nextIdByTag(uuid, BoundingBox3d.class), box)
                    .texture(OutlineRenderLayers.CHECKERED_THIN_TEXTURE_LOCATION)
                    .lightMap(LightTexture.FULL_BLOCK)
                    .disableNormals()
                    .colored(Color.DARK_GRAY)
                    .stroke(1 / 64f);
        }

    }

    public void showOperationResult(UUID uuid, OperationResult result) {
        getEntrance().getClientManager().getOperationsRenderer().showResult(uuid, result);
        if (result instanceof BatchOperationResult batchOperationResult) {

            var resultMap = batchOperationResult.getResult().stream().filter(BlockOperationResult.class::isInstance).map(BlockOperationResult.class::cast).filter(blockOperationResult -> BlockOperationPreview.getColorByOpResult(blockOperationResult) != null).collect(Collectors.groupingBy(BlockOperationPreview::getColorByOpResult));

            for (var allColor : BlockOperationPreview.getAllColors()) {
                if (resultMap.get(allColor) == null) continue;
                var locations =  resultMap.get(allColor).stream().map(OperationResult::getOperation).filter(BlockPositionLocatable.class::isInstance).map(BlockPositionLocatable.class::cast).map(BlockPositionLocatable::locate).filter(Objects::nonNull).toList();
                getEntrance().getClientManager().getOutlineRenderer().showCluster(nextIdByTag(batchOperationResult.getOperation().getContext().getId(), allColor), locations)
                        .texture(OutlineRenderLayers.CHECKERED_THIN_TEXTURE_LOCATION)
                        .lightMap(LightTexture.FULL_BLOCK)
                        .disableNormals()
                        .colored(allColor)
                        .stroke(1 / 64f);
            }


        }
    }

    public void showBuildTooltip(UUID id, int priority, Player player, Context context, OperationResult result) {
        var entries = new ArrayList<>();

        var texts = new ArrayList<Tuple2<Text, Text>>();
        texts.add(new Tuple2<>(Text.translate("effortless.build.summary.structure").withStyle(TextStyle.WHITE), context.buildMode().getDisplayName().withStyle(TextStyle.GOLD)));
        var replace = AbstractRadialScreen.button(context.replaceMode());
        texts.add(new Tuple2<>(replace.getDisplayCategory().withStyle(TextStyle.WHITE), replace.getDisplayName().withStyle(TextStyle.GOLD)));

        for (var supportedFeature : context.buildMode().getSupportedFeatures()) {
            var option = context.buildFeatures().stream().filter(feature -> Objects.equals(feature.getCategory(), supportedFeature.getName())).findFirst();
            if (option.isEmpty()) continue;
            var button = AbstractRadialScreen.button(option.get());
            texts.add(new Tuple2<>(button.getDisplayCategory().withStyle(TextStyle.WHITE), button.getDisplayName().withStyle(TextStyle.GOLD)));
        }

        entries.add(texts);
//        getEntrance().getClientManager().getTooltipRenderer().showAsGroup(nextIdByTag(id, Context.class), priority, entries);
//        entries.clear();
        entries.add(context.buildMode().getIcon());
        getEntrance().getClientManager().getTooltipRenderer().showGroupEntry(nextIdByTag(id, BuildMode.class), priority, entries);
        entries.clear();

        for (var itemType : ItemSummaryType.values()) {
            var products = result.getProducts(itemType);
            if (!products.isEmpty()) {
                var color = switch (itemType) {
                    case BLOCKS_PLACED -> TextStyle.WHITE;
                    case BLOCKS_DESTROYED -> TextStyle.RED;
                    case BLOCKS_PLACE_INSUFFICIENT -> TextStyle.RED;
                    case BLOCKS_BREAK_INSUFFICIENT -> TextStyle.RED;
                    case BLOCKS_NOT_PLACEABLE -> TextStyle.GRAY;
                    case BLOCKS_NOT_BREAKABLE -> TextStyle.GRAY;
                    case BLOCKS_PLACE_NOT_WHITELISTED -> TextStyle.GRAY;
                    case BLOCKS_BREAK_NOT_WHITELISTED -> TextStyle.GRAY;
                    case BLOCKS_PLACE_BLACKLISTED -> TextStyle.GRAY;
                    case BLOCKS_BREAK_BLACKLISTED -> TextStyle.GRAY;
                };
                entries.add(products.stream().map(stack -> ItemStackUtils.putColorTag(stack, color.getColor())).toList());
                entries.add(Text.translate("effortless.build.summary." + itemType.name().toLowerCase(Locale.ROOT)).withStyle(color));
            }
            if (!entries.isEmpty()) {
                getEntrance().getClientManager().getTooltipRenderer().showGroupEntry(nextIdByTag(id, itemType), priority, entries);
            } else {
                getEntrance().getClientManager().getTooltipRenderer().showEmptyEntry(nextIdByTag(id, itemType), priority);
            }
            entries.clear();
        }

    }

    public void showClientMessage(Player player, Context context) {
        var message = "";
        if (!context.tracingResult().isSuccess()) {
            switch (context.tracingResult()) {
                case SUCCESS_FULFILLED -> {
                }
                case SUCCESS_PARTIAL -> {
                }
                case PASS -> {
                }
                case FAILED -> {
                    message = Text.translate("effortless.message.building.cannot_reach_target").withStyle(TextStyle.WHITE).getString();
                }
            }
        } else {
            message = getStateComponent(context.state()) + " "
                    + context.buildMode().getDisplayName().withStyle(TextStyle.GOLD) + " "
                    + "("
                    + (context.getInteractionBox().x() > context.getMaxBoxSideLength() ? TextStyle.RED : TextStyle.WHITE) + context.getInteractionBox().x() + TextStyle.RESET
                    + "x"
                    + (context.getInteractionBox().y() > context.getMaxBoxSideLength() ? TextStyle.RED : TextStyle.WHITE) + context.getInteractionBox().y() + TextStyle.RESET
                    + "x"
                    + (context.getInteractionBox().z() > context.getMaxBoxSideLength() ? TextStyle.RED : TextStyle.WHITE) + context.getInteractionBox().z() + TextStyle.RESET
                    + "="
                    + (context.getInteractionBox().volume() > context.getMaxBoxVolume() ? TextStyle.RED : TextStyle.WHITE) + context.getInteractionBox().volume() + TextStyle.RESET
                    + ")";
        }

        player.sendClientMessage(message, true);


    }

}
