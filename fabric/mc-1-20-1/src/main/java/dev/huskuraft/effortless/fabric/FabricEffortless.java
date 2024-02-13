package dev.huskuraft.effortless.fabric;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.fabric.events.ServerPlayerEvents;
import dev.huskuraft.effortless.vanilla.core.MinecraftBuffer;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.core.MinecraftServer;
import dev.huskuraft.effortless.vanilla.core.MinecraftWorld;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

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

}
