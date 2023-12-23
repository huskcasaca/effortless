package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.config.ConfigReader;
import dev.huskuraft.effortless.config.ConfigWriter;
import dev.huskuraft.effortless.content.ContentCreator;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftAdapter;
import dev.huskuraft.effortless.vanilla.content.MinecraftServerContentCreator;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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

    public static final EventNetworkChannel CHANNEL =
            ChannelBuilder.named(MinecraftAdapter.adapt(Effortless.CHANNEL_ID))
                    .acceptedVersions((status, version) -> true)
                    .optional()
                    .networkProtocolVersion(Effortless.COMPATIBILITY_VERSION)
                    .eventNetworkChannel();

    public ForgeEffortless() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);

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
    public ConfigReader getConfigReader() {
        return input -> MinecraftAdapter.adapt(NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap()));
    }

    @Override
    public ConfigWriter getConfigWriter() {
        return (output, config) -> NbtIo.writeCompressed(MinecraftAdapter.adapt(config), output);
    }

    @Override
    public ContentCreator getContentCreator() {
        return new MinecraftServerContentCreator();
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
        onRegisterNetwork(receiver -> {
            ForgeEffortless.CHANNEL.addListener(event1 -> {
                if (event1.getPayload() != null && event1.getSource().getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {
                    try {
                        receiver.receiveBuffer(MinecraftAdapter.adapt(event1.getPayload()), MinecraftAdapter.adapt(event1.getSource().getSender()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return (buffer, player) -> {
                ((ServerPlayer) MinecraftAdapter.adapt(player)).connection.send(NetworkDirection.PLAY_TO_CLIENT.buildPacket(MinecraftAdapter.adapt(buffer), ForgeEffortless.CHANNEL.getName()).getThis());
            };
        });
    }
}
