package dev.huskuraft.effortless.fabric;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.core.InteractionType;
import dev.huskuraft.effortless.core.TickPhase;
import dev.huskuraft.effortless.fabric.events.GuiRenderEvents;
import dev.huskuraft.effortless.fabric.events.InteractionInputEvents;
import dev.huskuraft.effortless.fabric.events.KeyboardInputEvents;
import dev.huskuraft.effortless.fabric.events.RegisterShadersEvents;
import dev.huskuraft.effortless.input.InputKey;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.platform.ClientPlatform;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.vanilla.adapters.*;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClientPlatform;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftBlockRenderTextures;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;

public class FabricEffortlessClient extends EffortlessClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Client client1 = MinecraftClient.fromMinecraftClient(Minecraft.getInstance());
        getEventRegistry().getClientStartEvent().invoker().onClientStart(client1);
        getEventRegistry().getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            var channelId = MinecraftResource.toMinecraftResource(getChannel().getChannelId());
            ClientPlayNetworking.registerGlobalReceiver(channelId, (client, handler, buf, responseSender) -> {
                receiver.receiveBuffer(MinecraftBuffer.fromMinecraftBuffer(buf), MinecraftPlayer.fromMinecraftPlayer(client.player));
            });
            return (buffer, player1) -> ClientPlayNetworking.send(channelId, MinecraftBuffer.toMinecraftBuffer(buffer));
        });

        getEventRegistry().getRegisterKeysEvent().invoker().onRegisterKeys(key1 -> {
            KeyBindingHelper.registerKeyBinding(MinecraftKeyBinding.toMinecraft(key1.getBinding()));
        });

        RegisterShadersEvents.REGISTER_SHADERS.register((provider, sink) -> {
            MinecraftBlockRenderTextures.Shaders.registerShaders(provider, sink::register);
        });

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            Client client = MinecraftClient.fromMinecraftClient(minecraft);
            getEventRegistry().getClientTickEvent().invoker().onClientTick(client, TickPhase.START);
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            Client client = MinecraftClient.fromMinecraftClient(minecraft);
            getEventRegistry().getClientTickEvent().invoker().onClientTick(client, TickPhase.END);
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            Renderer renderer = new MinecraftRenderer(context.matrixStack(), Minecraft.getInstance().renderBuffers().bufferSource());
            float deltaTick = context.tickDelta();
            getEventRegistry().getRenderWorldEvent().invoker().onRenderWorld(renderer, deltaTick);
        });

        GuiRenderEvents.RENDER_GUI.register((guiGraphics, f) -> {
            Renderer renderer = new MinecraftRenderer(guiGraphics);
            getEventRegistry().getRenderGuiEvent().invoker().onRenderGui(renderer, f);
        });

        KeyboardInputEvents.KEY_INPUT.register((key, scanCode, action, modifiers) -> {
            InputKey key1 = new InputKey(key, scanCode, action, modifiers);
            getEventRegistry().getKeyInputEvent().invoker().onKeyInput(key1);
        });

        InteractionInputEvents.ATTACK.register((player, hand) -> {
            return getEventRegistry().getInteractionInputEvent().invoker().onInteractionInput(InteractionType.ATTACK, MinecraftPlayer.fromMinecraftInteractionHand(hand)).interruptsFurtherEvaluation();
        });

        InteractionInputEvents.USE_ITEM.register((player, hand) -> {
            return getEventRegistry().getInteractionInputEvent().invoker().onInteractionInput(InteractionType.USE_ITEM, MinecraftPlayer.fromMinecraftInteractionHand(hand)).interruptsFurtherEvaluation();
        });

    }

    @Override
    public String getLoaderName() {
        return "Fabric-Official";
    }

    @Override
    public String getLoaderVersion() {
        return FabricLoaderImpl.VERSION;
    }

    @Override
    public String getGameVersion() {
        return FabricLoaderImpl.INSTANCE.getGameProvider().getRawGameVersion();
    }

    @Override
    public Path getGameDir() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public ClientPlatform getPlatform() {
        return new MinecraftClientPlatform();
    }

    @Override
    public Environment getEnvironment() {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> Environment.CLIENT;
            case SERVER -> Environment.SERVER;
        };
    }

    @Override
    public boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

}
