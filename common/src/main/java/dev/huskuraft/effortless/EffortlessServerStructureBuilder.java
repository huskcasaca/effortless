package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.building.*;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPreviewPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerCommandPacket;

import javax.annotation.Nullable;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class EffortlessServerStructureBuilder extends StructureBuilder {

    private final Effortless entrance;

    public EffortlessServerStructureBuilder(Effortless entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getPlayerCloneEvent().register(this::onPlayerClone);
        getEntrance().getEventRegistry().getPlayerChangeWorldEvent().register(this::onPlayerChangeWorld);
    }

    public Effortless getEntrance() {
        return entrance;
    }

    @Override
    public BuildResult build(Player player, BuildState state) {
        return null;
    }

    @Override
    public BuildResult build(Player player, BuildState state, @Nullable BlockInteraction interaction) {
        return null;
    }

    @Override
    public BuildResult updateContext(Player player, UnaryOperator<Context> updater) {
        return null;
    }

    @Override
    public Context getDefaultContext() {
        return null;
    }

    @Override
    public Context getContext(Player player) {
        return null;
    }

    @Override
    public Context getContextTraced(Player player) {
        return null;
    }


    public void onTick() {

    }

    @Override
    public void setContext(Player player, Context context) {

    }

    @Override
    public void setBuildMode(Player player, BuildMode buildMode) {

    }

    @Override
    public void setBuildFeature(Player player, SingleSelectFeature feature) {

    }

    @Override
    public void setBuildFeature(Player player, MultiSelectFeature feature) {

    }

    @Override
    public void setPattern(Player player, Pattern pattern) {

    }

    @Override
    public BuildResult onPlayerBreak(Player player) {
        return BuildResult.CANCELED;
    }

    @Override
    public BuildResult onPlayerPlace(Player player) {
        return BuildResult.CANCELED;
    }

    @Override
    public void onContextReceived(Player player, Context context) {

        if (context.isPreview()) {
            // FIXME: 13/10/23 add event for server manager
            for (var serverPlayer : player.getServer().getPlayers()) {
                if (serverPlayer.getId().equals(player.getId())) {
                    continue;
                }
                getEntrance().getChannel().sendPacket(new PlayerBuildPreviewPacket(player.getId(), context), serverPlayer);
            }
        } else {
            // FIXME: 13/10/23 getCommandSenderWorld
            getOperationResultStack(player).push(context.createSession(player.getWorld(), player).build().commit());
        }
    }

    private final Map<UUID, OperationResultStack> undoRedoStacks = new HashMap<>();

    @Override
    public OperationResultStack getOperationResultStack(Player player) {
        return undoRedoStacks.computeIfAbsent(player.getId(), uuid -> new OperationResultStack());
    }

    @Override
    public void undo(Player player) {
        try {
            getOperationResultStack(player).undo();
            getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.UNDO), player);
        } catch (EmptyStackException e) {
//            getEntrance().getChannel().sendPacket(new PlayerActionPacket(SingleAction.NOTHING_TO_UNDO), player);
        }
    }

    @Override
    public void redo(Player player) {
        try {
            getOperationResultStack(player).redo();
            getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.REDO), player);
        } catch (EmptyStackException e) {
//            getEntrance().getChannel().sendPacket(new PlayerActionPacket(SingleAction.NOTHING_TO_REDO), player);
        }
    }

    private void onPlayerClone(Player from, Player to, boolean death) {
        getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.RESET_BUILD_STATE), to);
    }

    private void onPlayerChangeWorld(Player player, World origin, World destination) {
        getEntrance().getChannel().sendPacket(new PlayerCommandPacket(SingleCommand.RESET_BUILD_STATE), player);
    }

}
