package dev.huskuraft.effortless.fabric;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.config.ConfigReader;
import dev.huskuraft.effortless.config.ConfigWriter;
import dev.huskuraft.effortless.content.ContentCreator;
import dev.huskuraft.effortless.vanilla.adapters.MinecraftAdapter;
import dev.huskuraft.effortless.vanilla.content.MinecraftServerContentCreator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

public class FabricEffortless extends Effortless implements ModInitializer {

    @Override
    public void onInitialize() {
        onRegisterNetwork(receiver -> {
            var channelId = MinecraftAdapter.adapt(Effortless.CHANNEL_ID);
            ServerPlayNetworking.registerGlobalReceiver(channelId, (server, player, handler, buf, responseSender) -> {
                receiver.receiveBuffer(MinecraftAdapter.adapt(buf), MinecraftAdapter.adapt(player));
            });
            return (buffer, player) -> ServerPlayNetworking.send((ServerPlayer) MinecraftAdapter.adapt(player), channelId, MinecraftAdapter.adapt(buffer));
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
    public ConfigReader getConfigReader() {
        return input -> MinecraftAdapter.adapt(NbtIo.readCompressed(input));
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
