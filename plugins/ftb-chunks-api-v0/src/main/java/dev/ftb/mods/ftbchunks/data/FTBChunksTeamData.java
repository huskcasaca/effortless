package dev.ftb.mods.ftbchunks.data;

import java.util.Collection;
import java.util.UUID;

import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.data.Team;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class FTBChunksTeamData {

    public ClaimedChunkManager getManager() {
        throw new RuntimeException("stub!");
    }

    public Team getTeam() {
        throw new RuntimeException("stub!");
    }

    public UUID getTeamId() {
        throw new RuntimeException("stub!");
    }

    public Collection<ClaimedChunk> getClaimedChunks() {
        throw new RuntimeException("stub!");
    }

    public Collection<ClaimedChunk> getForceLoadedChunks() {
        throw new RuntimeException("stub!");
    }

    public Collection<ClaimedChunk> getOriginalClaims(UUID playerID) {
        throw new RuntimeException("stub!");
    }

    public ClaimResult claim(CommandSourceStack source, ChunkDimPos pos, boolean checkOnly) {
        throw new RuntimeException("stub!");
    }

    public ClaimResult unclaim(CommandSourceStack source, ChunkDimPos pos, boolean checkOnly) {
        throw new RuntimeException("stub!");
    }

    public ClaimResult load(CommandSourceStack source, ChunkDimPos pos, boolean checkOnly) {
        throw new RuntimeException("stub!");
    }

    public ClaimResult unload(CommandSourceStack source, ChunkDimPos pos, boolean checkOnly) {
        throw new RuntimeException("stub!");
    }

    public void save() {
        throw new RuntimeException("stub!");
    }

    public boolean isTeamMember(UUID p) {
        throw new RuntimeException("stub!");
    }

    public boolean isAlly(UUID p) {
        throw new RuntimeException("stub!");
    }

    public int getExtraClaimChunks() {
        throw new RuntimeException("stub!");
    }

    public int getExtraForceLoadChunks() {
        throw new RuntimeException("stub!");
    }

    public void setForceLoadMember(UUID id, boolean val) {
        throw new RuntimeException("stub!");
    }


    public void updateChunkTickets(boolean load) {
        throw new RuntimeException("stub!");
    }

    public boolean canForceLoadChunks() {
        throw new RuntimeException("stub!");
    }

    public boolean hasForceLoadMembers() {
        throw new RuntimeException("stub!");
    }

    public boolean allowExplosions() {
        throw new RuntimeException("stub!");
    }

    public boolean allowMobGriefing() {
        throw new RuntimeException("stub!");
    }

    public void setLastLoginTime(long when) {
        throw new RuntimeException("stub!");
    }

    public long getLastLoginTime() {
        throw new RuntimeException("stub!");
    }

    public int getMaxClaimChunks() {
        throw new RuntimeException("stub!");
    }

    public int getMaxForceLoadChunks() {
        throw new RuntimeException("stub!");
    }

    public void updateLimits() {
        throw new RuntimeException("stub!");
    }

    private void updateMemberLimitData(boolean onlinePlayersOnly) {
        throw new RuntimeException("stub!");
    }

    public void addMemberData(ServerPlayer player, FTBChunksTeamData otherTeam) {
        throw new RuntimeException("stub!");
    }

    public void deleteMemberData(UUID playerId) {
        throw new RuntimeException("stub!");
    }
}
