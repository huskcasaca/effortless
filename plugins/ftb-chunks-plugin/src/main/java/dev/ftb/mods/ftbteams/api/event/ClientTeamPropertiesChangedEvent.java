package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;

/**
 * @author LatvianModder
 */
public class ClientTeamPropertiesChangedEvent {
	private final Team team;
	private final TeamPropertyCollection old;

	public ClientTeamPropertiesChangedEvent(Team t, TeamPropertyCollection p) {
		team = t;
		old = p;
	}

	public Team getTeam() {
		return team;
	}

	public TeamPropertyCollection getOldProperties() {
		return old;
	}
}