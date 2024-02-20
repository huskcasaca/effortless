package dev.huskuraft.effortless.fabric.events;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.events.ClientEventRegistry;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.input.InputKey;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClient;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftRenderer;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftShader;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;

@AutoService(ClientEventRegistry.class)
public class FabricClientEventRegistry extends ClientEventRegistry {

    public FabricClientEventRegistry() {
        PlatformLifecycleEvents.CLIENT_START.register(() -> {
            getClientStartEvent().invoker().onClientStart(new MinecraftClient(Minecraft.getInstance()));
            getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            });

            getRegisterKeysEvent().invoker().onRegisterKeys(key1 -> {
                KeyBindingHelper.registerKeyBinding(key1.getBinding().reference());
            });
        });

        ClientShadersEvents.REGISTER.register((provider, sink) -> {
            getRegisterShaderEvent().invoker().onRegisterShader((resource, format, consumer) -> sink.register(new ShaderInstance(provider, resource.getPath(), format.reference()), shaderInstance -> consumer.accept(new MinecraftShader(shaderInstance))));
        });

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            getClientTickEvent().invoker().onClientTick(new MinecraftClient(minecraft), ClientTick.Phase.START);
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            getClientTickEvent().invoker().onClientTick(new MinecraftClient(minecraft), ClientTick.Phase.END);
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            getRenderWorldEvent().invoker().onRenderWorld(new MinecraftRenderer(context.matrixStack()), context.tickDelta());
        });

        ClientRenderEvents.GUI.register((guiGraphics, f) -> {
            getRenderGuiEvent().invoker().onRenderGui(new MinecraftRenderer(guiGraphics.pose()), f);
        });

        KeyboardInputEvents.KEY_INPUT.register((key, scanCode, action, modifiers) -> {
            getKeyInputEvent().invoker().onKeyInput(new InputKey(key, scanCode, action, modifiers));
        });

        InteractionInputEvents.ATTACK.register((player, hand) -> {
            return getInteractionInputEvent().invoker().onInteractionInput(InteractionType.ATTACK, MinecraftConvertor.fromPlatformInteractionHand(hand)).interruptsFurtherEvaluation();
        });

        InteractionInputEvents.USE_ITEM.register((player, hand) -> {
            return getInteractionInputEvent().invoker().onInteractionInput(InteractionType.USE_ITEM, MinecraftConvertor.fromPlatformInteractionHand(hand)).interruptsFurtherEvaluation();
        });

    }

}
