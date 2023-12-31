package dev.huskuraft.effortless.events;

import dev.huskuraft.effortless.events.api.Event;
import dev.huskuraft.effortless.events.api.EventFactory;
import dev.huskuraft.effortless.events.networking.RegisterNetwork;
import dev.huskuraft.effortless.events.player.PlayerChangeWorld;
import dev.huskuraft.effortless.events.player.PlayerClone;

public class EventsRegistry {

    private final Event<RegisterNetwork> registerNetworkEvent = EventFactory.createLoop();

    private final Event<PlayerChangeWorld> playerChangeWorldEvent = EventFactory.createLoop();
    private final Event<PlayerClone> playerCloneEvent = EventFactory.createLoop();

    public Event<RegisterNetwork> getRegisterNetworkEvent() {
        return registerNetworkEvent;
    }

    public Event<PlayerChangeWorld> getPlayerChangeWorldEvent() {
        return playerChangeWorldEvent;
    }

    public Event<PlayerClone> getPlayerCloneEvent() {
        return playerCloneEvent;
    }
}
