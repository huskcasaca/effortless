package dev.huskuraft.effortless.fabric;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.fabric.events.ServerPlayerEvents;
import dev.huskuraft.effortless.vanilla.core.*;
import dev.huskuraft.effortless.vanilla.platform.MinecraftCommonPlatform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

public class FabricEffortless extends Effortless implements ModInitializer {

    @Override
    public void onInitialize() {
        getEventRegistry().getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            var channelId = (ResourceLocation) getChannel().getChannelId().reference();
            ServerPlayNetworking.registerGlobalReceiver(channelId, (server, player, handler, buf, responseSender) -> {
                receiver.receiveBuffer(new MinecraftBuffer(buf), new MinecraftPlayer(player));
            });
            return (buffer, player) -> ServerPlayNetworking.send(player.reference(), channelId, buffer.reference());
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            getEventRegistry().getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(new MinecraftPlayer(player), new MinecraftWorld(origin), new MinecraftWorld(destination));
        });
        ServerPlayerEvents.LOGGED_IN.register(player -> {
            getEventRegistry().getPlayerLoggedInEvent().invoker().onPlayerLoggedIn(new MinecraftPlayer(player));
        });
        ServerPlayerEvents.LOGGED_OUT.register(player -> {
            getEventRegistry().getPlayerLoggedOutEvent().invoker().onPlayerLoggedOut(new MinecraftPlayer(player));
        });
        ServerPlayerEvents.RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            getEventRegistry().getPlayerRespawnEvent().invoker().onPlayerRespawn(new MinecraftPlayer(oldPlayer), new MinecraftPlayer(newPlayer), alive);
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            getEventRegistry().getServerStartingEvent().invoker().onServerStarting(new MinecraftServer(server));
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            getEventRegistry().getServerStartedEvent().invoker().onServerStarted(new MinecraftServer(server));
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            getEventRegistry().getServerStoppingEvent().invoker().onServerStopping(new MinecraftServer(server));
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            getEventRegistry().getServerStoppedEvent().invoker().onServerStopped(new MinecraftServer(server));
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
