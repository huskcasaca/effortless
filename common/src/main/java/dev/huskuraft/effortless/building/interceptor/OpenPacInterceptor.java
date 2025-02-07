package dev.huskuraft.effortless.building.interceptor;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.plugin.openpac.ChunkClaimsManager;
import dev.huskuraft.effortless.api.plugin.openpac.OpenPacClientPlugin;
import dev.huskuraft.effortless.api.plugin.openpac.OpenPacPlugin;

public final class OpenPacInterceptor implements BuildInterceptor {

    private final ChunkClaimsManager chunkClaimsManager;

    public OpenPacInterceptor(
            ChunkClaimsManager chunkClaimsManager
    ) {
        this.chunkClaimsManager = chunkClaimsManager;
    }

    public OpenPacInterceptor(
            Entrance entrance
    ) {
        if (entrance instanceof ClientEntrance) {
            var plugin = entrance.findPlugin(OpenPacClientPlugin.class);
            this.chunkClaimsManager = plugin.map(OpenPacClientPlugin::getClaimManager).orElse(null);
        } else {
            var plugin = entrance.findPlugin(OpenPacPlugin.class);
            this.chunkClaimsManager = plugin.map(openPacPlugin -> openPacPlugin.getServerClaimManager(entrance.getServer())).orElse(null);
        }
    }

    public ChunkClaimsManager getChunkClaimsManager() {
        return chunkClaimsManager;
    }

    @Override
    public boolean isEnabled() {
        return chunkClaimsManager != null;
    }

    @Override
    public boolean allowInteraction(Player player, World world, BlockPosition blockPosition) {
        if (!isEnabled()) {
            return true;
        }
        var claim = getChunkClaimsManager().get(world.getDimensionId().location(), blockPosition);
        return claim == null || claim.getPlayerId().equals(player.getId());
    }
}
