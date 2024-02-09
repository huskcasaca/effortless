package dev.huskuraft.effortless.building;

import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.history.OperationResultStack;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;

public abstract class StructureBuilder {

    public abstract BuildResult build(Player player, BuildState state);

    public abstract BuildResult build(Player player, BuildState state, @Nullable BlockInteraction interaction);

    public abstract BuildResult updateContext(Player player, UnaryOperator<Context> updater);

    public abstract Context getDefaultContext();

    public abstract Context getContext(Player player);

    public abstract Context getContextTraced(Player player);

    public abstract void setContext(Player player, Context context);

    public abstract void setBuildMode(Player player, BuildMode buildMode);

    public abstract void setBuildFeature(Player player, SingleSelectFeature feature);

    public abstract void setBuildFeature(Player player, MultiSelectFeature feature);

    public abstract void setPattern(Player player, Pattern pattern);

    public abstract void reset();

    public void resetContext(Player player) {
        setContext(player, getDefaultContext());
    }

    public void resetBuildState(Player player) {
        setContext(player, getContext(player).resetBuildState());
    }

    public abstract BuildResult onPlayerBreak(Player player);

    public abstract BuildResult onPlayerPlace(Player player);

    public abstract void onContextReceived(Player player, Context context);

    public abstract OperationResultStack getOperationResultStack(Player player);

    public abstract void undo(Player player);

    public abstract void redo(Player player);

}
