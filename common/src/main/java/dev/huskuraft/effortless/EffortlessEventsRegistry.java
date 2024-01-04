package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.events.Event;
import dev.huskuraft.effortless.api.events.EventFactory;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStarted;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStarting;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopped;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopping;
import dev.huskuraft.effortless.api.events.networking.RegisterNetwork;
import dev.huskuraft.effortless.api.events.player.PlayerChangeWorld;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedIn;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedOut;
import dev.huskuraft.effortless.api.events.player.PlayerRespawn;

public class EffortlessEventsRegistry {

    private final Event<RegisterNetwork> registerNetworkEvent = EventFactory.createLoop();

    private final Event<PlayerChangeWorld> playerChangeWorldEvent = EventFactory.createLoop();
    private final Event<PlayerRespawn> playerRespawnEvent = EventFactory.createLoop();
    private final Event<PlayerLoggedIn> playerLoggedInEvent = EventFactory.createLoop();
    private final Event<PlayerLoggedOut> playerLoggedOutEvent = EventFactory.createLoop();

    private final Event<ServerStarting> serverStartingEvent = EventFactory.createLoop();
    private final Event<ServerStarted> serverStartedEvent = EventFactory.createLoop();
    private final Event<ServerStopping> serverStoppingEvent = EventFactory.createLoop();
    private final Event<ServerStopped> serverStoppedEvent = EventFactory.createLoop();

    public Event<RegisterNetwork> getRegisterNetworkEvent() {
        return registerNetworkEvent;
    }

    public Event<PlayerChangeWorld> getPlayerChangeWorldEvent() {
        return playerChangeWorldEvent;
    }

    public Event<PlayerRespawn> getPlayerRespawnEvent() {
        return playerRespawnEvent;
    }

    public Event<PlayerLoggedIn> getPlayerLoggedInEvent() {
        return playerLoggedInEvent;
    }

    public Event<PlayerLoggedOut> getPlayerLoggedOutEvent() {
        return playerLoggedOutEvent;
    }

    public Event<ServerStarting> getServerStartingEvent() {
        return serverStartingEvent;
    }

    public Event<ServerStarted> getServerStartedEvent() {
        return serverStartedEvent;
    }

    public Event<ServerStopping> getServerStoppingEvent() {
        return serverStoppingEvent;
    }

    public Event<ServerStopped> getServerStoppedEvent() {
        return serverStoppedEvent;
    }
}
