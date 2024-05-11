package dev.huskuraft.effortless;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Interaction;
import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.core.ItemStack;
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
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.BuildResult;
import dev.huskuraft.effortless.building.BuildStage;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.MultiSelectFeature;
import dev.huskuraft.effortless.building.SingleCommand;
import dev.huskuraft.effortless.building.SingleSelectFeature;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.history.HistoryResult;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.operation.ItemSummaryType;
import dev.huskuraft.effortless.building.operation.Operation;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.operation.block.BlockOperationResult;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.session.BatchBuildSession;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;
import dev.huskuraft.effortless.renderer.opertaion.children.BlockOperationRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderLayers;
import dev.huskuraft.effortless.screen.radial.AbstractWheelScreen;
import dev.huskuraft.effortless.session.config.GeneralConfig;
import dev.huskuraft.effortless.session.config.SessionConfig;

public final class EffortlessClientStructureBuilder extends StructureBuilder {

    private final EffortlessClient entrance;

    private final Map<UUID, Context> contexts = new HashMap<>();
    private final Map<UUID, OperationResultStack> undoRedoStacks = new HashMap<>();
    private final AtomicReference<ResourceLocation> lastClientPlayerLevel = new AtomicReference<>();

    public EffortlessClientStructureBuilder(EffortlessClient entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getClientTickEvent().register(this::onClientTick);
    }

    private static String getStateComponent(BuildState state) {
        return Text.translate("effortless.state.%s".formatted(
                switch (state) {
                    case IDLE -> "idle";
                    case PLACE_BLOCK -> "placing_block";
                    case BREAK_BLOCK -> "breaking_block";
                    case INTERACT_BLOCK -> "interacting_block";
                }
        )).getString();
    }

//    private static String getTracingComponent(TracingResult result) {
//        return Text.translate("effortless.tracing.%s".formatted(
//                switch (result) {
//                    case SUCCESS_FULFILLED -> "success_fulfilled";
//                    case SUCCESS_PARTIAL -> "success_partial";
//                    case PASS -> "pass";
//                    case FAILED_TRACE -> "failed";
//                    case FAILED_OVERFLOW -> null;
//                }
//        )).getString();
//    }

    private EffortlessClient getEntrance() {
        return entrance;
    }

