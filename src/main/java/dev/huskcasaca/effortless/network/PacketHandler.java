package dev.huskcasaca.effortless.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class PacketHandler {

    public static Map<Class<? extends Message>, ResourceLocation> MSG_IDENTIFIER = new HashMap<>();
    public static Map<Class<? extends Message>, MessageSerializer<?>> MSG_SERIALIZER = new HashMap<>();
    public static Map<Class<? extends Message>, ServerMessageHandler<?>> SERVER_MSG_HANDLER = new HashMap<>();
    public static Map<Class<? extends Message>, ClientMessageHandler<?>> CLIENT_MSG_HANDLER = new HashMap<>();

    public static void register() {

        registerServerMessage(ModifierSettingsMessage.class, MessageChannel.MODIFIER_SETTINGS, new ModifierSettingsMessage.Serializer(), new ModifierSettingsMessage.ServerHandler());
        registerServerMessage(ModeSettingsMessage.class, MessageChannel.MODE_SETTINGS, new ModeSettingsMessage.Serializer(), new ModeSettingsMessage.ServerHandler());
        registerServerMessage(ModeActionMessage.class, MessageChannel.MODE_ACTION, new ModeActionMessage.Serializer(), new ModeActionMessage.ServerHandler());
        registerServerMessage(BlockPlacedMessage.class, MessageChannel.BLOCK_PLACED, new BlockPlacedMessage.Serializer(), new BlockPlacedMessage.ServerHandler());
        registerServerMessage(BlockBrokenMessage.class, MessageChannel.BLOCK_BROKEN, new BlockBrokenMessage.Serializer(), new BlockBrokenMessage.ServerHandler());
        registerServerMessage(CancelModeMessage.class, MessageChannel.CANCEL_MODE, new CancelModeMessage.Serializer(), new CancelModeMessage.ServerHandler());
        registerServerMessage(RequestLookAtMessage.class, MessageChannel.REQUEST_LOOK_AT, new RequestLookAtMessage.Serializer(), new RequestLookAtMessage.ServerHandler());
        registerServerMessage(AddUndoMessage.class, MessageChannel.ADD_UNDO, new AddUndoMessage.Serializer(), new AddUndoMessage.ServerHandler());
        registerServerMessage(ClearUndoMessage.class, MessageChannel.CLEAR_UNDO, new ClearUndoMessage.Serializer(), new ClearUndoMessage.ServerHandler());
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {

        registerClientMessage(ModifierSettingsMessage.class, MessageChannel.MODIFIER_SETTINGS, new ModifierSettingsMessage.Serializer(), new ModifierSettingsMessage.ClientHandler());
        registerClientMessage(ModeSettingsMessage.class, MessageChannel.MODE_SETTINGS, new ModeSettingsMessage.Serializer(), new ModeSettingsMessage.ClientHandler());
        registerClientMessage(ModeActionMessage.class, MessageChannel.MODE_ACTION, new ModeActionMessage.Serializer(), new ModeActionMessage.ClientHandler());
        registerClientMessage(BlockPlacedMessage.class, MessageChannel.BLOCK_PLACED, new BlockPlacedMessage.Serializer(), new BlockPlacedMessage.ClientHandler());
        registerClientMessage(BlockBrokenMessage.class, MessageChannel.BLOCK_BROKEN, new BlockBrokenMessage.Serializer(), new BlockBrokenMessage.ClientHandler());
        registerClientMessage(CancelModeMessage.class, MessageChannel.CANCEL_MODE, new CancelModeMessage.Serializer(), new CancelModeMessage.ClientHandler());
        registerClientMessage(RequestLookAtMessage.class, MessageChannel.REQUEST_LOOK_AT, new RequestLookAtMessage.Serializer(), new RequestLookAtMessage.ClientHandler());
        registerClientMessage(AddUndoMessage.class, MessageChannel.ADD_UNDO, new AddUndoMessage.Serializer(), new AddUndoMessage.ClientHandler());
        registerClientMessage(ClearUndoMessage.class, MessageChannel.CLEAR_UNDO, new ClearUndoMessage.Serializer(), new ClearUndoMessage.ClientHandler());
    }

    private static <M extends Message> void registerServerMessage(Class<M> clazz, ResourceLocation identifier, MessageSerializer<M> serializer, ServerMessageHandler<M> msgHandler) {
        MSG_IDENTIFIER.put(clazz, identifier);
        MSG_SERIALIZER.put(clazz, serializer);
        SERVER_MSG_HANDLER.put(clazz, msgHandler);
        ServerPlayNetworking.registerGlobalReceiver(identifier, (server, player, handler, buf, responseSender) -> {
            msgHandler.handleServerSide(server, player, handler, serializer.decode(buf), responseSender);
        });
    }


    private static <M extends Message> void registerClientMessage(Class<M> clazz, ResourceLocation identifier, MessageSerializer<M> serializer, ClientMessageHandler<M> msgHandler) {
        MSG_IDENTIFIER.put(clazz, identifier);
        MSG_SERIALIZER.put(clazz, serializer);
        CLIENT_MSG_HANDLER.put(clazz, msgHandler);
        ClientPlayNetworking.registerGlobalReceiver(identifier, (client, handler, buf, responseSender) -> {
            msgHandler.handleClientSide(client, client.player, handler, serializer.decode(buf), responseSender);
        });
    }

    @SuppressWarnings("unchecked")
    public static <M extends Message> void sendToServer(M message) {
        Class<?> clazz = message.getClass();
        ResourceLocation location = MSG_IDENTIFIER.get(clazz);
        if (location == null) {
            throw new IllegalArgumentException("Message " + clazz.getName() + " is not registered");
        }
        FriendlyByteBuf buf = PacketByteBufs.create();
        ServerMessageHandler<M> handler = (ServerMessageHandler<M>) SERVER_MSG_HANDLER.get(clazz);
        MessageSerializer<M> serializer = (MessageSerializer<M>) MSG_SERIALIZER.get(clazz);
        serializer.encode(message, buf);
        ClientPlayNetworking.send(location, buf);
    }

    @SuppressWarnings("unchecked")
    public static <M extends Message> void sendToClient(M message, ServerPlayer player) {
        Class<?> clazz = message.getClass();
        ResourceLocation location = MSG_IDENTIFIER.get(clazz);
        if (location == null) {
            throw new IllegalArgumentException("Message " + clazz.getName() + " is not registered");
        }
        FriendlyByteBuf buf = PacketByteBufs.create();
        MessageSerializer<M> serializer = (MessageSerializer<M>) MSG_SERIALIZER.get(clazz);
        serializer.encode(message, buf);
        serializer.encode(message, buf);
        ServerPlayNetworking.send(player, location, buf);
    }

}
