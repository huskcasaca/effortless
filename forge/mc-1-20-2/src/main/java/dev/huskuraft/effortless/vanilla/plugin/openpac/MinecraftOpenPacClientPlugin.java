package dev.huskuraft.effortless.vanilla.plugin.openpac;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.plugin.openpac.ChunkClaimsManager;
import dev.huskuraft.effortless.api.plugin.openpac.OpenPacClientPlugin;
import xaero.pac.client.api.OpenPACClientAPI;

@AutoService(OpenPacClientPlugin.class)
public class MinecraftOpenPacClientPlugin implements OpenPacClientPlugin {

    @Override
    public String getId() {
        return MinecraftOpenPacPlugin.ID;
    }

    @Override
    public void init() {
    }

    @Override
    public ChunkClaimsManager getClaimManager() {
        return MinecraftChunkClaimsManager.ofNullable(OpenPACClientAPI.get().getClaimsManager());
    }

}
