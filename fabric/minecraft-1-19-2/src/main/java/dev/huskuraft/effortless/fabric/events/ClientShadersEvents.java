package dev.huskuraft.effortless.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.function.Consumer;

public class ClientShadersEvents {

    public static final Event<RegisterShaders> REGISTER = EventFactory.createArrayBacked(RegisterShaders.class, callbacks -> (provider, sink) -> {
        for (var callback : callbacks) {
            callback.onRegisterShaders(provider, sink);
        }
    });

    @FunctionalInterface
    public interface RegisterShaders {
        void onRegisterShaders(ResourceProvider provider, ShadersSink sink) throws IOException;

        @FunctionalInterface
        interface ShadersSink {
            void register(ShaderInstance shader, Consumer<ShaderInstance> callback);
        }
    }

}
