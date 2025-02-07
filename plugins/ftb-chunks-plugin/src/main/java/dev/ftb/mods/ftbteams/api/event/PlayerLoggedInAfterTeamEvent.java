package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.server.level.ServerPlayer;

/**
 * @author LatvianModder
 */
public class PlayerLoggedInAfterTeamEvent extends TeamEvent {
	private final ServerPlayer player;

	public PlayerLoggedInAfterTeamEvent(Team t, ServerPlayer p) {
		super(t);
		player = p;
	}

	public ServerPlayer getPlayer() {
		return player;
	}
}