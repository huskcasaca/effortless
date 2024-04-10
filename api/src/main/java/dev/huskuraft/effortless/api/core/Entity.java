package dev.huskuraft.effortless.api.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.platform.Server;
import dev.huskuraft.effortless.api.text.Text;

public interface Entity extends PlatformReference {

    UUID getId();

    boolean isDeadOrDying();

    Client getClient();

    Server getServer();

    World getWorld();

    Text getDisplayName();

    Vector3d getPosition();

    Vector3d getEyePosition();

    Vector3d getEyeDirection();

    GameMode getGameType();

    BlockInteraction raytrace(double maxDistance, float deltaTick, boolean includeFluids);

}
