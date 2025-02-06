package dev.huskuraft.effortless.vanilla.plugin.openpac;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.plugin.openpac.ChunkClaimsManager;
import dev.huskuraft.effortless.api.plugin.openpac.OpenPacPlugin;
import xaero.pac.common.server.api.OpenPACServerAPI;

@AutoService(OpenPacPlugin.class)
public class MinecraftOpenPacPlugin implements OpenPacPlugin {

    public static final String ID = "openpartiesandclaims";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void init() {
    }

    @Override
    public ChunkClaimsManager getServerClaimManager(@Nonnull Server server) {
        return MinecraftChunkClaimsManager.ofNullable(OpenPACServerAPI.get(server.reference()).getServerClaimsManager());
    }

}
