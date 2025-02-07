package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.client.FTBTeamsClient;
import dev.ftb.mods.ftbteams.client.KnownClientPlayerNet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdatePresenceMessage(KnownClientPlayer update) implements CustomPacketPayload {
	public static final Type<UpdatePresenceMessage> TYPE = new Type<>(FTBTeamsAPI.rl("update_presence"));

	public static final StreamCodec<FriendlyByteBuf, UpdatePresenceMessage> STREAM_CODEC = StreamCodec.composite(
			KnownClientPlayerNet.STREAM_CODEC, UpdatePresenceMessage::update,
			UpdatePresenceMessage::new
	);

	public static void handle(UpdatePresenceMessage message, NetworkManager.PacketContext context) {
		context.queue(() -> FTBTeamsClient.updatePresence(message.update));
	}

	@Override
	public Type<UpdatePresenceMessage> type() {
		return TYPE;
	}
}
