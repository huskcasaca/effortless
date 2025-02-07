package dev.ftb.mods.ftbchunks.data;

import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;

public class ClaimedChunk implements ClaimResult {

    public ClaimedChunk(FTBChunksTeamData p, ChunkDimPos cp) {
    }

    public FTBChunksTeamData getTeamData() {
        throw new RuntimeException("stub!");
    }

    public ChunkDimPos getPos() {
        throw new RuntimeException("stub!");
    }

    public long getTimeClaimed() {
        throw new RuntimeException("stub!");
    }

    @Override
    public String claimResultName() {
        throw new RuntimeException("stub!");
    }

    @Override
    public boolean isSuccess() {
        throw new RuntimeException("stub!");
    }

    @Override
    public void setClaimedTime(long t) {
        throw new RuntimeException("stub!");
    }

    public long getForceLoadedTime() {
        throw new RuntimeException("stub!");
    }

    public boolean isForceLoaded() {
        throw new RuntimeException("stub!");
    }

    public boolean isActuallyForceLoaded() {
        throw new RuntimeException("stub!");
    }

    @Override
    public void setForceLoadedTime(long time) {
    }

    @Override
    public String getTranslationKey() {
        throw new RuntimeException("stub!");
    }

    public boolean canEntitySpawn(Entity entity) {
        throw new RuntimeException("stub!");
    }

    public boolean allowExplosions() {
        throw new RuntimeException("stub!");
    }

    public boolean allowMobGriefing() {
        throw new RuntimeException("stub!");
    }


    public void unload(CommandSourceStack source) {
        throw new RuntimeException("stub!");
    }

    public void unclaim(CommandSourceStack source, boolean sync) {
        throw new RuntimeException("stub!");
    }

    public long getForceLoadExpiryTime() {
        throw new RuntimeException("stub!");
    }

    public void setForceLoadExpiryTime(long forceLoadExpiryTime) {
        throw new RuntimeException("stub!");
    }

    public boolean hasExpired(long now) {
        throw new RuntimeException("stub!");
    }

}
