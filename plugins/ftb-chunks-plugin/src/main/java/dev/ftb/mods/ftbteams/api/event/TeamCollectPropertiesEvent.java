package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.property.TeamProperty;

import java.util.function.Consumer;

/**
 * Fired server-side whenever a team is created. You can listen for this event in your mod to attach any additional
 * properties you wish to add to the team.
 */
public class TeamCollectPropertiesEvent {
	private final Consumer<TeamProperty<?>> callback;

	public TeamCollectPropertiesEvent(Consumer<TeamProperty<?>> c) {
		callback = c;
	}

	/**
	 * Add the given property to the team. It will appear for display and editing in the team properties GUI when the
	 * "Settings" button in the main team is clicked.
	 *
	 * @param property the property to add
	 */
	public void add(TeamProperty<?> property) {
		callback.accept(property);
	}
}