    @Override
    public BuildResult updateContext(Player player, UnaryOperator<Context> updater) {
        var context = updater.apply(getContext(player));
        if (context.isFulfilled()) {

            var finalizedContext = context.finalize(player, BuildStage.INTERACT);
            var previewContext = finalizedContext.withBuildType(BuildType.PREVIEW_ONCE);
            var result = new BatchBuildSession(player.getWorld(), player, finalizedContext).build().commit();

//            if (finalizedContext.buildType() != BuildType.COMMAND) {
            getEntrance().getChannel().sendPacket(new PlayerBuildPacket(finalizedContext));
//            }

            showBuildContextResult(context.id(), 1024, player, context, result);
            showBuildTooltip(context.id(), 1024, player, context, result);
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
        if (buildMode.isDisabled()) {
            getEntrance().getClientManager().getTooltipRenderer().hideAllEntries(false);
            updateContext(player, context -> context.withPattern(context.pattern().withEnabled(false)));
        }
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

    @Override
    public void setBuildFeature(Player player, MultiSelectFeature feature) {
        if (!isPermissionGranted(player)) {
            player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_permission")));
            return;
        }
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
        if (!isPermissionGranted(player)) {
            player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_permission")));
            return;
        }
        updateContext(player, context -> {
            return context.withPattern(pattern).finalize(player, BuildStage.UPDATE_CONTEXT);
        });
    }

    @Override
    public void resetAll() {
        lastClientPlayerLevel.set(null);
        contexts.clear();
        undoRedoStacks.clear();
        getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), config.transformerPresets(), false));
    }

    public EventResult onPlayerInteract(Player player, InteractionType type, InteractionHand hand) {
        if (getEntrance().getConfigStorage().get().passiveMode() && !EffortlessKeys.PASSIVE_BUILD_MODIFIER.getBinding().isDown() && !getContext(player).isBuilding()) {
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
                var message = " (" + ChatFormatting.RED + MathUtils.round(traced.getPosition().distance(player.getEyePosition())) + ChatFormatting.RESET + "/" + context.customParams().generalConfig().maxReachDistance() + ")";
                player.sendClientMessage(Text.translate("effortless.message.building.cannot_reach_target").getString() + message, true);
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
                if (state == BuildState.PLACE_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_place_permission")));
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_place_blocks_no_permission"), true);
                }
                if (state == BuildState.BREAK_BLOCK) {
                    player.sendMessage(Effortless.getSystemMessage(Text.translate("effortless.message.permissions.no_break_permission")));
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_break_blocks_no_permissio"), true);
                }
                return context.newInteraction();
            }

            if (!nextContext.isBoxVolumeInBounds()) {
                if (nextContext.buildState() == BuildState.PLACE_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_place_blocks_box_volume_too_large").getString() + " (" + nextContext.getBoxVolume() + "/" + nextContext.getMaxBoxVolume() + ")", true);
                }
                if (nextContext.buildState() == BuildState.BREAK_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_break_blocks_box_volume_too_large").getString() + " (" + nextContext.getBoxVolume() + "/" + nextContext.getMaxBoxVolume() + ")", true);
                }
                return context.newInteraction();
            }

            if (!nextContext.isBoxSideLengthInBounds()) {
                if (nextContext.buildState() == BuildState.PLACE_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_place_blocks_box_side_length_too_large").getString() + " (" + nextContext.getBoxSideLength() + "/" + nextContext.getMaxBoxSideLength() + ")", true);
                }
                if (nextContext.buildState() == BuildState.BREAK_BLOCK) {
                    player.sendClientMessage(Text.translate("effortless.message.building.cannot_break_blocks_box_side_length_too_large").getString() + " (" + nextContext.getBoxSideLength() + "/" + nextContext.getMaxBoxSideLength() + ")", true);
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
        var result = new BatchBuildSession(player.getWorld(), player, context).build().commit();

        showBuildContextResult(player.getId(), 1, player, context, result);
        showBuildTooltip(context.id(), 1, player, context, result);
    }

    public void onHistoryResultReceived(Player player, HistoryResult historyResult) {
        var entries = new ArrayList<>();
        entries.add(historyResult.itemSummary().values().stream().flatMap(List::stream).toList());
        entries.add(Text.translate("effortless.history." + historyResult.type().getName()));
        entries.add(historyResult.context().buildMode().getIcon());
        getEntrance().getClientManager().getTooltipRenderer().showGroupEntry(UUID.randomUUID(), 1, entries, true);
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

        if (!player.getWorld().getDimensionId().location().equals(lastClientPlayerLevel.get())) {
            resetContextInteractions(player);
            lastClientPlayerLevel.set(player.getWorld().getDimensionId().location());
            return;
        }

        if (getContext(player).isDisabled()) {
            return;
        }

        if (getEntrance().getConfigStorage().get().passiveMode() && !EffortlessKeys.PASSIVE_BUILD_MODIFIER.getBinding().isDown() && !getContext(player).isBuilding()) {
            getEntrance().getClientManager().getTooltipRenderer().hideEntry(generateId(player.getId(), Context.class), 0, false);
            return;
        }

        reloadContext(player);

        Context context1 = getContextTraced(player);
        var context = context1.withBuildType(BuildType.PREVIEW);

        if (context.getBoxVolume() > getEntrance().getConfigStorage().get().renderConfig().maxRenderVolume()) {
            showBuildContextResult(player.getId(), 0, player, context, null);
            showBuildTooltip(player.getId(), 0, player, context, null);
        } else {
            var result = new BatchBuildSession(player.getWorld(), player, context.withBuildType(BuildType.PREVIEW)).build().commit();
            showBuildContextResult(player.getId(), 0, player, context, result);
            showBuildTooltip(player.getId(), 0, player, context, result);
        }

        if (!getContext(player).isIdle()) {
            showBuildMessage(player, context);
        }

        getEntrance().getChannel().sendPacket(new PlayerBuildPacket(context));
    }

    private void reloadContext(Player player) {
        setContext(player, getContext(player).withRandomPatternSeed());
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
        return getEntrance().getSessionManager().getServerSessionConfig().getPlayerConfig(player).allowUseMod();
    }

    private UUID generateId(UUID uuid, Object tag) {
        return new UUID(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits() + tag.hashCode());
    }

    public void showBuildContextResult(UUID uuid, int priority, Player player, Context context, OperationResult result) {
        if (player.getId() != getEntrance().getClient().getPlayer().getId()) {
            if (!getEntrance().getConfigStorage().get().renderConfig().showOtherPlayersBuild()) {
                return;
            }
        }
        getEntrance().getClientManager().getPatternRenderer().showPattern(uuid, context);

        if (context.buildInteractions().isEmpty()) {
            getEntrance().getClientManager().getOutlineRenderer().remove(generateId(uuid, BoundingBox3d.class));
        } else {
            var box = BoundingBox3d.fromLowerCornersOf(context.buildInteractions().results().stream().filter(Objects::nonNull).map(BlockInteraction::getBlockPosition).toArray(Vector3i[]::new));
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
                var locations = resultMap.get(allColor).stream().map(OperationResult::getOperation).map(Operation::locate).filter(Objects::nonNull).toList();
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
        if (player.getId() != getEntrance().getClient().getPlayer().getId()) {
            if (!getEntrance().getConfigStorage().get().renderConfig().showOtherPlayersBuildTooltips()) {
                return;
            }
        }
        var entries = new ArrayList<>();

        if (result != null) {
            var allProducts = new ArrayList<ItemStack>();
            for (var itemType : ItemSummaryType.values()) {
                var products = result.getProducts(itemType);
                if (!products.isEmpty()) {
                    var color = switch (itemType) {
                        case BLOCKS_PLACED -> ChatFormatting.WHITE;
                        case BLOCKS_DESTROYED -> ChatFormatting.RED;
                        case BLOCKS_INTERACTED -> ChatFormatting.YELLOW;
                        case BLOCKS_PLACE_INSUFFICIENT -> ChatFormatting.RED;
                        case BLOCKS_BREAK_INSUFFICIENT -> ChatFormatting.RED;
                        case BLOCKS_NOT_PLACEABLE -> ChatFormatting.GRAY;
                        case BLOCKS_NOT_BREAKABLE -> ChatFormatting.GRAY;
                        case BLOCKS_NOT_INTERACTABLE -> ChatFormatting.GRAY;
                        case BLOCKS_PLACE_NOT_WHITELISTED -> ChatFormatting.GRAY;
                        case BLOCKS_BREAK_NOT_WHITELISTED -> ChatFormatting.GRAY;
                        case BLOCKS_PLACE_BLACKLISTED -> ChatFormatting.GRAY;
                        case BLOCKS_BREAK_BLACKLISTED -> ChatFormatting.GRAY;
                    };
                    products = products.stream().map(stack -> ItemStackUtils.putColorTag(stack, color.getColor())).toList();
                    entries.add(products);
                    entries.add(Text.translate("effortless.build.summary." + itemType.name().toLowerCase(Locale.ROOT)).withStyle(color));
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

        for (var supportedFeature : context.buildMode().getSupportedFeatures()) {
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
                    message = Text.translate("effortless.message.building.cannot_reach_target").withStyle(ChatFormatting.WHITE).getString();
                    var interaction = context.buildInteractions().results().stream().filter(result -> result != null && result.getTarget() == Interaction.Target.MISS).findAny();
                    if (interaction.isPresent()) {
                        message += " (" + interaction.get().getBlockPosition().distance(player.getPosition().toVector3i()) + "/" + context.customParams().generalConfig().maxReachDistance() + ")";
                    }
                }
            }
        } else {
            message = getStateComponent(context.buildState()) + " "
                    + context.buildMode().getDisplayName().withStyle(ChatFormatting.GOLD).getString() + " "
                    + "("
                    + (context.getInteractionBox().x() > context.getMaxBoxSideLength() ? ChatFormatting.RED : ChatFormatting.WHITE) + context.getInteractionBox().x() + ChatFormatting.RESET
                    + "x"
                    + (context.getInteractionBox().y() > context.getMaxBoxSideLength() ? ChatFormatting.RED : ChatFormatting.WHITE) + context.getInteractionBox().y() + ChatFormatting.RESET
                    + "x"
                    + (context.getInteractionBox().z() > context.getMaxBoxSideLength() ? ChatFormatting.RED : ChatFormatting.WHITE) + context.getInteractionBox().z() + ChatFormatting.RESET
                    + "="
                    + (!context.isBoxVolumeInBounds() ? ChatFormatting.RED : ChatFormatting.WHITE) + context.getBoxVolume() + ChatFormatting.RESET
                    + ")";
        }

        player.sendClientMessage(message, true);


    }

}
