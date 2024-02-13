package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.events.Event;
import dev.huskuraft.effortless.api.events.EventRegistry;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStarted;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStarting;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopped;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopping;
import dev.huskuraft.effortless.api.events.networking.RegisterNetwork;
import dev.huskuraft.effortless.api.events.player.PlayerChangeWorld;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedIn;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedOut;
import dev.huskuraft.effortless.api.events.player.PlayerRespawn;

public class EffortlessEventRegistry extends EventRegistry {

    public Event<RegisterNetwork> getRegisterNetworkEvent() {
        return get(RegisterNetwork.class);
    }

    public Event<PlayerChangeWorld> getPlayerChangeWorldEvent() {
        return get(PlayerChangeWorld.class);
    }

    public Event<PlayerRespawn> getPlayerRespawnEvent() {
        return get(PlayerRespawn.class);
    }

    public Event<PlayerLoggedIn> getPlayerLoggedInEvent() {
        return get(PlayerLoggedIn.class);
    }

    public Event<PlayerLoggedOut> getPlayerLoggedOutEvent() {
        return get(PlayerLoggedOut.class);
    }

    public Event<ServerStarting> getServerStartingEvent() {
        return get(ServerStarting.class);
    }

    public Event<ServerStarted> getServerStartedEvent() {
        return get(ServerStarted.class);
    }

    public Event<ServerStopping> getServerStoppingEvent() {
        return get(ServerStopping.class);
    }

    public Event<ServerStopped> getServerStoppedEvent() {
        return get(ServerStopped.class);
    }
}
