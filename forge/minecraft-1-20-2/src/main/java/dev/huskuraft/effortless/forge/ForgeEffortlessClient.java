package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.input.InputKey;
import dev.huskuraft.effortless.api.platform.ClientPlatform;
import dev.huskuraft.effortless.vanilla.adapters.*;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClientPlatform;
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
        getEventRegistry().getClientStartEvent().invoker().onClientStart(MinecraftClient.fromMinecraftClient(Minecraft.getInstance()));
        getEventRegistry().getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            ForgeEffortless.CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    try {
                        receiver.receiveBuffer(MinecraftBuffer.fromMinecraftBuffer(event1.getPayload()), MinecraftPlayer.fromMinecraftPlayer(Minecraft.getInstance().player));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return (buffer, player) -> {
                Minecraft.getInstance().getConnection().send(NetworkDirection.PLAY_TO_SERVER.buildPacket(MinecraftBuffer.toMinecraftBuffer(buffer), getChannel().getChannelId().reference()).getThis());
            };
        });
    }

    @SubscribeEvent
    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        getEventRegistry().getRegisterKeysEvent().invoker().onRegisterKeys(key -> {
            event.register(MinecraftKeyBinding.toMinecraft(key.getBinding()));
        });
    }

    @SubscribeEvent
    public void onReloadShader(RegisterShadersEvent event) {
        getEventRegistry().getRegisterShaderEvent().invoker().onRegisterShader((resource, format, consumer) -> event.registerShader(new ShaderInstance(event.getResourceProvider(), resource.getPath(), format.reference()), shaderInstance -> consumer.accept((MinecraftShader) () -> shaderInstance)));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        getEventRegistry().getClientTickEvent().invoker().onClientTick(MinecraftClient.fromMinecraftClient(Minecraft.getInstance()), switch (event.phase) {
            case START -> ClientTick.Phase.START;
            case END -> ClientTick.Phase.END;
        });
    }

    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            getEventRegistry().getRenderWorldEvent().invoker().onRenderWorld(MinecraftRenderer.fromMinecraft(event.getPoseStack()), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent event) {
        getEventRegistry().getRenderGuiEvent().invoker().onRenderGui(MinecraftRenderer.fromMinecraft(event.getGuiGraphics().pose()), event.getPartialTick());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        getEventRegistry().getKeyInputEvent().invoker().onKeyInput(new InputKey(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));
    }

    @SubscribeEvent
    public void onInteractionInput(InputEvent.InteractionKeyMappingTriggered event) {
        var type = event.isAttack() ? InteractionType.ATTACK : event.isUseItem() ? InteractionType.USE_ITEM : InteractionType.UNKNOWN;
        var hand = MinecraftPrimitives.fromMinecraftInteractionHand(event.getHand());
        if (getEventRegistry().getInteractionInputEvent().invoker().onInteractionInput(type, hand).interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

}
