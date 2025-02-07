package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record TeamMessageImpl(UUID sender, long date, Component text) implements TeamMessage {
	public static final StreamCodec<RegistryFriendlyByteBuf, TeamMessage> STREAM_CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, TeamMessage::sender,
			ByteBufCodecs.VAR_LONG, TeamMessage::date,
			ComponentSerialization.STREAM_CODEC, TeamMessage::text,
			TeamMessageImpl::new
	);

	public static TeamMessage fromNBT(CompoundTag tag, HolderLookup.Provider provider) {
		return new TeamMessageImpl(
				UUID.fromString(tag.getString("from")),
				tag.getLong("date"),
				Component.Serializer.fromJson(tag.getString("text"), provider)
		);
	}

	public static CompoundTag toNBT(TeamMessage msg, HolderLookup.Provider provider) {
		SNBTCompoundTag tag = new SNBTCompoundTag();
		tag.singleLine();
		tag.putString("from", msg.sender().toString());
		tag.putLong("date", msg.date());
		tag.putString("text", Component.Serializer.toJson(msg.text(), provider));
		return tag;
	}
}
