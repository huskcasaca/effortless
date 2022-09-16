package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.*;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.events.api.EventResult;
import dev.huskuraft.effortless.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.screen.radial.AbstractRadialScreen;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.renderer.RenderStyleProvider;
import dev.huskuraft.effortless.renderer.opertaion.SurfaceColor;
import dev.huskuraft.effortless.renderer.outliner.Outline;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

final class ActualClientStructureBuilder extends StructureBuilder {

    private final Map<UUID, Context> contexts = new HashMap<>();
    private final ClientEntrance entrance;

    public ActualClientStructureBuilder(Entrance entrance) {
        this.entrance = (ClientEntrance) entrance;

        getEntrance().getEventRegistry().onClientTick().register(this::onClientTick);
        getEntrance().getEventRegistry().onClientPlayerInteract().register(this::onPlayerInteract);

    }

    private static Text getStateComponent(BuildState state) {
        return Text.translate(Text.asKey("state", switch (state) {
                    case IDLE -> "idle";
                    case PLACE_BLOCK -> "place_block";
                    case BREAK_BLOCK -> "break_block";
                })
        );
    }

    private static Text getTracingComponent(TracingResult result) {
        return Text.translate(Text.asKey("tracing", switch (result) {
                    case SUCCESS_FULFILLED -> "success_fulfilled";
                    case SUCCESS_PARTIAL -> "success_partial";
                    case PASS -> "pass";
                    case FAILED -> "failed";
                })
        );
    }

    private ClientEntrance getEntrance() {
        return entrance;
    }

    @Override
    public BuildingResult perform(Player player, BuildState state) {
        return perform(player, state, null);
    }

    @Override
    public BuildingResult perform(Player player, BuildState state, @Nullable BlockInteraction interaction) {
        return update(player, context -> {
            if (interaction == null) {
                return context.reset();
            }
            if (interaction.getTarget() != Interaction.Target.BLOCK) {
                return context.reset();
            }
            if (context.isBuilding() && context.state() != state) {
                return context.reset();
            }
            return context.withState(state).withNextInteraction(interaction);
        });
    }

    @Override
    public Context getDefaultContext() {
        return Context.defaultSet();
    }

    @Override
    public Context getContext(Player player) {
        return contexts.computeIfAbsent(player.getId(), uuid -> getDefaultContext());
    }

    @Override
    public Context getContextTraced(Player player) {
        var context = getContext(player);
        if (context.noClicks()) {
            if (player.getItemStack(InteractionHand.MAIN).isEmpty()) {
                context = context.withBreakingState();
            } else {
                context = context.withPlacingState();
            }
        }
        return context.withNextHitTraceBy(player).withUUID(player.getId());
    }

    @Override
    public void setContext(Player player, Context context) {
        contexts.put(player.getId(), context);
    }

    // from settings screen
    @Override
    public void setBuildMode(Player player, BuildMode buildMode) {
        update(player, context -> context.withEmptyInteractions().withBuildMode(buildMode));
    }

    @Override
    public void setBuildFeature(Player player, SingleSelectFeature feature) {
        update(player, context -> context.withBuildFeature(feature));
    }

