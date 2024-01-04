package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.events.Event;
import dev.huskuraft.effortless.api.events.EventFactory;
import dev.huskuraft.effortless.api.events.networking.RegisterNetwork;
import dev.huskuraft.effortless.api.events.player.PlayerChangeWorld;
import dev.huskuraft.effortless.api.events.player.PlayerClone;

public class EffortlessEventsRegistry {

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
