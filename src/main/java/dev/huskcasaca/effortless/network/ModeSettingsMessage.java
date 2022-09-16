package dev.huskcasaca.effortless.network;

import dev.huskcasaca.effortless.buildmode.BuildMode;
import dev.huskcasaca.effortless.buildmode.BuildModeHandler;
import dev.huskcasaca.effortless.buildmode.ModeSettingsManager;
import dev.huskcasaca.effortless.buildmode.ModeSettingsManager.ModeSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Shares mode settings (see ModeSettingsManager) between server and client
 */
public class ModeSettingsMessage implements Message {

    private ModeSettings modeSettings;

    public ModeSettingsMessage() {
    }

    public ModeSettingsMessage(ModeSettings modeSettings) {
        this.modeSettings = modeSettings;
    }

    public static void encode(ModeSettingsMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.modeSettings.getBuildMode().ordinal());
    }

    public static ModeSettingsMessage decode(FriendlyByteBuf buf) {
        BuildMode buildMode = BuildMode.values()[buf.readInt()];

        return new ModeSettingsMessage(new ModeSettings(buildMode));
    }

    public static class Serializer implements MessageSerializer<ModeSettingsMessage> {

        @Override
        public void encode(ModeSettingsMessage message, FriendlyByteBuf buf) {
            ModeSettingsMessage.encode(message, buf);
        }

        @Override
        public ModeSettingsMessage decode(FriendlyByteBuf buf) {
            return ModeSettingsMessage.decode(buf);
        }

    }

    public static class ServerHandler implements ServerMessageHandler<ModeSettingsMessage> {

        @Override
        public void handleServerSide(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, ModeSettingsMessage message, PacketSender responseSender) {
            server.execute(() -> {
                ModeSettingsManager.sanitize(message.modeSettings, player);
                ModeSettingsManager.setModeSettings(player, message.modeSettings);
                BuildModeHandler.initializeMode(player);
            });
        }

    }

    @Environment(EnvType.CLIENT)
    public static class ClientHandler implements ClientMessageHandler<ModeSettingsMessage> {

        @Override
        public void handleClientSide(Minecraft client, LocalPlayer player, ClientPacketListener handler, ModeSettingsMessage message, PacketSender responseSender) {
            client.execute(() -> {
                ModeSettingsManager.sanitize(message.modeSettings, player);
                ModeSettingsManager.setModeSettings(player, message.modeSettings);
                BuildModeHandler.initializeMode(player);
            });
        }

    }
}
