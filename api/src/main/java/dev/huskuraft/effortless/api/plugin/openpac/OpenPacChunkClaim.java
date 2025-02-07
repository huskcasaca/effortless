package dev.huskuraft.effortless.api.plugin.openpac;

import dev.huskuraft.effortless.api.platform.PlatformReference;

import javax.annotation.Nonnull;

import java.util.UUID;

public interface OpenPacChunkClaim extends PlatformReference {

    @Nonnull
    UUID getPlayerId();

}
