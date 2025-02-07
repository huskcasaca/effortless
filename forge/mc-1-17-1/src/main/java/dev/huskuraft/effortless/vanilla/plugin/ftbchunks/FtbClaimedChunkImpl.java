package dev.huskuraft.effortless.vanilla.plugin.ftbchunks;

import java.util.UUID;

import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.huskuraft.effortless.api.plugin.ftbchunks.FtbClaimedChunk;

public record FtbClaimedChunkImpl(ClaimedChunk refs) implements FtbClaimedChunk {

    public static FtbClaimedChunk ofNullable(ClaimedChunk refs) {
        if (refs == null) return null;
        return new FtbClaimedChunkImpl(refs);
    }

    @Override
    public boolean isTeamMember(UUID uuid) {
        return refs.getTeamData().isTeamMember(uuid);
    }
}
