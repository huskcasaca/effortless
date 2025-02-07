package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Fired server-side when a new team is created; could be a player, party, or server team.
 */
public class TeamCreatedEvent extends TeamEvent {
	private final ServerPlayer creator;
	private final UUID creatorId;

	public TeamCreatedEvent(Team team, @Nullable ServerPlayer creator, @NotNull UUID creatorId) {
		super(team);
		this.creator = creator;
		this.creatorId = creatorId;
	}

	/**
	 * Get the player responsible for creation of the team.
	 *
	 * @return the creating player
	 */
	public ServerPlayer getCreator() {
		return creator;
	}

	/**
	 * Get the UUID of the player responsible for creation of the team.
	 *
	 * @return the creator's UUID
	 */
	public UUID getCreatorId() {
		return creatorId;
	}
}