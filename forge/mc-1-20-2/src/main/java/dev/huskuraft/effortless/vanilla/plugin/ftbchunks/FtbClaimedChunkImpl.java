package dev.huskuraft.effortless.vanilla.plugin.ftbchunks;

import java.util.UUID;

import dev.huskuraft.effortless.api.plugin.ftbchunks.FtbClaimedChunk;

public record FtbClaimedChunkImpl(dev.ftb.mods.ftbchunks.api.ClaimedChunk refs) implements FtbClaimedChunk {

    public static FtbClaimedChunk ofNullable(dev.ftb.mods.ftbchunks.api.ClaimedChunk refs) {
        if (refs == null) return null;
        return new FtbClaimedChunkImpl(refs);
    }

    @Override
    public boolean isTeamMember(UUID uuid) {
        return refs.getTeamData().isTeamMember(uuid);
    }
}
