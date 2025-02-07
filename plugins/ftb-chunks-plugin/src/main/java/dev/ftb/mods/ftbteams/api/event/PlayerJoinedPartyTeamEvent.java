package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * @author LatvianModder
 */
public class PlayerJoinedPartyTeamEvent extends TeamEvent {
	private final Team previousTeam;
	private final ServerPlayer player;

	public PlayerJoinedPartyTeamEvent(Team team, Team previousTeam, ServerPlayer player) {
		super(team);
		this.previousTeam = previousTeam;
		this.player = player;
	}

	/**
	 * Get the player's previous team, which will be always be their player team.
	 *
	 * @return the player's previous team
	 */
	public Team getPreviousTeam() {
		return previousTeam;
	}

	/**
	 * Get the player who just joined. This will be always be non-null, since players need to be online to accept an
	 * invitation to a team.
	 *
	 * @return the player
	 */
	@NotNull
	public ServerPlayer getPlayer() {
		return player;
	}
}