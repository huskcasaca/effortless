package dev.huskuraft.effortless;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
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
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.renderer.LightTexture;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.text.TextStyle;
import dev.huskuraft.effortless.building.BatchBuildSession;
import dev.huskuraft.effortless.building.BuildResult;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.MultiSelectFeature;
import dev.huskuraft.effortless.building.SingleCommand;
import dev.huskuraft.effortless.building.SingleSelectFeature;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.building.TracingResult;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.operation.BlockPositionLocatable;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderLayers;
import dev.huskuraft.effortless.screen.radial.AbstractRadialScreen;

public final class EffortlessClientStructureBuilder extends StructureBuilder {

    private final EffortlessClient entrance;

    private final Map<UUID, Context> contexts = new HashMap<>();
    private final Map<UUID, OperationResultStack> undoRedoStacks = new HashMap<>();

    public EffortlessClientStructureBuilder(EffortlessClient entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getClientTickEvent().register(this::onClientTick);
    }

    private static Text getStateComponent(BuildState state) {
        return Text.translate("effortless.state.%s".formatted(
                switch (state) {
                    case IDLE -> "idle";
                    case PLACE_BLOCK -> "place_block";
                    case BREAK_BLOCK -> "break_block";
                }
        ));
    }

    private static Text getTracingComponent(TracingResult result) {
        return Text.translate("effortless.tracing.%s".formatted(
                switch (result) {
                    case SUCCESS_FULFILLED -> "success_fulfilled";
                    case SUCCESS_PARTIAL -> "success_partial";
                    case PASS -> "pass";
                    case FAILED -> "failed";
                }
        ));
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
            if (interaction.getTarget() != Interaction.Target.BLOCK) {
                return context.newInteraction();
            }
            if (context.isBuilding() && context.state() != state) {
                return context.newInteraction();
            }
            return context.withState(state).withNextInteraction(interaction);
        });
    }

    @Override
    public BuildResult updateContext(Player player, UnaryOperator<Context> updater) {
        var context = updater.apply(getContext(player));
        if (context.isFulfilled()) {

            var finalizedContext = context.finalize(player, BuildStage.INTERACT);
            var previewContext = finalizedContext.withPreviewOnceType();
            var result = new BatchBuildSession(player.getWorld(), player, finalizedContext).build().commit();

            if (finalizedContext.type() != BuildType.COMMAND) {
                getEntrance().getChannel().sendPacket(new PlayerBuildPacket(finalizedContext));
            }

            showContext(context.uuid(), context);
            showOperationResult(context.uuid(), result);
            showOperationResultTooltip(context.uuid(), player, result, 1000);
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

    @Override
    public Context getDefaultContext() {
        return Context.defaultSet(false);
    }

    @Override
    public Context getContext(Player player) {
        return contexts.computeIfAbsent(player.getId(), uuid -> getDefaultContext());
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
    public void setContext(Player player, Context context) {
        contexts.put(player.getId(), context);
    }

    // from settings screen
    @Override
    public void setBuildMode(Player player, BuildMode buildMode) {
        updateContext(player, context -> context.withEmptyInteractions().withBuildMode(buildMode));
    }

    @Override
    public void setBuildFeature(Player player, SingleSelectFeature feature) {
        updateContext(player, context -> context.withBuildFeature(feature));
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

        showOperationResultTooltip(context.uuid(), player, result, 1);
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

//        notifySession();

        setContext(player, getContext(player).withRandomPatternSeed());
        var context = getContextTraced(player).withPreviewType();

        var result = new BatchBuildSession(player.getWorld(), player, context.withPreviewType()).build().commit();

        showContext(player.getId(), context);
        showOperationResult(player.getId(), result);

        showContextTooltip(player.getId(), context, 0);
        showOperationResultTooltip(player.getId(), player, result, 1);

        getEntrance().getChannel().sendPacket(new PlayerBuildPacket(context));
    }

    private UUID nextIdByTag(UUID uuid, String tag) {
        return new UUID(uuid.getMostSignificantBits() + tag.hashCode(), uuid.getLeastSignificantBits());
    }

    public void showContext(UUID uuid, Context context) {
        getEntrance().getClientManager().getPatternRenderer().showPattern(uuid, context);
        if (!context.isMissingHit() && !context.interactions().isEmpty()) {
            var box = BoundingBox3d.fromLowerCornersOf(context.interactions().results().stream().map(BlockInteraction::getBlockPosition).toArray(Vector3i[]::new));
            getEntrance().getClientManager().getOutlineRenderer().showBoundingBox(nextIdByTag(uuid, "boundingBox"), box)
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
            for (var colorListEntry : batchOperationResult.getResultByColor().entrySet()) {
                var locations =  colorListEntry.getValue().stream().map(OperationResult::getOperation).filter(BlockPositionLocatable.class::isInstance).map(BlockPositionLocatable.class::cast).map(BlockPositionLocatable::locate).filter(Objects::nonNull).toList();
                var cluster = getEntrance().getClientManager().getOutlineRenderer().showCluster(colorListEntry.getKey().toString() + batchOperationResult.getOperation().getContext().uuid(), locations)
                        .texture(OutlineRenderLayers.CHECKERED_THIN_TEXTURE_LOCATION)
                        .lightMap(LightTexture.FULL_BLOCK)
                        .disableNormals()
                        .colored(colorListEntry.getKey())
                        .stroke(1 / 64f);
//                switch (result.getOperation().getContext().state()) {
//                    case IDLE -> {
//                    }
//                    case PLACE_BLOCK -> cluster.colored(SurfaceColor.COLOR_WHITE);
//                    case BREAK_BLOCK -> cluster.colored(SurfaceColor.COLOR_RED);
//                }
            }

//
//            var cluster = getEntrance().getClientManager().getOutlineRenderer().showCluster(batchOperationResult.getOperation().getContext().uuid(), batchOperationResult.locations())
//                    .texture(OutlineRenderLayers.CHECKERED_TEXTURE_LOCATION)
//                    .lightMap(LightTexture.FULL_BLOCK)
//                    .disableNormals()
//                    .stroke(1 / 64f);
//            switch (result.getOperation().getContext().state()) {
//                case IDLE -> {
//                }
//                case PLACE_BLOCK -> cluster.colored(SurfaceColor.COLOR_WHITE);
//                case BREAK_BLOCK -> cluster.colored(SurfaceColor.COLOR_RED);
//            }
        }
    }

    public void showOperationResultTooltip(UUID uuid, Player player, OperationResult result, int priority) {
        getEntrance().getClientManager().getTooltipRenderer().showTitledItems(nextIdByTag(uuid, "placed"), Text.translate("effortless.build.summary.placed_blocks", player.getDisplayName()).withStyle(TextStyle.WHITE), result.getProducts(ItemType.PLAYER_USED), priority);
        getEntrance().getClientManager().getTooltipRenderer().showTitledItems(nextIdByTag(uuid, "destroyed"), Text.translate("effortless.build.summary.destroyed_blocks", player.getDisplayName()).withStyle(TextStyle.RED), result.getProducts(ItemType.WORLD_DROPPED), priority);
    }

    public void showContextTooltip(UUID uuid, Context context, int priority) {
        var texts = new ArrayList<Text>();
        texts.add(Text.translate("effortless.build.summary.structure").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(context.buildMode().getDisplayName().withStyle(TextStyle.GOLD)));
        var replace = AbstractRadialScreen.button(context.replaceMode());
        texts.add(replace.getDisplayCategory().withStyle(TextStyle.WHITE).append(Text.text(" ")).append(replace.getDisplayName().withStyle(TextStyle.GOLD)));

        for (var supportedFeature : context.buildMode().getSupportedFeatures()) {
            var option = context.buildFeatures().stream().filter(feature -> Objects.equals(feature.getCategory(), supportedFeature.getName())).findFirst();
            if (option.isEmpty()) continue;
            var button = AbstractRadialScreen.button(option.get());
            texts.add(button.getDisplayCategory().withStyle(TextStyle.WHITE).append(Text.text(" ")).append(button.getDisplayName().withStyle(TextStyle.GOLD)));
        }

//        texts.add(Text.translate("effortless.build.summary.state").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(getStateComponent(context.state()).withStyle(TextStyle.GOLD)));
        texts.add(Text.translate("effortless.build.summary.tracing").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(getTracingComponent(context.tracingResult()).withStyle(TextStyle.GOLD)));

        getEntrance().getClientManager().getTooltipRenderer().showMessages(nextIdByTag(uuid, "info"), texts, priority);
    }

}
