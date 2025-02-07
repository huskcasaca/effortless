package dev.huskuraft.effortless.vanilla.plugin.openpac;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.plugin.openpac.OpenPacChunkClaimsManager;
import dev.huskuraft.effortless.api.plugin.openpac.OpenPacPlugin;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.api.OpenPACServerAPI;

@AutoService(OpenPacPlugin.class)
public class OpenPacPluginImpl implements OpenPacPlugin {

    @Override
    public String getId() {
        return OpenPartiesAndClaims.MOD_ID;
    }

    @Override
    public void init() {
    }

    @Override
    public OpenPacChunkClaimsManager getServerClaimManager(@Nonnull Server server) {
        return OpenPacChunkClaimsManagerImpl.ofNullable(OpenPACServerAPI.get(server.reference()).getServerClaimsManager());
    }

}
