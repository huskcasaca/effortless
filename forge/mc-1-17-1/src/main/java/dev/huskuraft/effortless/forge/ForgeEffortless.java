package dev.huskuraft.effortless.forge;

import org.apache.commons.lang3.tuple.Pair;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.forge.platform.ForgePlatform;
import dev.huskuraft.effortless.vanilla.core.MinecraftBuffer;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import dev.huskuraft.effortless.vanilla.core.MinecraftServer;
import dev.huskuraft.effortless.vanilla.core.MinecraftWorld;
import dev.huskuraft.effortless.vanilla.platform.MinecraftCommonContentFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.event.EventNetworkChannel;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppedEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppingEvent;

public class ForgeEffortless extends Effortless {

    public static EventNetworkChannel CHANNEL;

    public ForgeEffortless() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        CHANNEL = NetworkRegistry.ChannelBuilder.named(getChannel().getChannelId().reference())
                .clientAcceptedVersions(getChannel().getCompatibilityVersionStr()::equals)
                .serverAcceptedVersions(getChannel().getCompatibilityVersionStr()::equals)
                .networkProtocolVersion(getChannel()::getCompatibilityVersionStr)
                .eventNetworkChannel();
    }

    @Override
    public Platform getPlatform() {
        return ForgePlatform.INSTANCE;
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        getEventRegistry().getRegisterNetworkEvent().invoker().onRegisterNetwork(receiver -> {
            ForgeEffortless.CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().get().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    receiver.receiveBuffer(new MinecraftBuffer(event1.getPayload()), new MinecraftPlayer(event1.getSource().get().getSender()));
                }
            });
            return (buffer, player) -> {
                var minecraftPacket = NetworkDirection.PLAY_TO_CLIENT.buildPacket(Pair.of(buffer.reference(), -1), getChannel().getChannelId().reference()).getThis();
                ((ServerPlayer) player.reference()).connection.send(minecraftPacket);
            };
        });
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        getEventRegistry().getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(new MinecraftPlayer(event.getPlayer()), new MinecraftWorld(event.getEntity().getServer().getLevel(event.getFrom())), new MinecraftWorld(event.getEntity().getServer().getLevel(event.getTo())));
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        getEventRegistry().getPlayerRespawnEvent().invoker().onPlayerRespawn(new MinecraftPlayer(event.getPlayer()), new MinecraftPlayer(event.getPlayer()), event.isEndConquered());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        getEventRegistry().getPlayerLoggedInEvent().invoker().onPlayerLoggedIn(new MinecraftPlayer(event.getPlayer()));
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        getEventRegistry().getPlayerLoggedOutEvent().invoker().onPlayerLoggedOut(new MinecraftPlayer(event.getPlayer()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        getEventRegistry().getServerStartingEvent().invoker().onServerStarting(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onSeverrStarted(FMLServerStartedEvent event) {
        getEventRegistry().getServerStartedEvent().invoker().onServerStarted(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        getEventRegistry().getServerStoppingEvent().invoker().onServerStopping(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopped(FMLServerStoppedEvent event) {
        getEventRegistry().getServerStoppedEvent().invoker().onServerStopped(new MinecraftServer(event.getServer()));
    }

}
