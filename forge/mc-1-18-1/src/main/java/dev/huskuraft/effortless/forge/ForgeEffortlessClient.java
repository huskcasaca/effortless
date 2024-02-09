package dev.huskuraft.effortless.forge;

import org.apache.commons.lang3.tuple.Pair;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.api.events.lifecycle.ClientTick;
import dev.huskuraft.effortless.api.input.InputKey;
import dev.huskuraft.effortless.api.platform.ClientContentFactory;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.forge.platform.ForgePlatform;
import dev.huskuraft.effortless.vanilla.core.MinecraftBuffer;
import dev.huskuraft.effortless.vanilla.core.MinecraftClient;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.platform.MinecraftClientContentFactory;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftRenderer;
import dev.huskuraft.effortless.vanilla.renderer.MinecraftShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;

@EventBusSubscriber(modid = Effortless.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeEffortlessClient extends EffortlessClient {

    public ForgeEffortlessClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onReloadShader);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Platform getPlatform() {
        return ForgePlatform.INSTANCE;
    }

    @Override
    public ClientContentFactory getContentFactory() {
        return MinecraftClientContentFactory.INSTANCE;
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        getEventRegistry().getClientStartEvent().invoker().onClientStart(new MinecraftClient(Minecraft.getInstance()));
        getEventRegistry().getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            ForgeEffortless.CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                    receiver.receiveBuffer(new MinecraftBuffer(event1.getPayload()), new MinecraftPlayer(Minecraft.getInstance().player));
                }
            });
            return (buffer, player) -> {
                var minecraftPacket = NetworkDirection.PLAY_TO_SERVER.buildPacket(Pair.of(buffer.reference(), -1), getChannel().getChannelId().reference()).getThis();
                Minecraft.getInstance().getConnection().send(minecraftPacket);
            };
        });
		getEventRegistry().getRegisterKeysEvent().invoker().onRegisterKeys(key -> {
			ClientRegistry.registerKeyBinding(key.getBinding().reference());
		});
    }

    @SubscribeEvent
    public void onReloadShader(RegisterShadersEvent event) {
        getEventRegistry().getRegisterShaderEvent().invoker().onRegisterShader((resource, format, consumer) -> {
            var minecraftShader = new ShaderInstance(event.getResourceManager(), resource.getPath(), format.reference());
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
    public void onRenderLevelStage(RenderLevelLastEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
//            return;
//        }
        var renderer = new MinecraftRenderer(event.getPoseStack());
        var partialTick = event.getPartialTick();
        getEventRegistry().getRenderWorldEvent().invoker().onRenderWorld(renderer, partialTick);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        getEventRegistry().getRenderGuiEvent().invoker().onRenderGui(new MinecraftRenderer(event.getMatrixStack()), event.getPartialTicks());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        getEventRegistry().getKeyInputEvent().invoker().onKeyInput(new InputKey(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers()));
    }

    @SubscribeEvent
    public void onClickInput(InputEvent.ClickInputEvent event) {
        var type = event.isAttack() ? InteractionType.ATTACK : event.isUseItem() ? InteractionType.USE_ITEM : InteractionType.UNKNOWN;
        var hand = MinecraftConvertor.fromPlatformInteractionHand(event.getHand());
        if (getEventRegistry().getInteractionInputEvent().invoker().onInteractionInput(type, hand).interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

}
