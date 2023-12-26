package dev.huskuraft.effortless;

import dev.huskuraft.effortless.building.*;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.packets.player.PlayerBuildPreviewPacket;

import javax.annotation.Nullable;
import java.util.function.UnaryOperator;

final class EffortlessServerStructureBuilder extends StructureBuilder {

    private final Entrance entrance;

    public EffortlessServerStructureBuilder(Entrance entrance) {
        this.entrance = entrance;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    @Override
    public BuildResult perform(Player player, BuildState state) {
        return null;
    }

    @Override
    public BuildResult perform(Player player, BuildState state, @Nullable BlockInteraction interaction) {
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
    public void onPlayerBreak(Player player) {

    }

    @Override
    public void onPlayerPlace(Player player) {

    }

    @Override
    public void onContextReceived(Player player, Context context) {

        if (context.isPreview()) {
            // FIXME: 13/10/23 add event for server manager
            for (var serverPlayer : player.getServer().getPlayers()) {
                if (serverPlayer.getId() == player.getId()) {
                    continue;
                }
                getEntrance().getChannel().sendPacket(new PlayerBuildPreviewPacket(player.getId(), context), player);
            }
        } else {
            // FIXME: 13/10/23 getCommandSenderWorld
            context.createSession(player.getWorld(), player).build().commit();
        }
    }

}
