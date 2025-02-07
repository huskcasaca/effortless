package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

/**
 * @author LatvianModder
 */
public class TeamInfoEvent extends TeamEvent {
	private final CommandSourceStack source;

	public TeamInfoEvent(Team t, CommandSourceStack p) {
		super(t);
		source = p;
	}

	public CommandSourceStack getSource() {
		return source;
	}

	public void add(Component component) {
		source.sendSuccess(() -> component, false);
	}
}