package dev.huskuraft.effortless.api.plugin.ftbchunks;

import dev.huskuraft.effortless.api.platform.PlatformReference;

import java.util.UUID;

public interface FtbClaimedChunk extends PlatformReference {

    boolean isTeamMember(UUID uuid);

}
