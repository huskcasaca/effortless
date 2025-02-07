package dev.ftb.mods.ftbteams.data;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftblibrary.util.NetworkHelper;
import dev.ftb.mods.ftblibrary.util.TextComponentUtils;
import dev.ftb.mods.ftbteams.FTBTeams;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.event.*;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.net.SendMessageResponseMessage;
import dev.ftb.mods.ftbteams.net.UpdatePropertiesResponseMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.*;

/**
 * Base class for server-side teams
 */
public abstract class AbstractTeam extends AbstractTeamBase {
	protected final TeamManagerImpl manager;
	private boolean shouldSave;

	public AbstractTeam(TeamManagerImpl manager, UUID id) {
		super(id);
		this.manager = manager;

		properties.collectProperties();
	}

	@Override
	public void markDirty() {
		shouldSave = true;
		manager.nameMap = null;
	}

	public List<ServerPlayer> getOnlineRanked(TeamRank rank) {
		List<ServerPlayer> list = new ArrayList<>();

		for (UUID id : getPlayersByRank(rank).keySet()) {
			ServerPlayer player = FTBTUtils.getPlayerByUUID(manager.getServer(), id);
			if (player != null) {
				list.add(player);
			}
		}

		return list;
	}

	@Override
	public List<ServerPlayer> getOnlineMembers() {
		return getOnlineRanked(TeamRank.MEMBER);
	}

	void onCreated(@Nullable ServerPlayer player, @NotNull UUID playerId) {
		if (player != null) {
			TeamEvent.CREATED.invoker().accept(new TeamCreatedEvent(this, player, playerId));
		}
		markDirty();
		manager.markDirty();
		manager.saveNow();
	}

	void updateCommands(ServerPlayer player) {
		player.getServer().getPlayerList().sendPlayerPermissionLevel(player);
	}

	void onPlayerChangeTeam(@Nullable Team prev, UUID player, @Nullable ServerPlayer p, boolean deleted) {
		TeamEvent.PLAYER_CHANGED.invoker().accept(new PlayerChangedTeamEvent(this, prev, player, p));

		if (prev instanceof PartyTeam && this instanceof PlayerTeam) {
			TeamEvent.PLAYER_LEFT_PARTY.invoker().accept(new PlayerLeftPartyTeamEvent(prev, this, player, p, deleted));
		} else if (prev instanceof PlayerTeam && p != null) {
			TeamEvent.PLAYER_JOINED_PARTY.invoker().accept(new PlayerJoinedPartyTeamEvent(this, prev, p));
		}

		if (deleted && prev != null) {
			TeamEvent.DELETED.invoker().accept(new TeamEvent(prev));
		}

		if (p != null) {
			updateCommands(p);
		}
	}

	// Data IO //

	public SNBTCompoundTag serializeNBT(HolderLookup.Provider provider) {
		SNBTCompoundTag tag = new SNBTCompoundTag();
		tag.putString("id", getId().toString());
		tag.putString("type", getType().getSerializedName());
		serializeExtraNBT(tag);

		SNBTCompoundTag ranksNBT = new SNBTCompoundTag();

		for (Map.Entry<UUID, TeamRank> entry : ranks.entrySet()) {
			ranksNBT.putString(entry.getKey().toString(), entry.getValue().getSerializedName());
		}

		tag.put("ranks", ranksNBT);
		tag.put("properties", properties.write(new SNBTCompoundTag()));

		ListTag messageHistoryTag = new ListTag();
		for (TeamMessage msg : getMessageHistory()) {
			messageHistoryTag.add(TeamMessageImpl.toNBT(msg, provider));
		}
		tag.put("message_history", messageHistoryTag);

		TeamEvent.SAVED.invoker().accept(new TeamEvent(this));
		tag.put("extra", extraData);

		return tag;
	}

	protected void serializeExtraNBT(CompoundTag tag) {
	}

	public void deserializeNBT(CompoundTag tag, HolderLookup.Provider provider) {
		ranks.clear();
		CompoundTag ranksNBT = tag.getCompound("ranks");

		for (String s : ranksNBT.getAllKeys()) {
			ranks.put(UUID.fromString(s), TeamRank.NAME_MAP.get(ranksNBT.getString(s)));
		}

		properties.read(tag.getCompound("properties"));
		extraData = tag.getCompound("extra");
		messageHistory.clear();

		ListTag messageHistoryTag = tag.getList("message_history", Tag.TAG_COMPOUND);
		for (int i = 0; i < messageHistoryTag.size(); i++) {
			addMessage(TeamMessageImpl.fromNBT(messageHistoryTag.getCompound(i), provider));
		}

		TeamEvent.LOADED.invoker().accept(new TeamEvent(this));
	}

