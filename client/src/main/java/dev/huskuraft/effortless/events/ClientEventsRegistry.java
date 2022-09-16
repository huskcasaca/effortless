package dev.huskuraft.effortless.events;

import dev.huskuraft.effortless.events.api.Event;
import dev.huskuraft.effortless.events.api.EventFactory;
import dev.huskuraft.effortless.events.input.KeyPress;
import dev.huskuraft.effortless.events.input.RegisterKeys;
import dev.huskuraft.effortless.events.lifecycle.ClientStart;
import dev.huskuraft.effortless.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.events.player.ClientPlayerInteract;
import dev.huskuraft.effortless.events.render.RenderGui;
import dev.huskuraft.effortless.events.render.RenderWorld;

public class ClientEventsRegistry extends EventsRegistry {

    private final Event<RegisterKeys> registerKeysEvent = EventFactory.createLoop();
    private final Event<KeyPress> keyPressEvent = EventFactory.createLoop();

    private final Event<ClientStart> clientStartEvent = EventFactory.createLoop();
    private final Event<ClientTick> clientTickEvent = EventFactory.createLoop();

    private final Event<RenderGui> renderGuiEvent = EventFactory.createLoop();
    private final Event<RenderWorld> renderEndEvent = EventFactory.createLoop();

    private final Event<ClientPlayerInteract> clientPlayerInteractEvent = EventFactory.createEventResult();

    public Event<RegisterKeys> onRegisterKeys() {
        return registerKeysEvent;
    }

    public Event<KeyPress> onKeyPress() {
        return keyPressEvent;
    }

    public Event<ClientStart> onClientStart() {
        return clientStartEvent;
    }

    public Event<ClientTick> onClientTick() {
        return clientTickEvent;
    }

    public Event<RenderGui> onRenderGui() {
        return renderGuiEvent;
    }

    public Event<RenderWorld> onRenderWorld() {
        return renderEndEvent;
    }

    public Event<ClientPlayerInteract> onClientPlayerInteract() {
        return clientPlayerInteractEvent;
    }

}
