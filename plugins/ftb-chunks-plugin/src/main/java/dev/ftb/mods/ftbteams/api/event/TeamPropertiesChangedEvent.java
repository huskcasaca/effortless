package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;

/**
 * @author LatvianModder
 */
public class TeamPropertiesChangedEvent extends TeamEvent {
	private final TeamPropertyCollection prevProps;

	public TeamPropertiesChangedEvent(Team team, TeamPropertyCollection prevProps) {
		super(team);

		this.prevProps = prevProps;
	}

	/**
	 * Get the previous properties for the team.
	 *
	 * @apiNote the new properties can be retrieved simply by calling {@code event.getTeam().getProperties()}
	 *
	 * @return the previous properties
	 */
	public TeamPropertyCollection getPreviousProperties() {
		return prevProps;
	}
}