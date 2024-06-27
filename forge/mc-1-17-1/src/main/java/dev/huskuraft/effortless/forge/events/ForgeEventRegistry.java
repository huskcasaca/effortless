package dev.huskuraft.effortless.forge.events;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.events.impl.EventRegistry;
import dev.huskuraft.effortless.forge.networking.ForgeNetworking;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.core.MinecraftServer;
import dev.huskuraft.effortless.vanilla.core.MinecraftWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppingEvent;

@AutoService(EventRegistry.class)
public class ForgeEventRegistry extends EventRegistry {


    public ForgeEventRegistry() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        getRegisterNetworkEvent().invoker().onRegisterNetwork(ForgeNetworking::register);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(MinecraftPlayer.ofNullable(event.getPlayer()), MinecraftWorld.ofNullable(event.getEntity().getServer().getLevel(event.getFrom())), MinecraftWorld.ofNullable(event.getEntity().getServer().getLevel(event.getTo())));
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        getPlayerRespawnEvent().invoker().onPlayerRespawn(MinecraftPlayer.ofNullable(event.getPlayer()), MinecraftPlayer.ofNullable(event.getPlayer()), event.isEndConquered());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        getPlayerLoggedInEvent().invoker().onPlayerLoggedIn(MinecraftPlayer.ofNullable(event.getPlayer()));
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        getPlayerLoggedOutEvent().invoker().onPlayerLoggedOut(MinecraftPlayer.ofNullable(event.getPlayer()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        getServerStartingEvent().invoker().onServerStarting(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onSeverStarted(FMLServerStartedEvent event) {
        getServerStartedEvent().invoker().onServerStarted(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        getServerStoppingEvent().invoker().onServerStopping(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopped(FMLServerStoppedEvent event) {
        getServerStoppedEvent().invoker().onServerStopped(new MinecraftServer(event.getServer()));
    }

}
