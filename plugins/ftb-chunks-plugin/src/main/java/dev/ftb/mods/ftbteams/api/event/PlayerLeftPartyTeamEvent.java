package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class PlayerLeftPartyTeamEvent extends TeamEvent {
	private final Team playerTeam;
	private final UUID playerId;
	private final ServerPlayer player;
	private final boolean teamDeleted;

	public PlayerLeftPartyTeamEvent(Team team, Team playerTeam, UUID playerId, @Nullable ServerPlayer player, boolean teamDeleted) {
		super(team);
		this.playerTeam = playerTeam;
		this.playerId = playerId;
		this.player = player;
		this.teamDeleted = teamDeleted;
	}

	/**
	 * Get the player's new team, which will be their own player team, i.e. {@link Team#isPlayerTeam()} will always return true.
	 *
	 * @return the player's new team
	 */
	public Team getPlayerTeam() {
		return playerTeam;
	}

	/**
	 * Get the ID of the player who just left.
	 *
	 * @return the player's UUID
	 */
	public UUID getPlayerId() {
		return playerId;
	}

	/**
	 * Get the player who just left. This may be null if the player is not currently online.
	 *
	 * @return the player, may be null
	 */
	@Nullable
	public ServerPlayer getPlayer() {
		return player;
	}

	/**
	 * Has the team been deleted too?  This will be true if this player is the last player to leave the party team,
	 * i.e. the party owner is disbanding the team.
	 *
	 * @return true if the team is being deleted, false otherwise
	 */
	public boolean getTeamDeleted() {
		return teamDeleted;
	}
}