package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.Player;

import javax.annotation.Nullable;
import java.util.function.UnaryOperator;

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

    public abstract BuildResult onPlayerBreak(Player player);

    public abstract BuildResult onPlayerPlace(Player player);

    public abstract void onContextReceived(Player player, Context context);

}
