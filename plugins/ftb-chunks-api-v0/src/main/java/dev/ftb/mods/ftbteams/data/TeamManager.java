package dev.ftb.mods.ftbteams.data;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public abstract class TeamManager {

    public abstract UUID getId();

    public abstract Map<UUID, Team> getTeamMap();

    public abstract Collection<Team> getTeams();

    public abstract Map<String, Team> getTeamNameMap();

    @Nullable
    public abstract Team getTeamByID(UUID uuid);

    @Nullable
    public abstract Team getPlayerTeam(UUID uuid);

    public abstract Team getPlayerTeam(ServerPlayer player);

    public abstract boolean arePlayersInSameTeam(ServerPlayer player1, ServerPlayer player2);

    public abstract UUID getPlayerTeamID(UUID id);

    public abstract Component getName(@Nullable UUID id);

}
