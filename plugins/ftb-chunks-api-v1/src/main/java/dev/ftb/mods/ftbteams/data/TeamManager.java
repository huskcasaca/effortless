package dev.ftb.mods.ftbteams.data;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;


import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

public class TeamManager {

    public UUID getId() {
        throw new RuntimeException("stub!");
    }

    public Map<UUID, Team> getTeamMap() {
        throw new RuntimeException("stub!");
    }

    public Collection<Team> getTeams() {
        throw new RuntimeException("stub!");
    }

    public Map<String, Team> getTeamNameMap() {
        throw new RuntimeException("stub!");
    }

    @Nullable
    public Team getTeamByID(UUID uuid) {
        throw new RuntimeException("stub!");
    }

    @Nullable
    public Team getPlayerTeam(UUID uuid) {
        throw new RuntimeException("stub!");
    }

    public Team getPlayerTeam(ServerPlayer player) {
        throw new RuntimeException("stub!");
    }

    public boolean arePlayersInSameTeam(ServerPlayer player1, ServerPlayer player2) {
        throw new RuntimeException("stub!");
    }

    public UUID getPlayerTeamID(UUID id) {
        throw new RuntimeException("stub!");
    }

    public Component getName(@Nullable UUID id) {
        throw new RuntimeException("stub!");
    }

}
