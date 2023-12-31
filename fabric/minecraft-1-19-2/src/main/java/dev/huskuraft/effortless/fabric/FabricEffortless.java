package dev.huskuraft.effortless.fabric;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.platform.Platform;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftBuffer;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftResource;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftWorld;
import dev.huskuraft.effortless.vanilla.platform.MinecraftCommonPlatform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public class FabricEffortless extends Effortless implements ModInitializer {

    @Override
    public void onInitialize() {
        getEventRegistry().getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            var channelId = MinecraftResource.toMinecraftResource(getChannel().getChannelId());
            ServerPlayNetworking.registerGlobalReceiver(channelId, (server, player, handler, buf, responseSender) -> {
                receiver.receiveBuffer(MinecraftBuffer.fromMinecraftBuffer(buf), MinecraftPlayer.fromMinecraftPlayer(player));
            });
            return (buffer, player) -> ServerPlayNetworking.send((ServerPlayer) MinecraftPlayer.toMinecraftPlayer(player), channelId, MinecraftBuffer.toMinecraftBuffer(buffer));
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            getEventRegistry().getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(MinecraftPlayer.fromMinecraftPlayer(player), MinecraftWorld.fromMinecraftWorld(origin), MinecraftWorld.fromMinecraftWorld(destination));
        });

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            getEventRegistry().getPlayerCloneEvent().invoker().onPlayerClone(MinecraftPlayer.fromMinecraftPlayer(oldPlayer), MinecraftPlayer.fromMinecraftPlayer(newPlayer), !alive);
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
    public Platform getPlatform() {
        return new MinecraftCommonPlatform();
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
