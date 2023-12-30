package dev.huskuraft.effortless.events;

import dev.huskuraft.effortless.events.api.Event;
import dev.huskuraft.effortless.events.api.EventFactory;
import dev.huskuraft.effortless.events.input.InteractionInput;
import dev.huskuraft.effortless.events.input.KeyInput;
import dev.huskuraft.effortless.events.input.RegisterKeys;
import dev.huskuraft.effortless.events.lifecycle.ClientStart;
import dev.huskuraft.effortless.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.events.render.RenderGui;
import dev.huskuraft.effortless.events.render.RenderWorld;

public class ClientEventsRegistry extends EventsRegistry {

    private final Event<RegisterKeys> registerKeysEvent = EventFactory.createLoop();
    private final Event<KeyInput> keyInputEvent = EventFactory.createLoop();
    private final Event<InteractionInput> interactionInputEvent = EventFactory.createEventResult();

    private final Event<ClientStart> clientStartEvent = EventFactory.createLoop();
    private final Event<ClientTick> clientTickEvent = EventFactory.createLoop();

    private final Event<RenderGui> renderGuiEvent = EventFactory.createLoop();
    private final Event<RenderWorld> renderWorldEvent = EventFactory.createLoop();

    public Event<RegisterKeys> getRegisterKeysEvent() {
        return registerKeysEvent;
    }

    public Event<KeyInput> getKeyInputEvent() {
        return keyInputEvent;
    }

    public Event<InteractionInput> getInteractionInputEvent() {
        return interactionInputEvent;
    }

    public Event<ClientStart> getClientStartEvent() {
        return clientStartEvent;
    }

    public Event<ClientTick> getClientTickEvent() {
        return clientTickEvent;
    }

    public Event<RenderGui> getRenderGuiEvent() {
        return renderGuiEvent;
    }

    public Event<RenderWorld> getRenderWorldEvent() {
        return renderWorldEvent;
    }

}
