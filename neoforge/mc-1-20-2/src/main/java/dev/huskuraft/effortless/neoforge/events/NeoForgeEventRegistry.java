package dev.huskuraft.effortless.neoforge.events;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.events.impl.EventRegistry;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.core.MinecraftServer;
import dev.huskuraft.effortless.vanilla.core.MinecraftWorld;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@AutoService(EventRegistry.class)
public class NeoForgeEventRegistry extends EventRegistry {


    public NeoForgeEventRegistry() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterNetwork);

        NeoForge.EVENT_BUS.register(this);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
    }

    public void onRegisterNetwork(FMLCommonSetupEvent event) {
        getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
        });
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(MinecraftPlayer.ofNullable(event.getEntity()), MinecraftWorld.ofNullable(event.getEntity().getServer().getLevel(event.getFrom())), MinecraftWorld.ofNullable(event.getEntity().getServer().getLevel(event.getTo())));
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        getPlayerRespawnEvent().invoker().onPlayerRespawn(MinecraftPlayer.ofNullable(event.getEntity()), MinecraftPlayer.ofNullable(event.getEntity()), event.isEndConquered());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        getPlayerLoggedInEvent().invoker().onPlayerLoggedIn(MinecraftPlayer.ofNullable(event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        getPlayerLoggedOutEvent().invoker().onPlayerLoggedOut(MinecraftPlayer.ofNullable(event.getEntity()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        getServerStartingEvent().invoker().onServerStarting(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onSeverrStarted(ServerStartedEvent event) {
        getServerStartedEvent().invoker().onServerStarted(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        getServerStoppingEvent().invoker().onServerStopping(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        getServerStoppedEvent().invoker().onServerStopped(new MinecraftServer(event.getServer()));
    }

}
