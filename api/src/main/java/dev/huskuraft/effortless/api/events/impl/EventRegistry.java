package dev.huskuraft.effortless.api.events.impl;

import dev.huskuraft.effortless.api.events.Event;
import dev.huskuraft.effortless.api.events.EventHolder;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStarted;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStarting;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopped;
import dev.huskuraft.effortless.api.events.lifecycle.ServerStopping;
import dev.huskuraft.effortless.api.events.networking.RegisterNetwork;
import dev.huskuraft.effortless.api.events.player.PlayerChangeWorld;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedIn;
import dev.huskuraft.effortless.api.events.player.PlayerLoggedOut;
import dev.huskuraft.effortless.api.events.player.PlayerRespawn;

public class EventRegistry extends EventHolder {

    public Event<RegisterNetwork> getRegisterNetworkEvent() {
        return get();
    }

    public Event<PlayerChangeWorld> getPlayerChangeWorldEvent() {
        return get();
    }

    public Event<PlayerRespawn> getPlayerRespawnEvent() {
        return get();
    }

    public Event<PlayerLoggedIn> getPlayerLoggedInEvent() {
        return get();
    }

    public Event<PlayerLoggedOut> getPlayerLoggedOutEvent() {
        return get();
    }

    public Event<ServerStarting> getServerStartingEvent() {
        return get();
    }

    public Event<ServerStarted> getServerStartedEvent() {
        return get();
    }

    public Event<ServerStopping> getServerStoppingEvent() {
        return get();
    }

    public Event<ServerStopped> getServerStoppedEvent() {
        return get();
    }

}
