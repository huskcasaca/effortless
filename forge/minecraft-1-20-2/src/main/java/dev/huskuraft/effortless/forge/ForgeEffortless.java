package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.Platform;
import dev.huskuraft.effortless.vanilla.adapters.*;
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
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import net.minecraftforge.network.NetworkDirection;

import java.nio.file.Path;

@Mod(Effortless.MOD_ID)
@Mod.EventBusSubscriber(modid = Effortless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeEffortless extends Effortless {

    public static EventNetworkChannel CHANNEL;

    public ForgeEffortless() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        CHANNEL = ChannelBuilder.named(getChannel().getChannelId().<ResourceLocation>reference())
                .acceptedVersions((status, version) -> true)
                .optional()
                .networkProtocolVersion(getChannel().getCompatibilityVersion())
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
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    try {
                        receiver.receiveBuffer(MinecraftConvertor.fromPlatformBuffer(event1.getPayload()), MinecraftConvertor.fromPlatformPlayer(event1.getSource().getSender()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return (buffer, player) -> {
                ((ServerPlayer) MinecraftConvertor.toPlatformPlayer(player)).connection.send(NetworkDirection.PLAY_TO_CLIENT.buildPacket(MinecraftConvertor.toPlatformBuffer(buffer), getChannel().getChannelId().reference()).getThis());
            };
        });
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        getEventRegistry().getPlayerChangeWorldEvent().invoker().onPlayerChangeWorld(MinecraftConvertor.fromPlatformPlayer(event.getEntity()), MinecraftConvertor.fromPlatformWorld(event.getEntity().getServer().getLevel(event.getFrom())), MinecraftConvertor.fromPlatformWorld(event.getEntity().getServer().getLevel(event.getTo())));
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        getEventRegistry().getPlayerRespawnEvent().invoker().onPlayerRespawn(MinecraftConvertor.fromPlatformPlayer(event.getEntity()), MinecraftConvertor.fromPlatformPlayer(event.getEntity()), event.isEndConquered());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        getEventRegistry().getPlayerLoggedInEvent().invoker().onPlayerLoggedIn(MinecraftConvertor.fromPlatformPlayer(event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        getEventRegistry().getPlayerLoggedOutEvent().invoker().onPlayerLoggedOut(MinecraftConvertor.fromPlatformPlayer(event.getEntity()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        getEventRegistry().getServerStartingEvent().invoker().onServerStarting(MinecraftConvertor.fromPlatformServer(event.getServer()));
    }

    @SubscribeEvent
    public void onSeverrStarted(ServerStartedEvent event) {
        getEventRegistry().getServerStartedEvent().invoker().onServerStarted(MinecraftConvertor.fromPlatformServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        getEventRegistry().getServerStoppingEvent().invoker().onServerStopping(MinecraftConvertor.fromPlatformServer(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        getEventRegistry().getServerStoppedEvent().invoker().onServerStopped(MinecraftConvertor.fromPlatformServer(event.getServer()));
    }

}
