package dev.huskuraft.effortless.vanilla.plugin.openpac;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.plugin.openpac.OpenPacChunkClaimsManager;
import dev.huskuraft.effortless.api.plugin.openpac.OpenPacClientPlugin;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.client.api.OpenPACClientAPI;

@AutoService(OpenPacClientPlugin.class)
public class OpenPacClientPluginImpl implements OpenPacClientPlugin {

    @Override
    public String getId() {
        return OpenPartiesAndClaims.MOD_ID;
    }

    @Override
    public void init() {
    }

    @Override
    public OpenPacChunkClaimsManager getClaimManager() {
        return OpenPacChunkClaimsManagerImpl.ofNullable(OpenPACClientAPI.get().getClaimsManager());
    }

}