    @Override
    public void setBuildFeature(Player player, MultiSelectFeature feature) {
        update(player, context -> {
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
        update(player, context -> {
            return context.withPattern(pattern);
        });
    }

    @Override
    public void onPlayerBreak(Player player) {
        var context = getContext(player);
        var result = context.withBreakingState().trace(player);
        var perform = perform(player, BuildState.BREAK_BLOCK, result);

        if (perform.isSuccess()) {
            // play sound if further than normal
            // TODO: 22/7/23
            // FIXME: 13/10/23
//            if (result.getLocation().subtract(player.getEyePosition(1f)).lengthSqr() > 25f) {
//                var blockPos = result.getBlockPos();
//                var state = player.getWorld().getBlockData(blockPosition);
//                var soundtype = state.getBlock().getSoundType(state);
//                player.getWorld().playSound(player, player.blockPosition(), soundtype.getBreakSound(), SoundSource.BLOCKS, 0.4f, soundtype.getPitch());
//            }
        }
    }

    @Override
    public void onPlayerPlace(Player player) {
        var context = getContext(player);
        setRightClickDelay(4); // for single build speed

        if (player.getItemStack(InteractionHand.MAIN).isEmpty()) {
            return;
        }

        var result = context.withPlacingState().trace(player);
        var perform = perform(player, BuildState.PLACE_BLOCK, result);

        if (perform.isSuccess()) {
//            player.swing(PlayerHand.MAIN);
        }
    }

    @Override
    public BuildingResult update(Player player, UnaryOperator<Context> updater) {
        var context = getContext(player);
        var updated = updater.apply(context);
        if (updated.isFulfilled()) {

            showLocalPlayerPreviewOnce(player, updated);

            getEntrance().getChannel().sendPacket(new PlayerBuildPacket(updated));
            setContext(player, updated.reset());

            return BuildingResult.COMPLETED;
        } else {
            setContext(player, updated);
            if (updated.isIdle()) {
                return BuildingResult.CANCELED;
            } else {
                return BuildingResult.PARTIAL;
            }
        }
    }

    private void setRightClickDelay(int delay) {
//        getEntrance().getClient().setRightClickDelay(delay); // for single build speed
    }

    public void onClientTick(Client client, TickPhase phase) {
        switch (phase) {

            case START -> {
                var player = getEntrance().getClient().getPlayer();
                if (player != null && !getContext(player).isDisabled()) {
                    var context = getContextTraced(player).withPreviewType();
                    showLocalPlayerPreview(player, context);
                    getEntrance().getChannel().sendPacket(new PlayerBuildPacket(context));
                }
            }
            case END -> {
            }
        }
    }

    @Override
    public void onContextReceived(Player player, Context context) {
        showRemotePlayerPreview(player, context);
    }

    private EventResult onPlayerInteract(Player player, InteractionType type, InteractionHand hand) {
        if (getContext(player).isDisabled()) {
            return EventResult.pass();
        }

        var result = getEntrance().getClient().getLastInteraction();
        if (result != null && result.getTarget() == Interaction.Target.ENTITY) {
            return EventResult.pass();
        }

        switch (type) {
            case HIT -> onPlayerBreak(player);
            case USE -> onPlayerPlace(player);
            case UNKNOWN -> {
                return EventResult.pass();
            }
        }
        return EventResult.interruptTrue();
    }

    public void showOperationResult(UUID uuid, OperationResult result) {
        getEntrance().getClientManager().getOperationsRenderer().showResult(uuid, result);
        if (result instanceof BatchOperationResult result1) {
            var cluster = getEntrance().getClientManager().getOutlineRenderer().showCluster(result1.getOperation().getContext().uuid(), result1.locations())
                    .texture(RenderStyleProvider.CHECKERED_THIN_TEXTURE_LOCATION)
                    .lightMap(Outline.LightTexture.FULL_BLOCK)
                    .disableNormals()
                    .stroke(1 / 64f);
            switch (result.getOperation().getContext().state()) {
                case IDLE -> {
                }
                case PLACE_BLOCK -> cluster.colored(SurfaceColor.COLOR_WHITE);
                case BREAK_BLOCK -> cluster.colored(SurfaceColor.COLOR_RED);
            }
        }
    }

    public void showSummaryOverlay(UUID uuid, OperationResult result, int priority) {
        getEntrance().getClientManager().getSubtitleManager().showTitledItems("placed" + uuid, Text.text("Placed Blocks").withStyle(TextStyle.WHITE), result.get(ItemType.PLAYER_USED), priority);
        getEntrance().getClientManager().getSubtitleManager().showTitledItems("destroyed" + uuid, Text.text("Destroyed Blocks").withStyle(TextStyle.RED), result.get(ItemType.WORLD_DROPPED), priority);
    }

    public void showContextOverlay(UUID uuid, Context context, int priority) {
        var texts = new ArrayList<Text>();
        texts.add(Text.text("Structure").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(context.buildMode().getNameComponent().withStyle(TextStyle.GOLD)));
        var replace = AbstractRadialScreen.option(context.replaceMode());
        texts.add(replace.getCategoryComponent().withStyle(TextStyle.WHITE).append(Text.text(" ")).append(replace.getNameComponent().withStyle(TextStyle.GOLD)));

        for (var supportedFeature : context.buildMode().getSupportedFeatures()) {
            var option = context.buildFeatures().stream().filter(feature -> Objects.equals(feature.getCategory(), supportedFeature.getName())).findFirst();
            if (option.isEmpty()) continue;
            var button = AbstractRadialScreen.option(option.get());
            texts.add(button.getCategoryComponent().withStyle(TextStyle.WHITE).append(Text.text(" ")).append(button.getNameComponent().withStyle(TextStyle.GOLD)));
        }

        texts.add(Text.text("State").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(getStateComponent(context.state()).withStyle(TextStyle.GOLD)));
        texts.add(Text.text("Tracing").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(getTracingComponent(context.tracingResult()).withStyle(TextStyle.GOLD)));

        getEntrance().getClientManager().getSubtitleManager().showMessages("info" + uuid, texts, priority);
    }

    public void showLocalPlayerPreview(Player player, Context context) {
        var result = context.withPreviewType().createSession(player.getWorld(), player).build().commit();

        showOperationResult(context.uuid(), result);
        showContextOverlay(context.uuid(), context, 0);
        showSummaryOverlay(context.uuid(), result, 1);
    }

    public void showLocalPlayerPreviewOnce(Player player, Context context) {
        var result = context.withPreviewOnceType().createSession(player.getWorld(), player).build().commit();

        showOperationResult(context.uuid(), result);
        showSummaryOverlay(context.uuid(), result, 1000);
    }

    public void showRemotePlayerPreview(Player player, Context context) {
        var result = context.createSession(player.getWorld(), player).build().commit();

        showSummaryOverlay(context.uuid(), result, 1);
    }


}
