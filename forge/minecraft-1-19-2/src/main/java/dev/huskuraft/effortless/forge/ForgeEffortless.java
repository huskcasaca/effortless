package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.vanilla.core.*;
import dev.huskuraft.effortless.vanilla.platform.MinecraftCommonPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;

@Mod(Effortless.MOD_ID)
@Mod.EventBusSubscriber(modid = Effortless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ForgeEffortlessClient::new);
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
    public Platform getPlatform() {
        return new MinecraftCommonPlatform();
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
        getEventRegistry().getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(new MinecraftPlayer(event.getEntity()), new MinecraftWorld(event.getEntity().getServer().getLevel(event.getFrom())), new MinecraftWorld(event.getEntity().getServer().getLevel(event.getTo())));
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        getEventRegistry().getPlayerRespawnEvent().invoker().onPlayerRespawn(new MinecraftPlayer(event.getEntity()), new MinecraftPlayer(event.getEntity()), event.isEndConquered());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        getEventRegistry().getPlayerLoggedInEvent().invoker().onPlayerLoggedIn(new MinecraftPlayer(event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        getEventRegistry().getPlayerLoggedOutEvent().invoker().onPlayerLoggedOut(new MinecraftPlayer(event.getEntity()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        getEventRegistry().getServerStartingEvent().invoker().onServerStarting(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onSeverrStarted(ServerStartedEvent event) {
        getEventRegistry().getServerStartedEvent().invoker().onServerStarted(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        getEventRegistry().getServerStoppingEvent().invoker().onServerStopping(new MinecraftServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        getEventRegistry().getServerStoppedEvent().invoker().onServerStopped(new MinecraftServer(event.getServer()));
    }

}
