package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.*;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.operation.ItemType;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.batch.BatchOperationResult;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.SingleAction;
import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.packets.player.PlayerActionPacket;
import dev.huskuraft.effortless.packets.player.PlayerBuildPacket;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.renderer.LightTexture;
import dev.huskuraft.effortless.renderer.opertaion.SurfaceColor;
import dev.huskuraft.effortless.renderer.texture.RenderTextures;
import dev.huskuraft.effortless.screen.radial.AbstractRadialScreen;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

final class EffortlessClientStructureBuilder extends StructureBuilder {

    private final Map<UUID, Context> contexts = new HashMap<>();
    private final ClientEntrance entrance;

    public EffortlessClientStructureBuilder(Entrance entrance) {
        this.entrance = (ClientEntrance) entrance;

        getEntrance().getEventRegistry().onClientTick().register(this::onClientTick);
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

    private ClientEntrance getEntrance() {
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
                return context.resetBuildState();
            }
            if (interaction.getTarget() != Interaction.Target.BLOCK) {
                return context.resetBuildState();
            }
            if (context.isBuilding() && context.state() != state) {
                return context.resetBuildState();
            }
            return context.withState(state).withNextInteraction(interaction);
        });
    }

    @Override
    public BuildResult updateContext(Player player, UnaryOperator<Context> updater) {
        var context = updater.apply(getContext(player));
        if (context.isFulfilled()) {

            var finalized = context.finalize(player, BuildStage.INTERACT);
            var result = finalized.withPreviewOnceType().createSession(player.getWorld(), player).build().commit();

            showOperationResult(context.uuid(), result);
            showOperationResultTooltip(context.uuid(), result, 1000);

            getEntrance().getChannel().sendPacket(new PlayerBuildPacket(finalized));
            setContext(player, context.resetBuildState());

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
        return Context.defaultSet();
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
    public BuildResult onPlayerBreak(Player player) {
        var context = getContext(player);
        var interaction = context.withBreakingState().trace(player);
        var result = build(player, BuildState.BREAK_BLOCK, interaction);

        if (result.isSuccess()) {
            // play sound if further than normal
            // TODO: 22/7/23
            // FIXME: 13/10/23
//            if (interaction.getLocation().subtract(player.getEyePosition(1f)).lengthSqr() > 25f) {
//                var blockPos = interaction.getBlockPos();
//                var state = player.getWorld().getBlockState(blockPosition);
//                var soundtype = state.getBlock().getSoundType(state);
//                player.getWorld().playSound(player, player.blockPosition(), soundtype.getBreakSound(), SoundSource.BLOCKS, 0.4f, soundtype.getPitch());
//            }
        }
        return result;
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
        var result = context.createSession(player.getWorld(), player).build().commit();

//        showPattern(player.getId(), context);
        showOperationResult(player.getId(), result);

        showOperationResultTooltip(context.uuid(), result, 1);
    }

    @Override
    public OperationResultStack getOperationResultStack(Player player) {
        return null;
    }

    @Override
    public void undo(Player player) {
        getEntrance().getChannel().sendPacket(new PlayerActionPacket(SingleAction.UNDO));
    }

    @Override
    public void redo(Player player) {
        getEntrance().getChannel().sendPacket(new PlayerActionPacket(SingleAction.REDO));
    }

    public void onClientTick(Client client, TickPhase phase) {
        if (phase == TickPhase.END) {
            return;
        }

        var player = getEntrance().getClient().getPlayer();
        if (player == null || getContext(player).isDisabled()) {
            return;
        }
        setContext(player, getContext(player).withRandomPatternSeed());
        var context = getContextTraced(player).withPreviewType();

        var result = context.withPreviewType().createSession(player.getWorld(), player).build().commit();

        showPattern(player.getId(), context);
        showOperationResult(player.getId(), result);

        showContextTooltip(player.getId(), context, 0);
        showOperationResultTooltip(player.getId(), result, 1);

        getEntrance().getChannel().sendPacket(new PlayerBuildPacket(context));
    }

    private UUID nextUUIDByTag(UUID uuid, String tag) {
        return new UUID(uuid.getMostSignificantBits() + tag.hashCode(), uuid.getLeastSignificantBits());
    }

    public void showPattern(UUID uuid, Context context) {
        getEntrance().getClientManager().getPatternRenderer().showPattern(uuid, context);
    }

    public void showOperationResult(UUID uuid, OperationResult result) {
        getEntrance().getClientManager().getOperationsRenderer().showResult(uuid, result);
        if (result instanceof BatchOperationResult result1) {
            var cluster = getEntrance().getClientManager().getOutlineRenderer().showCluster(result1.getOperation().getContext().uuid(), result1.locations())
                    .texture(RenderTextures.CHECKERED_THIN_TEXTURE_LOCATION)
                    .lightMap(LightTexture.FULL_BLOCK)
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

    public void showOperationResultTooltip(UUID uuid, OperationResult result, int priority) {
        getEntrance().getClientManager().getSubtitleManager().showTitledItems(nextUUIDByTag(uuid, "placed"), Text.translate("effortless.build.summary.placed_blocks").withStyle(TextStyle.WHITE), result.getProducts(ItemType.PLAYER_USED), priority);
        getEntrance().getClientManager().getSubtitleManager().showTitledItems(nextUUIDByTag(uuid, "destroyed"), Text.translate("effortless.build.summary.destroyed_blocks").withStyle(TextStyle.RED), result.getProducts(ItemType.WORLD_DROPPED), priority);
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

        texts.add(Text.translate("effortless.build.summary.state").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(getStateComponent(context.state()).withStyle(TextStyle.GOLD)));
        texts.add(Text.translate("effortless.build.summary.tracing").withStyle(TextStyle.WHITE).append(Text.text(" ")).append(getTracingComponent(context.tracingResult()).withStyle(TextStyle.GOLD)));

        getEntrance().getClientManager().getSubtitleManager().showMessages(nextUUIDByTag(uuid, "info"), texts, priority);
    }

}
