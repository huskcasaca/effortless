package dev.huskuraft.effortless.forge.events;

import com.google.auto.service.AutoService;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.events.impl.ClientEventRegistry;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.input.InputKey;
import dev.huskuraft.effortless.forge.platform.ForgeInitializer;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClient;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftRenderer;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@AutoService(ClientEventRegistry.class)
public class ForgeClientEventRegistry extends ClientEventRegistry {

    public ForgeClientEventRegistry() {
        ForgeInitializer.EVENT_BUS.addListener(this::onClientSetup);
        ForgeInitializer.EVENT_BUS.addListener(this::onRegisterNetwork);
        ForgeInitializer.EVENT_BUS.addListener(this::onRegisterKeyMappings);
        ForgeInitializer.EVENT_BUS.addListener(this::onReloadShader);

        NeoForge.EVENT_BUS.register(this);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        getClientStartEvent().invoker().onClientStart(new MinecraftClient(Minecraft.getInstance()));
    }

    public void onRegisterNetwork(RegisterPayloadHandlersEvent event) {
        getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
        });
    }

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        getRegisterKeysEvent().invoker().onRegisterKeys(key -> {
            event.register(key.getKeyBinding().reference());
        });
    }

    public void onReloadShader(RegisterShadersEvent event) {
        getRegisterShaderEvent().invoker().onRegisterShader((resource, format, consumer) -> {
            var minecraftShader = new ShaderInstance(event.getResourceProvider(), resource.getPath(), format.reference());
            event.registerShader(minecraftShader, shaderInstance -> consumer.accept(new MinecraftShader(shaderInstance)));
        });
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        getClientTickEvent().invoker().onClientTick(new MinecraftClient(Minecraft.getInstance()), ClientTick.Phase.START); //  switch (event.phase) {
//            case START -> ClientTick.Phase.START;
//            case END -> ClientTick.Phase.END;
//        });
    }

    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        var renderer = new MinecraftRenderer(new PoseStack());
        var partialTick = event.getPartialTick();
        getRenderWorldEvent().invoker().onRenderWorld(renderer, partialTick.getGameTimeDeltaPartialTick(false));
    }

    @SubscribeEvent
    public void onRenderGui(CustomizeGuiOverlayEvent event) {
        getRenderGuiEvent().invoker().onRenderGui(new MinecraftRenderer(event.getGuiGraphics().pose()), event.getPartialTick().getGameTimeDeltaPartialTick(false));
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        getKeyInputEvent().invoker().onKeyInput(new InputKey(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));
    }

    @SubscribeEvent
    public void onInteractionInput(InputEvent.InteractionKeyMappingTriggered event) {
        var type = event.isAttack() ? InteractionType.ATTACK : event.isUseItem() ? InteractionType.USE_ITEM : InteractionType.UNKNOWN;
        var hand = MinecraftConvertor.fromPlatformInteractionHand(event.getHand());
        if (getInteractionInputEvent().invoker().onInteractionInput(type, hand).interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

}
