package dev.huskuraft.effortless;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.building.BuildResult;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.StructureBuilder;
import dev.huskuraft.effortless.building.history.HistoryResult;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;
import dev.huskuraft.effortless.building.structure.builder.BuildStructure;
import dev.huskuraft.effortless.networking.packets.player.PlayerBuildPreviewPacket;
import dev.huskuraft.effortless.networking.packets.player.PlayerHistoryResultPacket;

public final class EffortlessStructureBuilder extends StructureBuilder {

    private final Effortless entrance;

    private final Map<UUID, Context> contexts = new HashMap<>();
    private final Map<UUID, OperationResultStack> undoRedoStacks = new HashMap<>();

    public EffortlessStructureBuilder(Effortless entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getPlayerChangeWorldEvent().register(this::onPlayerChangeWorld);
        getEntrance().getEventRegistry().getPlayerRespawnEvent().register(this::onPlayerRespawn);
        getEntrance().getEventRegistry().getPlayerLoggedInEvent().register(this::onPlayerLoggedIn);
        getEntrance().getEventRegistry().getPlayerLoggedOutEvent().register(this::onPlayerLoggedOut);

        getEntrance().getEventRegistry().getServerStartedEvent().register(this::onServerStarted);
        getEntrance().getEventRegistry().getServerStoppedEvent().register(this::onServerStopped);
    }

    public Effortless getEntrance() {
        return entrance;
    }


    @Override
    public BuildResult updateContext(Player player, UnaryOperator<Context> updater) {
        return null;
    }

    @Override
    public Context getDefaultContext(Player player) {
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

    @Override
    public Map<UUID, Context> getAllContexts() {
        return Map.of();
    }

    public void onTick() {
    }

    @Override
    public boolean setContext(Player player, Context context) {
        return false;
    }

    @Override
    public boolean setBuildStructure(Player player, BuildStructure buildStructure) {
        return false;
    }

    @Override
    public boolean setReplaceMode(Player player, ReplaceMode replaceMode) {
        return false;
    }

    @Override
    public boolean setPattern(Player player, Pattern pattern) {
        return false;
    }

    @Override
    public void resetAll() {
        contexts.clear();
        undoRedoStacks.clear();
    }

    @Override
    public void onContextReceived(Player player, Context context) {

        if (context.isPreview()) {
            var server = player.getServer();
            for (var otherPlayer : server.getPlayerList().getPlayers()) {
                if (otherPlayer.getId().equals(player.getId()) || otherPlayer.getPosition().distance(player.getPosition()) > 128) {
                    continue;
                }
                getEntrance().getChannel().sendPacket(new PlayerBuildPreviewPacket(player.getId(), context), otherPlayer);
            }
        } else {
            getOperationResultStack(player).push(context.createSession(player.getWorld(), player).build().commit());
//            getEntrance().getChannel().sendPacket(new PlayerBuildPreviewPacket(player.getId(), context.withBuildType(BuildType.PREVIEW_SOUND)), player);
        }
    }

    @Override
    public OperationResultStack getOperationResultStack(Player player) {
        return undoRedoStacks.computeIfAbsent(player.getId(), uuid -> new OperationResultStack());
    }

    @Override
    public void undo(Player player) {
        try {
            getEntrance().getChannel().sendPacket(new PlayerHistoryResultPacket(new HistoryResult(HistoryResult.Type.SUCCESS_UNDO, getOperationResultStack(player).undo())), player);
        } catch (EmptyStackException e) {
            getEntrance().getChannel().sendPacket(new PlayerHistoryResultPacket(new HistoryResult(HistoryResult.Type.NOTHING_TO_UNDO)), player);
        }
    }

    @Override
    public void redo(Player player) {
        try {
            getEntrance().getChannel().sendPacket(new PlayerHistoryResultPacket(new HistoryResult(HistoryResult.Type.SUCCESS_REDO, getOperationResultStack(player).redo())), player);
        } catch (EmptyStackException e) {
            getEntrance().getChannel().sendPacket(new PlayerHistoryResultPacket(new HistoryResult(HistoryResult.Type.NOTHING_TO_REDO)), player);
        }
    }

    private void onPlayerChangeWorld(Player player, World origin, World destination) {
    }

    private void onPlayerRespawn(Player oldPlayer, Player newPlayer, boolean alive) {
    }

    private void onPlayerLoggedIn(Player player) {
    }

    private void onPlayerLoggedOut(Player player) {
    }

    private void onServerStarted(Server server) {
        resetAll();
    }

    private void onServerStopped(Server server) {
        resetAll();
    }

}
