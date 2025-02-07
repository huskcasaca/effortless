package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class PlayerChangedTeamEvent extends TeamEvent {
	private final Team previousTeam;
	private final UUID playerId;
	private final ServerPlayer player;

	public PlayerChangedTeamEvent(Team newTeam, @Nullable Team previousTeam, UUID playerId, @Nullable ServerPlayer player) {
		super(newTeam);
		this.previousTeam = previousTeam;
		this.playerId = playerId;
		this.player = player;
	}

	public Optional<Team> getPreviousTeam() {
		return Optional.ofNullable(previousTeam);
	}

	public UUID getPlayerId() {
		return playerId;
	}

	@Nullable
	public ServerPlayer getPlayer() {
		return player;
	}
}