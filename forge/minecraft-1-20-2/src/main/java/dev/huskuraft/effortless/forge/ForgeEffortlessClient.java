package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.input.InputKey;
import dev.huskuraft.effortless.api.platform.ClientPlatform;
import dev.huskuraft.effortless.vanilla.core.*;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClientPlatform;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftRenderer;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkDirection;

import java.nio.file.Path;

@EventBusSubscriber(modid = Effortless.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeEffortlessClient extends EffortlessClient {

    public ForgeEffortlessClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeyMappings);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onReloadShader);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getLoaderName() {
        return "Forge";
    }

    @Override
    public String getLoaderVersion() {
        return FMLLoader.versionInfo().forgeVersion();
    }

    @Override
    public String getGameVersion() {
        return FMLLoader.versionInfo().mcVersion();
    }

    @Override
    public Path getGameDir() {
        return FMLLoader.getGamePath();
    }

    @Override
    public Path getConfigDir() {
        return FMLLoader.getGamePath().resolve("config");
    }

    @Override
    public ClientPlatform getPlatform() {
        return new MinecraftClientPlatform();
    }

    @Override
    public Environment getEnvironment() {
        return switch (FMLLoader.getDist()) {
            case CLIENT -> Environment.CLIENT;
            case DEDICATED_SERVER -> Environment.SERVER;
        };
    }

    @Override
    public boolean isDevelopment() {
        return !FMLLoader.isProduction();
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        getEventRegistry().getClientStartEvent().invoker().onClientStart(new MinecraftClient(Minecraft.getInstance()));
        getEventRegistry().getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            ForgeEffortless.CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    receiver.receiveBuffer(new MinecraftBuffer(event1.getPayload()), new MinecraftPlayer(Minecraft.getInstance().player));
                }
            });
            return (buffer, player) -> {
                var minecraftPacket = NetworkDirection.PLAY_TO_SERVER.buildPacket(buffer.reference(), getChannel().getChannelId().reference()).getThis();
                Minecraft.getInstance().getConnection().send(minecraftPacket);
            };
        });
    }

    @SubscribeEvent
    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        getEventRegistry().getRegisterKeysEvent().invoker().onRegisterKeys(key -> {
            event.register(key.getBinding().reference());
        });
    }

    @SubscribeEvent
    public void onReloadShader(RegisterShadersEvent event) {
        getEventRegistry().getRegisterShaderEvent().invoker().onRegisterShader((resource, format, consumer) -> {
            var minecraftShader = new ShaderInstance(event.getResourceProvider(), resource.getPath(), format.reference());
            event.registerShader(minecraftShader, shaderInstance -> consumer.accept(new MinecraftShader(shaderInstance)));
        });
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        getEventRegistry().getClientTickEvent().invoker().onClientTick(new MinecraftClient(Minecraft.getInstance()), switch (event.phase) {
            case START -> ClientTick.Phase.START;
            case END -> ClientTick.Phase.END;
        });
    }

    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        var renderer = new MinecraftRenderer(event.getPoseStack());
        var partialTick = event.getPartialTick();
        getEventRegistry().getRenderWorldEvent().invoker().onRenderWorld(renderer, partialTick);
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent event) {
        getEventRegistry().getRenderGuiEvent().invoker().onRenderGui(new MinecraftRenderer(event.getGuiGraphics().pose()), event.getPartialTick());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        getEventRegistry().getKeyInputEvent().invoker().onKeyInput(new InputKey(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));
    }

    @SubscribeEvent
    public void onInteractionInput(InputEvent.InteractionKeyMappingTriggered event) {
        var type = event.isAttack() ? InteractionType.ATTACK : event.isUseItem() ? InteractionType.USE_ITEM : InteractionType.UNKNOWN;
        var hand = MinecraftConvertor.fromPlatformInteractionHand(event.getHand());
        if (getEventRegistry().getInteractionInputEvent().invoker().onInteractionInput(type, hand).interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

}
