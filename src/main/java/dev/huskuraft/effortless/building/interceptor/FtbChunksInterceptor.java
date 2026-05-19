package dev.huskuraft.effortless.building.interceptor;

import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.core.World;
import dev.huskuraft.universal.api.platform.ClientEntrance;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.plugin.ftbchunks.FtbChunkClaimsManager;
import dev.huskuraft.universal.api.plugin.ftbchunks.FtbChunksPlugin;

public final class FtbChunksInterceptor implements BuildInterceptor {

    private final FtbChunkClaimsManager ftbChunkClaimsManager;

    public FtbChunksInterceptor(
            FtbChunkClaimsManager ftbChunkClaimsManager
    ) {
        this.ftbChunkClaimsManager = ftbChunkClaimsManager;
    }

    public FtbChunksInterceptor(
            Entrance entrance
    ) {
        if (entrance instanceof ClientEntrance) {
            // FTB Chunks' claim manager is server-only; FTBChunksAPI.getManager() is null on a
            // dedicated client and throws inside the plugin impl. Server-side BatchBuildSession
            // enforces claims at commit time.
            this.ftbChunkClaimsManager = null;
        } else {
            this.ftbChunkClaimsManager = entrance.findPlugin(FtbChunksPlugin.class).map(FtbChunksPlugin::getClaimManager).orElse(null);
        }
    }

    public FtbChunkClaimsManager getChunkClaimsManager() {
        return ftbChunkClaimsManager;
    }

    @Override
    public boolean isEnabled() {
        return ftbChunkClaimsManager != null;
    }

    @Override
    public boolean allowInteraction(Player player, World world, BlockPosition blockPosition) {
        if (!isEnabled()) {
            return true;
        }
        var claim = getChunkClaimsManager().get(world, blockPosition);
        return claim == null || claim.isTeamMember(player.getId());
    }
}
