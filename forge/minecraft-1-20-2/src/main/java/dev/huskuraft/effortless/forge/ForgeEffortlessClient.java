package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.core.InteractionType;
import dev.huskuraft.effortless.core.TickPhase;
import dev.huskuraft.effortless.input.InputKey;
import dev.huskuraft.effortless.platform.ClientPlatform;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftBuffer;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftClient;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftClientAdapter;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftClientPlayer;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClientPlatform;
import dev.huskuraft.effortless.vanilla.renderer.BlockRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
        onClientStart(new MinecraftClient(Minecraft.getInstance()));
        onRegisterNetwork(receiver -> {
            ForgeEffortless.CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    try {
                        receiver.receiveBuffer(new MinecraftBuffer(event1.getPayload()), new MinecraftClientPlayer(Minecraft.getInstance().player));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return (buffer, player) -> {
                Minecraft.getInstance().getConnection().send(NetworkDirection.PLAY_TO_SERVER.buildPacket(((MinecraftBuffer) buffer).getRef(), ForgeEffortless.CHANNEL.getName()).getThis());
            };
        });
    }

    @SubscribeEvent
    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        onRegisterKeys(key -> {
            event.register(MinecraftClientAdapter.adapt(key.getBinding()));
        });
    }

    @SubscribeEvent
    public void onReloadShader(RegisterShadersEvent event) {
        BlockRenderType.Shaders.registerShaders(event.getResourceProvider(), event::registerShader);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        onClientTick(new MinecraftClient(Minecraft.getInstance()), switch (event.phase) {
            case START -> TickPhase.START;
            case END -> TickPhase.END;
        });
    }

    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            onRenderWorld(MinecraftClientAdapter.adapt(new GuiGraphics(Minecraft.getInstance(), event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource())), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent event) {
        onRenderGui(MinecraftClientAdapter.adapt(event.getGuiGraphics()), event.getPartialTick());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        onKeyInput(new InputKey(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));
    }

    @SubscribeEvent
    public void onInteractionInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (onInteractionInput(event.isAttack() ? InteractionType.ATTACK : event.isUseItem() ? InteractionType.USE_ITEM : InteractionType.UNKNOWN, MinecraftClientAdapter.adapt(event.getHand())).interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

}
