package dev.huskuraft.effortless.events;

import dev.huskuraft.effortless.events.api.Event;
import dev.huskuraft.effortless.events.api.EventFactory;
import dev.huskuraft.effortless.events.networking.RegisterNetwork;

public class EventsRegistry {

    private final Event<RegisterNetwork> registerNetworkEvent = EventFactory.createLoop();

    public Event<RegisterNetwork> onRegisterNetwork() {
        return registerNetworkEvent;
    }
}
