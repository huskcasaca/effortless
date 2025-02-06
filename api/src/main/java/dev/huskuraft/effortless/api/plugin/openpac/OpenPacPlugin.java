package dev.huskuraft.effortless.api.plugin.openpac;

import javax.annotation.Nonnull;

import dev.huskuraft.effortless.api.platform.Plugin;
import dev.huskuraft.effortless.api.platform.Server;

public interface OpenPacPlugin extends Plugin {

    ChunkClaimsManager getServerClaimManager(@Nonnull Server server);

}
