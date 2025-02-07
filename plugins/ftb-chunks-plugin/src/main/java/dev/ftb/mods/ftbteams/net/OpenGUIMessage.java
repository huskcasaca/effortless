package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.data.PlayerPermissions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record OpenGUIMessage() implements CustomPacketPayload {
	public static final Type<OpenGUIMessage> TYPE = new Type<>(FTBTeamsAPI.rl("open_gui"));

	private static final OpenGUIMessage INSTANCE = new OpenGUIMessage();

	public static final StreamCodec<FriendlyByteBuf, OpenGUIMessage> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	public static void handle(@SuppressWarnings("unused") OpenGUIMessage message, NetworkManager.PacketContext context) {
		context.queue(() -> {
			ServerPlayer player = (ServerPlayer) context.getPlayer();
			FTBTeamsAPI.api().getManager().getTeamForPlayer(player)
					.ifPresent(team -> NetworkManager.sendToPlayer(player, new OpenMyTeamGUIMessage(team.getProperties(), PlayerPermissions.forPlayer(player))));
		});
	}

	public static void sendToServer() {
		NetworkManager.sendToServer(INSTANCE);
	}

	@Override
	public Type<OpenGUIMessage> type() {
		return TYPE;
	}
}
