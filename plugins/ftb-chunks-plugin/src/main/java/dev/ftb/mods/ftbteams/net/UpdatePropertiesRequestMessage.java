package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.data.AbstractTeam;
import dev.ftb.mods.ftbteams.data.TeamPropertyCollectionImpl;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record UpdatePropertiesRequestMessage(TeamPropertyCollection properties) implements CustomPacketPayload {
	public static final Type<UpdatePropertiesRequestMessage> TYPE = new Type<>(FTBTeamsAPI.rl("update_propeties_request"));

	public static final StreamCodec<RegistryFriendlyByteBuf, UpdatePropertiesRequestMessage> STREAM_CODEC = StreamCodec.composite(
			TeamPropertyCollectionImpl.STREAM_CODEC, UpdatePropertiesRequestMessage::properties,
			UpdatePropertiesRequestMessage::new
	);

	public static void handle(UpdatePropertiesRequestMessage message, NetworkManager.PacketContext context) {
		context.queue(() -> {
			ServerPlayer player = (ServerPlayer) context.getPlayer();

			FTBTeamsAPI.api().getManager().getTeamForPlayer(player).ifPresent(team -> {
				if (team instanceof AbstractTeam abstractTeam && abstractTeam.isOfficerOrBetter(player.getUUID())) {
					abstractTeam.updatePropertiesFrom(message.properties);
					NetworkManager.sendToPlayers(player.server.getPlayerList().getPlayers(),
							new UpdatePropertiesResponseMessage(team.getId(), abstractTeam.getProperties()));
				}
			});
		});
	}

	@Override
	public Type<UpdatePropertiesRequestMessage> type() {
		return TYPE;
	}
}
