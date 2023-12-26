package dev.huskuraft.effortless.forge;

import com.mojang.blaze3d.platform.InputConstants;
import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.core.InteractionType;
import dev.huskuraft.effortless.core.TickPhase;
import dev.huskuraft.effortless.platform.ClientPlatform;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftClientAdapter;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClientPlatform;
import dev.huskuraft.effortless.vanilla.renderer.BlockRenderType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
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
    public ClientPlatform getGamePlatform() {
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
        onClientStart(MinecraftClientAdapter.adapt(Minecraft.getInstance()));
        onRegisterNetwork(receiver -> {
            ForgeEffortless.CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    try {
                        receiver.receiveBuffer(MinecraftClientAdapter.adapt(event1.getPayload()), MinecraftClientAdapter.adapt(Minecraft.getInstance().player));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return (buffer, player) -> {
                Minecraft.getInstance().getConnection().send(NetworkDirection.PLAY_TO_SERVER.buildPacket(MinecraftClientAdapter.adapt(buffer), ForgeEffortless.CHANNEL.getName()).getThis());
            };
        });
    }

    @SubscribeEvent
    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        onRegisterKeys(key -> {
            var keyMapping = new KeyMapping(key.getName(), InputConstants.Type.KEYSYM, MinecraftClientAdapter.adapt(key.getDefaultKey()), key.getCategory());
            key.bindKeyMapping(MinecraftClientAdapter.adapt(keyMapping));
            event.register(keyMapping);
        });
    }

    @SubscribeEvent
    public void onReloadShader(RegisterShadersEvent event) {
        BlockRenderType.Shaders.registerShaders(event.getResourceProvider(), event::registerShader);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        onClientTick(MinecraftClientAdapter.adapt(Minecraft.getInstance()), switch (event.phase) {
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
        onKeyPress(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers());
    }

    @SubscribeEvent
    public void onInteractionInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (onClientPlayerInteract(MinecraftClientAdapter.adapt(Minecraft.getInstance().player), event.isAttack() ? InteractionType.HIT : event.isUseItem() ? InteractionType.USE : InteractionType.UNKNOWN, MinecraftClientAdapter.adapt(event.getHand())).interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            event.setSwingHand(false);
        }
    }

}