	public <T> int settings(CommandSourceStack source, TeamProperty<T> key, String value) {
		MutableComponent keyc = Component.translatable(key.getTranslationKey("ftbteamsconfig")).withStyle(ChatFormatting.YELLOW);
		if (value.isEmpty()) {
			Component valuec = Component.literal(key.toString(getProperty(key))).withStyle(ChatFormatting.AQUA);
			source.sendSuccess(() -> keyc.append(" is set to ").append(valuec), true);
		} else {
			Optional<T> optional = key.fromString(value);

			if (optional.isPresent()) {
				TeamPropertyCollection old = properties.copy();
				setProperty(key, optional.get());
				Component valuec = Component.literal(value).withStyle(ChatFormatting.AQUA);
				source.sendSuccess(() -> Component.literal("Set ").append(keyc).append(" to ").append(valuec), true);

				TeamEvent.PROPERTIES_CHANGED.invoker().accept(new TeamPropertiesChangedEvent(this, old));
				if (ClientTeam.isSyncableProperty(key)) {
					NetworkHelper.sendToAll(source.getServer(), UpdatePropertiesResponseMessage.oneProperty(getId(), key, optional.get()));
				}
			} else {
				source.sendFailure(Component.literal("Failed to parse value!"));
				return 0;
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	public int declineInvitation(CommandSourceStack source) throws CommandSyntaxException {
		ServerPlayer player = source.getPlayerOrException();

		if (getRankForPlayer(player.getUUID()) == TeamRank.INVITED) {
			ranks.put(player.getUUID(), TeamRank.ALLY);
			source.sendSuccess(() -> Component.translatable("ftbteams.message.declined"), true);
			markDirty();
			manager.syncToAll(this);
			return Command.SINGLE_SUCCESS;
		} else {
			FTBTeams.LOGGER.warn("ignore invitation decline for player {} to team {} (not invited)", player.getUUID(), getId());
			return 0;
		}
	}

	@Override
	public List<Component> getTeamInfo() {
		List<Component> res = new ArrayList<>();

		res.add(Component.literal("== ")
				.append(getName())
				.append(Component.literal(" [" + getType().getSerializedName() + "]").withStyle(getType().getColor()))
				.append(" ==")
		);
		res.add(Component.translatable("ftbteams.info.id", FTBTUtils.makeCopyableComponent(getId().toString()).withStyle(ChatFormatting.YELLOW)));
		res.add(Component.translatable("ftbteams.info.short_id", FTBTUtils.makeCopyableComponent(getShortName()).withStyle(ChatFormatting.YELLOW)));

		if (isPartyTeam()) {
			res.add(getOwner().equals(Util.NIL_UUID) ?
					Component.translatable("ftbteams.info.owner", Component.translatable("ftbteams.info.owner.none").withStyle(ChatFormatting.GRAY)) :
					Component.translatable("ftbteams.info.owner", playerWithId(getOwner()))
			);

			res.add(Component.translatable("ftbteams.info.members"));
			if (getMembers().isEmpty()) {
				res.add(Component.literal("- ").append(Component.translatable("ftbteams.info.members.none")).withStyle(ChatFormatting.GRAY));
			} else {
				for (UUID member : getMembers()) {
					res.add(Component.literal("- ").append(playerWithId(member)));
				}
			}
		}

		return res;
	}

	private Component playerWithId(UUID member) {
		return manager.getPlayerName(member).copy().withStyle(Style.EMPTY
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(member.toString())))
		);
	}

	@Override
	public UUID getOwner() {
		return Util.NIL_UUID;
	}

	@Override
	public void sendMessage(UUID senderId, String message) {
		sendMessage(senderId, TextComponentUtils.withLinks(message));
	}

	void sendMessage(UUID from, Component text) {
		addMessage(FTBTeamsAPI.api().createMessage(from, text));

		MutableComponent component = Component.literal("<");
		component.append(manager.getPlayerName(from));
		component.append(" @");
		component.append(getName());
		component.append("> ");
		component.append(text);

		for (ServerPlayer p : getOnlineMembers()) {
			p.displayClientMessage(component, false);
			NetworkManager.sendToPlayer(p, new SendMessageResponseMessage(from, text));
		}

		markDirty();
	}

	public void updatePropertiesFrom(TeamPropertyCollection newProperties) {
		TeamPropertyCollection oldProperties = properties.copy();
		properties.updateFrom(newProperties);
		TeamEvent.PROPERTIES_CHANGED.invoker().accept(new TeamPropertiesChangedEvent(this, oldProperties));
		markDirty();
	}

	void saveIfNeeded(Path directory, HolderLookup.Provider provider) {
		if (shouldSave) {
			SNBT.write(directory.resolve(getType().getSerializedName() + "/" + getId() + ".snbt"), serializeNBT(provider));
			shouldSave = false;
		}
	}
}
