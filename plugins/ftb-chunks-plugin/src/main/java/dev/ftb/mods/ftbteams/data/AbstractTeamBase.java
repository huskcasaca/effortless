package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Base class for all teams, client and server side
 */
public abstract class AbstractTeamBase implements Team {
	protected final UUID id;
	protected final TeamPropertyCollectionImpl properties;
	protected final Map<UUID, TeamRank> ranks;
	protected CompoundTag extraData;
	protected final List<TeamMessage> messageHistory;
	private boolean valid;

	protected AbstractTeamBase(UUID id) {
		this(id, new TeamPropertyCollectionImpl());
	}

	protected AbstractTeamBase(UUID id, TeamPropertyCollection properties) {
		this.id = id;
		this.properties = properties instanceof TeamPropertyCollectionImpl p ? p : new TeamPropertyCollectionImpl();
		ranks = new HashMap<>();
		extraData = new CompoundTag();
		messageHistory = new LinkedList<>();
		valid = true;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public UUID getTeamId() {
		return id;
	}

	@Override
	public TeamPropertyCollection getProperties() {
		return properties;
	}

	public abstract TeamType getType();

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof AbstractTeam) {
			return id.equals(((AbstractTeam) o).getId());
		}

		return false;
	}

	@Override
	public String toString() {
		return getShortName();
	}

	@Override
	public CompoundTag getExtraData() {
		return extraData;
	}

	@Override
	public <T> T getProperty(TeamProperty<T> property) {
		return properties.get(property);
	}

	@Override
	public <T> void setProperty(TeamProperty<T> property, T value) {
		properties.set(property, value);
		markDirty();
	}

	public String getDisplayName() {
		return getProperty(TeamProperties.DISPLAY_NAME);
	}

	public String getDescription() {
		return getProperty(TeamProperties.DESCRIPTION);
	}

	public int getColor() {
		return getProperty(TeamProperties.COLOR).rgb();
	}

	public boolean isFreeToJoin() {
		return getProperty(TeamProperties.FREE_TO_JOIN);
	}

	public int getMaxMessageHistorySize() {
		return getProperty(TeamProperties.MAX_MSG_HISTORY_SIZE);
	}

	@Override
	public String getShortName() {
		String s = getDisplayName().replaceAll("\\W", "_");
		return (s.length() > 50 ? s.substring(0, 50) : s) + "#" + getId().toString().substring(0, 8);
	}

	@Override
	public Component getName() {
        return Component.literal(getDisplayName()).withStyle(Style.EMPTY
                .withColor(getType().getColor())
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftbteams info " + getShortName()))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("ftbteams.click_show_info")))
        );
	}

	@Override
	public Component getColoredName() {
        return Component.literal(getDisplayName())
                .withStyle(getProperty(TeamProperties.COLOR).toStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftbteams info " + getShortName()))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("ftbteams.click_show_info")))
        );
	}

	@Override
	public void markDirty() {
	}

	@Override
	public TeamRank getRankForPlayer(UUID playerId) {
		TeamRank rank = ranks.get(playerId);
		if (rank != null) {
			return rank;
		}

		return isFreeToJoin() ? TeamRank.INVITED : TeamRank.NONE;

	}

	public boolean isMember(UUID uuid) {
		return getRankForPlayer(uuid).isMemberOrBetter();
	}

	@Override
	public Map<UUID, TeamRank> getPlayersByRank(TeamRank minRank) {
		if (minRank == TeamRank.NONE) {
			return Collections.unmodifiableMap(ranks);
		}

		Map<UUID, TeamRank> map = new HashMap<>();
		ranks.forEach((id, rank) -> {
			if (rank.isAtLeast(minRank)) {
				map.put(id, rank);
			}
		});

		return Collections.unmodifiableMap(map);
	}

	@Override
	public String getTypeTranslationKey() {
		return "ftbteams.team_type." + getType().getSerializedName();
	}

	@Override
	public Set<UUID> getMembers() {
		return getPlayersByRank(TeamRank.MEMBER).keySet();
	}

	@Override
	public Team createParty(String description, @Nullable Color4I color) {
		if (!(this instanceof PlayerTeam playerTeam)) {
			throw new IllegalStateException("team is not a player team: " + getTeamId());
		}

		UUID playerId = playerTeam.getId();
		FTBTeamsAPI.api().getManager().getTeamForPlayerID(playerId).ifPresent(team -> {
			if (team instanceof PartyTeam) {
				throw new IllegalStateException("player " + playerId + " is already in a party team: " + team.getTeamId());
			}
		});

		if (color == null) color = FTBTUtils.randomColor();
		MinecraftServer server = TeamManagerImpl.INSTANCE.getServer();
		ServerPlayer player = server.getPlayerList().getPlayer(playerId);
		return playerTeam.createParty(playerId, player, FTBTUtils.getDefaultPartyName(server, playerId, player), description, color.rgb(), Set.of());
	}

	public boolean isAllyOrBetter(UUID profile) {
		return getRankForPlayer(profile).isAllyOrBetter();
	}

	public boolean isOfficerOrBetter(UUID profile) {
		return getRankForPlayer(profile).isOfficerOrBetter();
	}

	public boolean isInvited(UUID profile) {
		return getRankForPlayer(profile).isInvitedOrBetter();
	}

	public void addMessage(TeamMessage message) {
		addMessages(List.of(message));
	}

	public void addMessages(Collection<TeamMessage> messages) {
		messageHistory.addAll(messages);
		while (messageHistory.size() > getMaxMessageHistorySize()) {
			messageHistory.removeFirst();
		}
		markDirty();
	}

	@Override
	public List<TeamMessage> getMessageHistory() {
		return Collections.unmodifiableList(messageHistory);
	}

	public void addMember(UUID id, TeamRank rank) {
		ranks.put(id, rank);
	}

	public void removeMember(UUID id) {
		ranks.remove(id);
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	public void invalidateTeam() {
		valid = false;
	}
}
