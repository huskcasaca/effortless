package dev.huskuraft.effortless.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.config.ConfigReader;
import dev.huskuraft.effortless.config.ConfigWriter;
import dev.huskuraft.effortless.content.ContentCreator;
import dev.huskuraft.effortless.core.InteractionType;
import dev.huskuraft.effortless.core.TickPhase;
import dev.huskuraft.effortless.fabric.events.GuiRenderEvents;
import dev.huskuraft.effortless.fabric.events.InteractionInputEvents;
import dev.huskuraft.effortless.fabric.events.KeyboardInputEvents;
import dev.huskuraft.effortless.fabric.events.RegisterShadersEvents;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftAdapter;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftClientAdapter;
import dev.huskuraft.effortless.vanilla.content.MinecraftClientContentCreator;
import dev.huskuraft.effortless.vanilla.renderer.BlockRenderType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.NbtIo;

import java.nio.file.Path;

public class FabricEffortlessClient extends EffortlessClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        onClientStart(MinecraftClientAdapter.adapt(Minecraft.getInstance()));
        onRegisterNetwork(receiver -> {
            var channelId = MinecraftAdapter.adapt(Effortless.CHANNEL_ID);
            ClientPlayNetworking.registerGlobalReceiver(channelId, (client, handler, buf, responseSender) -> {
                receiver.receiveBuffer(MinecraftClientAdapter.adapt(buf), MinecraftClientAdapter.adapt(client.player));
            });
            return (buffer, player) -> ClientPlayNetworking.send(channelId, MinecraftClientAdapter.adapt(buffer));
        });

        onRegisterKeys(key -> {
            var keyMapping = new KeyMapping(key.getName(), InputConstants.Type.KEYSYM, MinecraftClientAdapter.adapt(key.getDefaultKey()), key.getCategory());
            key.bindKeyMapping(MinecraftClientAdapter.adapt(keyMapping));
            KeyBindingHelper.registerKeyBinding(keyMapping);
        });

        RegisterShadersEvents.REGISTER_SHADERS.register((provider, sink) -> {
            BlockRenderType.Shaders.registerShaders(provider, sink::register);
        });

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            onClientTick(MinecraftClientAdapter.adapt(minecraft), TickPhase.START);
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            onClientTick(MinecraftClientAdapter.adapt(minecraft), TickPhase.END);
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            onRenderWorld(MinecraftClientAdapter.adapt(new GuiGraphics(Minecraft.getInstance(), context.matrixStack(), Minecraft.getInstance().renderBuffers().bufferSource())), context.tickDelta());
        });

        GuiRenderEvents.RENDER_GUI.register((guiGraphics, f) -> {
            onRenderGui(MinecraftClientAdapter.adapt(guiGraphics), f);
        });

        KeyboardInputEvents.KEY_PRESS.register((key, scanCode, action, modifiers) -> {
            onKeyPress(key, scanCode, action, modifiers);
        });

        InteractionInputEvents.ATTACK.register((player, hand) -> {
            return onClientPlayerInteract(MinecraftClientAdapter.adapt(player), InteractionType.HIT, MinecraftClientAdapter.adapt(hand)).interruptsFurtherEvaluation();
        });

        InteractionInputEvents.USE_ITEM.register((player, hand) -> {
            return onClientPlayerInteract(MinecraftClientAdapter.adapt(player), InteractionType.USE, MinecraftClientAdapter.adapt(hand)).interruptsFurtherEvaluation();
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
    public ConfigReader getConfigReader() {
        return input -> MinecraftClientAdapter.adapt(NbtIo.readCompressed(input));
    }

    @Override
    public ConfigWriter getConfigWriter() {
        return (output, config) -> NbtIo.writeCompressed(MinecraftClientAdapter.adapt(config), output);
    }

    @Override
    public ContentCreator getContentCreator() {
        return new MinecraftClientContentCreator();
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
