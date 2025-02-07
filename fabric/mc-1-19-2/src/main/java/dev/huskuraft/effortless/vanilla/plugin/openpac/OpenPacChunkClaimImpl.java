package dev.huskuraft.effortless.vanilla.plugin.openpac;

import java.util.UUID;

import javax.annotation.Nonnull;

import dev.huskuraft.effortless.api.plugin.openpac.OpenPacChunkClaim;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

public record OpenPacChunkClaimImpl(IPlayerChunkClaimAPI refs) implements OpenPacChunkClaim {

    public static OpenPacChunkClaim ofNullable(IPlayerChunkClaimAPI refs) {
        if (refs == null) return null;
        return new OpenPacChunkClaimImpl(refs);
    }

    @Override
    public @Nonnull UUID getPlayerId() {
        return refs.getPlayerId();
    }

}
