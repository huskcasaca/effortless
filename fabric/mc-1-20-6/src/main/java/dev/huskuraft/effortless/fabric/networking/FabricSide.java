package dev.huskuraft.effortless.fabric.networking;

import java.util.function.BiConsumer;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

interface FabricSide {
    void registerPayload(FabricPayload.Type<FabricPayload> type, FabricPayloadStreamCodec codec);

    void registerReceiver(FabricPayload.Type<FabricPayload> type, BiConsumer<FabricPayload, Player> receiver);

    void send(FabricPayload payload, Player player);

    static FabricSide of(Side side) {
        return switch (side) {
            case CLIENT -> new Client();
            case SERVER -> new Server();
        };
    }

    class Server implements FabricSide {

        @Override
        public void registerPayload(FabricPayload.Type<FabricPayload> type, FabricPayloadStreamCodec codec) {
            PayloadTypeRegistry.playC2S().register(type, codec);
        }

        @Override
        public void registerReceiver(FabricPayload.Type<FabricPayload> type, BiConsumer<FabricPayload, Player> receiver) {
            ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                receiver.accept(payload, MinecraftPlayer.ofNullable(context.player()));
            });
        }

        @Override
        public void send(FabricPayload payload, Player player) {
            ServerPlayNetworking.send(player.reference(), payload);
        }
    }

    class Client implements FabricSide {

        @Override
        public void registerPayload(FabricPayload.Type<FabricPayload> type, FabricPayloadStreamCodec codec) {
            PayloadTypeRegistry.playS2C().register(type, codec);
        }

        @Override
        public void registerReceiver(FabricPayload.Type<FabricPayload> type, BiConsumer<FabricPayload, Player> receiver) {
            ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                receiver.accept(payload, MinecraftPlayer.ofNullable(context.player()));
            });
        }

        @Override
        public void send(FabricPayload payload, Player player) {
            ClientPlayNetworking.send(payload);
        }
    }
}
