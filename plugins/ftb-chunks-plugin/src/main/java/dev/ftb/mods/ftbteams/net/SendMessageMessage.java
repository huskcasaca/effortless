package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record SendMessageMessage(String msg) implements CustomPacketPayload {
	public static final Type<SendMessageMessage> TYPE = new Type<>(FTBTeamsAPI.rl("send_message"));

	public static final StreamCodec<FriendlyByteBuf, SendMessageMessage> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, SendMessageMessage::msg,
			SendMessageMessage::new
	);

	public static void handle(SendMessageMessage message, NetworkManager.PacketContext context) {
		context.queue(() -> {
			ServerPlayer player = (ServerPlayer) context.getPlayer();
			FTBTeamsAPI.api().getManager().getTeamForPlayer(player)
					.ifPresent(team -> team.sendMessage(player.getUUID(), message.msg));
		});
	}

	@Override
	public Type<SendMessageMessage> type() {
		return TYPE;
	}
}
