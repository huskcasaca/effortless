package dev.huskuraft.effortless.fabric;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.events.EventRegistry;
import dev.huskuraft.effortless.fabric.events.PlatformLifecycleEvents;
import dev.huskuraft.effortless.fabric.events.ServerPlayerEvents;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.core.MinecraftServer;
import dev.huskuraft.effortless.vanilla.core.MinecraftWorld;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

@AutoService(EventRegistry.class)
public class FabricEventRegistry extends EventRegistry {

    public FabricEventRegistry() {
        PlatformLifecycleEvents.COMMON_START.register(() -> {
            getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            });
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(new MinecraftPlayer(player), new MinecraftWorld(origin), new MinecraftWorld(destination));
        });
        ServerPlayerEvents.LOGGED_IN.register(player -> {
            getPlayerLoggedInEvent().invoker().onPlayerLoggedIn(new MinecraftPlayer(player));
        });
        ServerPlayerEvents.LOGGED_OUT.register(player -> {
            getPlayerLoggedOutEvent().invoker().onPlayerLoggedOut(new MinecraftPlayer(player));
        });
        ServerPlayerEvents.RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            getPlayerRespawnEvent().invoker().onPlayerRespawn(new MinecraftPlayer(oldPlayer), new MinecraftPlayer(newPlayer), alive);
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            getServerStartingEvent().invoker().onServerStarting(new MinecraftServer(server));
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            getServerStartedEvent().invoker().onServerStarted(new MinecraftServer(server));
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            getServerStoppingEvent().invoker().onServerStopping(new MinecraftServer(server));
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            getServerStoppedEvent().invoker().onServerStopped(new MinecraftServer(server));
        });

    }

}
