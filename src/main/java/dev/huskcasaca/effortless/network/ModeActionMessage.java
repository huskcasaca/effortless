package dev.huskcasaca.effortless.network;

import dev.huskcasaca.effortless.buildmode.BuildAction;
import dev.huskcasaca.effortless.buildmode.BuildActionHandler;
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
public class ModeActionMessage implements Message {

    private BuildAction action;

    public ModeActionMessage() {
    }

    public ModeActionMessage(BuildAction action) {
        this.action = action;
    }

    public static void encode(ModeActionMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.action.ordinal());
    }

    public static ModeActionMessage decode(FriendlyByteBuf buf) {
        BuildAction action = BuildAction.values()[buf.readInt()];
        return new ModeActionMessage(action);
    }

    public static class Serializer implements MessageSerializer<ModeActionMessage> {

        @Override
        public void encode(ModeActionMessage message, FriendlyByteBuf buf) {
            ModeActionMessage.encode(message, buf);
        }

        @Override
        public ModeActionMessage decode(FriendlyByteBuf buf) {
            return ModeActionMessage.decode(buf);
        }

    }

    public static class ServerHandler implements ServerMessageHandler<ModeActionMessage> {

        @Override
        public void handleServerSide(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, ModeActionMessage message, PacketSender responseSender) {
            server.execute(() -> BuildActionHandler.performAction(player, message.action));
        }

    }


    @Environment(EnvType.CLIENT)
    public static class ClientHandler implements ClientMessageHandler<ModeActionMessage> {

        @Override
        public void handleClientSide(Minecraft client, LocalPlayer player, ClientPacketListener handler, ModeActionMessage message, PacketSender responseSender) {
            client.execute(() -> BuildActionHandler.performAction(player, message.action));
        }
    }
}
