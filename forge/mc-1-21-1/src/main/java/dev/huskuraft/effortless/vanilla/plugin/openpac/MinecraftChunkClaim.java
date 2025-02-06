package dev.huskuraft.effortless.vanilla.plugin.openpac;

import java.util.UUID;

import javax.annotation.Nonnull;

import dev.huskuraft.effortless.api.plugin.openpac.ChunkClaim;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

public record MinecraftChunkClaim(IPlayerChunkClaimAPI refs) implements ChunkClaim {

    public static ChunkClaim ofNullable(IPlayerChunkClaimAPI refs) {
        if (refs == null) return null;
        return new MinecraftChunkClaim(refs);
    }

    @Override
    public @Nonnull UUID getPlayerId() {
        return refs.getPlayerId();
    }

}
