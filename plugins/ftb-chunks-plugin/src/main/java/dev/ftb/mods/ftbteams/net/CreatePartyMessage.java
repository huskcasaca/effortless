package dev.ftb.mods.ftbteams.net;

import com.mojang.authlib.GameProfile;
import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftbteams.FTBTeamsAPIImpl;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.data.PlayerTeam;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Set;

public record CreatePartyMessage(String name, String description, int color, Set<GameProfile> invited) implements CustomPacketPayload {
	public static final Type<CreatePartyMessage> TYPE = new Type<>(FTBTeamsAPI.rl("create_party"));

	public static final StreamCodec<FriendlyByteBuf, CreatePartyMessage> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8, CreatePartyMessage::name,
			ByteBufCodecs.STRING_UTF8, CreatePartyMessage::description,
			ByteBufCodecs.INT, CreatePartyMessage::color,
			ByteBufCodecs.GAME_PROFILE.apply(ByteBufCodecs.list()).map(Set::copyOf, List::copyOf), CreatePartyMessage::invited,
			CreatePartyMessage::new
	);

	public static void handle(CreatePartyMessage message, NetworkManager.PacketContext context) {
		context.queue(() -> {
			ServerPlayer player = (ServerPlayer) context.getPlayer();
			FTBTeamsAPI.api().getManager().getTeamForPlayer(player).ifPresent(team -> {
				if (FTBTeamsAPIImpl.INSTANCE.isPartyCreationFromAPIOnly()) {
					player.displayClientMessage(Component.translatable("ftbteams.party_api_only").withStyle(ChatFormatting.RED), false);
				} else if (team instanceof PlayerTeam playerTeam) {
					playerTeam.createParty(player.getUUID(), player, message.name, message.description, message.color, message.invited);
				}
			});
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
