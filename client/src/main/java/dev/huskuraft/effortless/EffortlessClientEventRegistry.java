package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.events.Event;
import dev.huskuraft.effortless.api.events.input.InteractionInput;
import dev.huskuraft.effortless.api.events.input.KeyInput;
import dev.huskuraft.effortless.api.events.input.RegisterKeys;
import dev.huskuraft.effortless.api.events.lifecycle.ClientStart;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.events.render.RegisterShader;
import dev.huskuraft.effortless.api.events.render.RenderGui;
import dev.huskuraft.effortless.api.events.render.RenderWorld;

public class EffortlessClientEventRegistry extends EffortlessEventRegistry {

    public Event<RegisterKeys> getRegisterKeysEvent() {
        return get(RegisterKeys.class);
    }

    public Event<KeyInput> getKeyInputEvent() {
        return get(KeyInput.class);
    }

    public Event<InteractionInput> getInteractionInputEvent() {
        return get(InteractionInput.class);
    }

    public Event<ClientStart> getClientStartEvent() {
        return get(ClientStart.class);
    }

    public Event<ClientTick> getClientTickEvent() {
        return get(ClientTick.class);
    }

    public Event<RenderGui> getRenderGuiEvent() {
        return get(RenderGui.class);
    }

    public Event<RenderWorld> getRenderWorldEvent() {
        return get(RenderWorld.class);
    }

    public Event<RegisterShader> getRegisterShaderEvent() {
        return get(RegisterShader.class);
    }
}
