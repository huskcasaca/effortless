package dev.ftb.mods.ftbteams.data;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.event.PlayerTransferredTeamOwnershipEvent;
import dev.ftb.mods.ftbteams.api.event.TeamAllyEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class PartyTeam extends AbstractTeam {
	UUID owner;

	public PartyTeam(TeamManagerImpl manager, UUID id) {
		super(manager, id);
		owner = Util.NIL_UUID;
	}

	@Override
	public TeamType getType() {
		return TeamType.PARTY;
	}

	@Override
	protected void serializeExtraNBT(CompoundTag tag) {
		tag.putString("owner", owner.toString());
	}

	@Override
	public void deserializeNBT(CompoundTag tag, HolderLookup.Provider provider) {
		super.deserializeNBT(tag, provider);
		owner = UUID.fromString(tag.getString("owner"));
	}

	@Override
	public TeamRank getRankForPlayer(UUID playerId) {
		return owner.equals(playerId) ? TeamRank.OWNER : super.getRankForPlayer(playerId);
	}

	public boolean isOwner(UUID profile) {
		return owner.equals(profile);
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public boolean isPartyTeam() {
		return true;
	}

	public int join(ServerPlayer player) throws CommandSyntaxException {
		Team oldTeam = manager.getTeamForPlayer(player)
				.orElseThrow(() -> TeamArgument.TEAM_NOT_FOUND.create(player.getUUID()));

		if (!(oldTeam instanceof PlayerTeam playerTeam)) {
			throw TeamArgument.ALREADY_IN_PARTY.create();
		}

		UUID id = player.getUUID();

		playerTeam.setEffectiveTeam(this);
		ranks.put(id, TeamRank.MEMBER);
		sendMessage(Util.NIL_UUID, Component.translatable("ftbteams.message.joined", player.getName()).withStyle(ChatFormatting.GREEN));
		markDirty();

		playerTeam.ranks.remove(id);
		playerTeam.markDirty();
		playerTeam.updatePresence();
		manager.syncToAll(this, oldTeam);
		onPlayerChangeTeam(oldTeam, id, player, false);

		return Command.SINGLE_SUCCESS;
	}

	public int invite(ServerPlayer inviter, Collection<GameProfile> profiles) throws CommandSyntaxException {
		if (!FTBTUtils.canPlayerUseCommand(inviter, "ftbteams.party.invite")) {
			throw TeamArgument.NO_PERMISSION.create();
		}

		for (GameProfile profile : profiles) {
			FTBTeamsAPI.api().getManager().getTeamForPlayerID(profile.getId()).ifPresent(team -> {
				if (!(team instanceof PartyTeam)) {
					ranks.put(profile.getId(), TeamRank.INVITED);
					markDirty();

					sendMessage(inviter.getUUID(), Component.translatable("ftbteams.message.invited",
							Component.literal(profile.getName()).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN));

					ServerPlayer invitee = FTBTUtils.getPlayerByUUID(manager.getServer(), profile.getId());

					if (invitee != null && !invitee.getUUID().equals(inviter.getUUID())) {
						invitee.displayClientMessage(Component.translatable("ftbteams.message.invite_sent",
								inviter.getName().copy().withStyle(ChatFormatting.YELLOW)), false);

						Component acceptButton = makeInviteButton("ftbteams.accept", ChatFormatting.GREEN,
								"/ftbteams party join " + getShortName());
						Component declineButton = makeInviteButton("ftbteams.decline", ChatFormatting.RED,
								"/ftbteams party decline " + getShortName());
						invitee.displayClientMessage(Component.literal("[")
								.append(acceptButton).append("] [")
								.append(declineButton).append("]"),
								false);
					}
				}
			});
		}

		return Command.SINGLE_SUCCESS;
	}

	private Component makeInviteButton(String xlate, ChatFormatting color, String command) {
		return Component.translatable(xlate)
				.withStyle(Style.EMPTY.withColor(color).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
	}

	public int kick(CommandSourceStack from, Collection<GameProfile> players) throws CommandSyntaxException {
		for (GameProfile player : players) {
			UUID id = player.getId();
			Team oldTeam = manager.getTeamForPlayerID(id).orElseThrow(TeamArgument.NOT_IN_PARTY::create);

			if (oldTeam != this) {
				throw TeamArgument.NOT_IN_PARTY.create();
			} else if (isOwner(id)) {
				throw TeamArgument.CANT_KICK_OWNER.create();
			}

			PlayerTeam team = manager.getPersonalTeamForPlayerID(id);
			team.setEffectiveTeam(team);

			ServerPlayer playerToKick = FTBTUtils.getPlayerByUUID(manager.getServer(), id);

			team.ranks.put(id, TeamRank.OWNER);
			UUID fromId = from.getPlayer() != null ? from.getPlayer().getUUID() : Util.NIL_UUID;
			sendMessage(fromId, Component.translatable("ftbteams.message.kicked", manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW), getName()).withStyle(ChatFormatting.GOLD));
			team.markDirty();

			ranks.remove(id);
			markDirty();

			team.updatePresence();
			manager.syncToAll(this, team);

			if (playerToKick != null) {
				playerToKick.displayClientMessage(Component.translatable("ftbteams.message.kicked", playerToKick.getName().copy().withStyle(ChatFormatting.YELLOW), getName().copy().withStyle(ChatFormatting.AQUA)), false);
				updateCommands(playerToKick);
			}

			team.onPlayerChangeTeam(this, id, playerToKick, false);
		}

		return Command.SINGLE_SUCCESS;
	}

	public int promote(ServerPlayer from, Collection<GameProfile> players) throws CommandSyntaxException {
		boolean changesMade = false;
		for (GameProfile player : players) {
			UUID id = player.getId();
			if (getRankForPlayer(id) == TeamRank.MEMBER) {
				ranks.put(id, TeamRank.OFFICER);
				Component playerName = manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW);
				sendMessage(from.getUUID(), Component.translatable("ftbteams.message.promoted", playerName).withStyle(ChatFormatting.GREEN));
				changesMade = true;
			} else {
				throw TeamArgument.NOT_MEMBER.create(manager.getPlayerName(id), getName());
			}
		}
		if (changesMade) {
			markDirty();
			manager.syncToAll(this);
		}

		return Command.SINGLE_SUCCESS;
	}

	public int demote(ServerPlayer from, Collection<GameProfile> players) throws CommandSyntaxException {
		boolean changesMade = false;
		for (GameProfile player : players) {
			UUID id = player.getId();
			if (getRankForPlayer(id) == TeamRank.OFFICER) {
				ranks.put(id, TeamRank.MEMBER);
				Component playerName = manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW);
				sendMessage(from.getUUID(), Component.translatable("ftbteams.message.demoted", playerName).withStyle(ChatFormatting.GOLD));
				changesMade = true;
			} else {
				throw TeamArgument.NOT_OFFICER.create(manager.getPlayerName(id), getName());
			}
		}
		if (changesMade) {
			markDirty();
			manager.syncToAll(this);
		}

		return Command.SINGLE_SUCCESS;
	}

	public int transferOwnership(CommandSourceStack from, Collection<GameProfile> toProfiles) throws CommandSyntaxException {
		return transferOwnership(from, toProfiles.stream().findFirst().orElseThrow());
	}

	public int transferOwnership(CommandSourceStack from, GameProfile toProfile) throws CommandSyntaxException {
		// new owner must be in this party
		UUID newOwnerID = toProfile.getId();
		if (!getMembers().contains(newOwnerID)) {
			throw TeamArgument.NOT_MEMBER.create(toProfile.toString(), getName());
		}

		if (owner.equals(newOwnerID)) {
			from.sendSystemMessage(Component.literal("Already owner!").withStyle(ChatFormatting.RED));
			return 0;
		}

		ranks.put(owner, TeamRank.OFFICER);
		owner = newOwnerID;
		ranks.put(owner, TeamRank.OWNER);

		markDirty();

		ServerPlayer fromPlayer = from.getPlayer();  // null if command run from console
		if (fromPlayer != null) {
			updateCommands(fromPlayer);
		}

		ServerPlayer toPlayer = from.getServer().getPlayerList().getPlayer(newOwnerID);
		if (toPlayer != null) {
			TeamEvent.OWNERSHIP_TRANSFERRED.invoker().accept(new PlayerTransferredTeamOwnershipEvent(this, fromPlayer, toPlayer));
			updateCommands(toPlayer);
		} else {
			TeamEvent.OWNERSHIP_TRANSFERRED.invoker().accept(new PlayerTransferredTeamOwnershipEvent(this, fromPlayer, toProfile));
		}

		UUID fromId = fromPlayer == null ? Util.NIL_UUID : fromPlayer.getUUID();
		Component msg = Component.translatable("ftbteams.message.transfer_owner", Component.literal(toProfile.getName()).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN);
		sendMessage(fromId, msg);
		if (fromPlayer == null) {
			from.sendSystemMessage(msg);
		}

		manager.syncToAll(this);

		return Command.SINGLE_SUCCESS;
	}

	public int leave(UUID id) throws CommandSyntaxException {
		ServerPlayer player = FTBTeamsAPI.api().getManager().getServer().getPlayerList().getPlayer(id);

		if (isOwner(id) && getMembers().size() > 1) {
			throw TeamArgument.OWNER_CANT_LEAVE.create();
		}

		// mark the player as being back in their personal team
		PlayerTeam playerTeam = manager.getPersonalTeamForPlayerID(id);
		playerTeam.setEffectiveTeam(playerTeam);
		playerTeam.ranks.put(id, TeamRank.OWNER);
		String playerName = player == null ? id.toString() : player.getGameProfile().getName();
		sendMessage(Util.NIL_UUID, Component.translatable("ftbteams.message.left_party", Component.literal(playerName).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GOLD));
		playerTeam.markDirty();

		// remove the player from this party team
		ranks.remove(id);
		manager.markDirty();

		// party team empty? delete it!
		boolean deletingTeam = false;
		if (getMembers().isEmpty()) {
			deletingTeam = true;
			invalidateTeam();
			manager.deleteTeam(this);
			manager.saveNow();
			manager.tryDeleteTeamFile(getId() + ".snbt", "party");
		}

		playerTeam.updatePresence();
		playerTeam.onPlayerChangeTeam(this, id, player, deletingTeam);
		manager.syncToAll(this, playerTeam);

		return Command.SINGLE_SUCCESS;
	}

	public int addAlly(CommandSourceStack source, Collection<GameProfile> players) throws CommandSyntaxException {
		if (source.getPlayer() != null && !FTBTUtils.canPlayerUseCommand(source.getPlayer(), "ftbteams.party.allies.add")) {
			throw TeamArgument.NO_PERMISSION.create();
		}

		UUID from = source.getEntity() == null ? Util.NIL_UUID : source.getEntity().getUUID();

		List<GameProfile> addedPlayers = new ArrayList<>();
		for (GameProfile player : players) {
			UUID id = player.getId();

			if (!isAllyOrBetter(id)) {
				ranks.put(id, TeamRank.ALLY);
				sendMessage(from, Component.translatable("ftbteams.message.add_ally",
						manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GREEN));
				addedPlayers.add(player);
				ServerPlayer invitedPlayer = manager.getServer().getPlayerList().getPlayer(id);
				if (invitedPlayer != null) {
					invitedPlayer.displayClientMessage(Component.translatable("ftbteams.message.now_allied", getDisplayName()).withStyle(ChatFormatting.GREEN), false);
				}
			}
		}

		if (!addedPlayers.isEmpty()) {
			markDirty();
			manager.syncToAll(this);
			TeamEvent.ADD_ALLY.invoker().accept(new TeamAllyEvent(this, addedPlayers, true));
			return Command.SINGLE_SUCCESS;
		}

		return 0;
	}

	public int removeAlly(CommandSourceStack source, Collection<GameProfile> players) {
		UUID from = source.getEntity() == null ? Util.NIL_UUID : source.getEntity().getUUID();
		List<GameProfile> removedPlayers = new ArrayList<>();

		for (GameProfile player : players) {
			UUID id = player.getId();

			if (isAllyOrBetter(id) && !isMember(id)) {
				ranks.remove(id);
				sendMessage(from, Component.translatable("ftbteams.message.remove_ally",
						manager.getPlayerName(id).copy().withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GOLD));
				removedPlayers.add(player);
				ServerPlayer removedPlayer = manager.getServer().getPlayerList().getPlayer(id);
				if (removedPlayer != null) {
					removedPlayer.displayClientMessage(Component.translatable("ftbteams.message.no_longer_allied", getDisplayName()).withStyle(ChatFormatting.GOLD), false);
				}
			}
		}

		if (!removedPlayers.isEmpty()) {
			markDirty();
			manager.syncToAll(this);
			TeamEvent.REMOVE_ALLY.invoker().accept(new TeamAllyEvent(this, removedPlayers, false));
			return Command.SINGLE_SUCCESS;
		}

		return 0;
	}

	public int listAllies(CommandSourceStack source) {
		source.sendSuccess(() -> Component.literal("Allies:"), false);
		boolean any = false;

		for (Map.Entry<UUID, TeamRank> entry : getPlayersByRank(TeamRank.ALLY).entrySet()) {
			if (!entry.getValue().isAtLeast(TeamRank.MEMBER)) {
				source.sendSuccess(() -> manager.getPlayerName(entry.getKey()), false);
				any = true;
			}
		}

		if (!any) {
			source.sendSuccess(() -> Component.literal("None"), false);
		}

		return Command.SINGLE_SUCCESS;
	}

	public int forceDisband(CommandSourceStack from) throws CommandSyntaxException {
		// kick all non-owner members
		List<GameProfile> members = getMembers().stream()
				.filter(id -> !id.equals(owner))
				.map(id -> new GameProfile(id, ""))
				.toList();
		kick(from, members);
		
		// now make the owner leave too
		leave(owner);

		from.sendSuccess(() -> Component.translatable("ftbteams.message.team_disbanded", getName(), getId().toString())
				.withStyle(ChatFormatting.GOLD), false);

		return Command.SINGLE_SUCCESS;
	}
}
