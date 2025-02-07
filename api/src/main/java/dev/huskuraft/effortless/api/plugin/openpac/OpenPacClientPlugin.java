package dev.huskuraft.effortless.api.plugin.openpac;

import dev.huskuraft.effortless.api.platform.ClientPlugin;

public interface OpenPacClientPlugin extends ClientPlugin {

    OpenPacChunkClaimsManager getClaimManager();

}
