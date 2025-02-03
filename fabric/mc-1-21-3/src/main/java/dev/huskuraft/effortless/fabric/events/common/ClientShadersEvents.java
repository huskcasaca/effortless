package dev.huskuraft.effortless.fabric.events.common;

import java.io.IOException;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.server.packs.resources.ResourceProvider;

public class ClientShadersEvents {

    public static final Event<Register> REGISTER = EventFactory.createArrayBacked(Register.class, callbacks -> (provider, sink) -> {
        for (var callback : callbacks) {
            callback.onRegisterShader(provider, sink);
        }
    });

    @FunctionalInterface
    public interface Register {
        void onRegisterShader(ResourceProvider provider, ShadersSink sink) throws IOException;

        @FunctionalInterface
        interface ShadersSink {
            void register(ShaderProgram shader, Consumer<ShaderProgram> callback);
        }
    }

}
