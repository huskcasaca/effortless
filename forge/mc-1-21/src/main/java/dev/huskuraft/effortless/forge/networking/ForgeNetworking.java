package dev.huskuraft.effortless.forge.networking;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;
import dev.huskuraft.effortless.vanilla.core.MinecraftPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.EventNetworkChannel;
import net.minecraftforge.network.PacketDistributor;

@AutoService(Networking.class)
public class ForgeNetworking implements Networking {

    private static final Map<ResourceLocation, EventNetworkChannel> MAP = new HashMap<>();

    private static void register(ResourceLocation channelId, Consumer<CustomPayloadEvent> eventConsumer) {
        MAP.computeIfAbsent(channelId, id -> {
            return ChannelBuilder.named((net.minecraft.resources.ResourceLocation) id.reference())
                    .acceptedVersions((status, i) -> true)
                    .eventNetworkChannel();
        }).addListener(eventConsumer);
    }

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        switch (side) {
            case CLIENT -> register(channelId, event -> {
                if (event.getPayload() != null && event.getSource().isClientSide()) {
                    receiver.receiveBuffer(event.getPayload(), MinecraftPlayer.ofNullable(event.getSource().getSender()));
                    event.getSource().setPacketHandled(true);
                }
            });
            case SERVER -> register(channelId, event -> {
                if (event.getPayload() != null && event.getSource().isServerSide()) {
                    receiver.receiveBuffer(event.getPayload(), MinecraftPlayer.ofNullable(event.getSource().getSender()));
                    event.getSource().setPacketHandled(true);
                }
            });
        }
        return switch (side) {
            case CLIENT ->
                    (byteBuf, player) -> MAP.get(channelId).send(new FriendlyByteBuf(byteBuf), PacketDistributor.SERVER.noArg());
            case SERVER ->
                    (byteBuf, player) -> MAP.get(channelId).send(new FriendlyByteBuf(byteBuf), PacketDistributor.PLAYER.with(player.reference()));
        };

    }

}
