package dev.ftb.mods.ftbchunks.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;


import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.data.Team;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClaimedChunkManager {


    public FTBChunksTeamData getData(@Nullable Team team) {
        throw new RuntimeException("stub!");
    }

    public FTBChunksTeamData getPersonalData(UUID id) {
        throw new RuntimeException("stub!");
    }

    public FTBChunksTeamData getPersonalData(ServerPlayer player) {
        throw new RuntimeException("stub!");
    }

    public FTBChunksTeamData getData(ServerPlayer player) {
        throw new RuntimeException("stub!");
    }

    public boolean hasData(ServerPlayer player) {
        throw new RuntimeException("stub!");
    }

    public void deleteTeam(Team toDelete) {
        throw new RuntimeException("stub!");
    }

    @Nullable
    public ClaimedChunk getChunk(ChunkDimPos pos) {
        throw new RuntimeException("stub!");
    }

    public Collection<ClaimedChunk> getAllClaimedChunks() {
        throw new RuntimeException("stub!");
    }

    public Map<UUID, List<ClaimedChunk>> getClaimedChunksByTeam(Predicate<ClaimedChunk> predicate) {
        throw new RuntimeException("stub!");
    }

    public boolean getBypassProtection(UUID player) {
        throw new RuntimeException("stub!");
    }

    public void setBypassProtection(UUID player, boolean bypass) {
        throw new RuntimeException("stub!");
    }

    public boolean protect(@Nullable Entity entity, InteractionHand hand, BlockPos pos, Protection protection, @Nullable Entity targetEntity) {
        throw new RuntimeException("stub!");
    }

    public void clearForceLoadedCache() {
        throw new RuntimeException("stub!");
    }

    public Map<ResourceKey<Level>, Long2ObjectMap<UUID>> getForceLoadedChunks() {
        throw new RuntimeException("stub!");
    }

    @Nonnull
    public Long2ObjectMap<UUID> getForceLoadedChunks(ResourceKey<Level> dimension) {
        throw new RuntimeException("stub!");
    }

    public boolean isChunkForceLoaded(ResourceKey<Level> dimension, int x, int z) {
        throw new RuntimeException("stub!");
    }

    public void registerClaim(ChunkDimPos pos, ClaimedChunk chunk) {
        throw new RuntimeException("stub!");
    }

    public void unregisterClaim(ChunkDimPos pos) {
        throw new RuntimeException("stub!");
    }
}
