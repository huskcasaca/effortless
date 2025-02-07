package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

/**
 * @author LatvianModder
 */
public class PlayerTransferredTeamOwnershipEvent extends TeamEvent {
	private final ServerPlayer from, to;
	private final GameProfile toProfile;

	public PlayerTransferredTeamOwnershipEvent(Team team, ServerPlayer prevOwner, ServerPlayer newOwner) {
		super(team);
		from = prevOwner;
		to = newOwner;
		toProfile = null;
	}

	public PlayerTransferredTeamOwnershipEvent(PartyTeam t, ServerPlayer from, GameProfile toProfile) {
		super(t);
		this.from = from;
		this.to = null;
		this.toProfile = toProfile;
	}

	@Nullable
	public ServerPlayer getFrom() {
		return from;
	}

	@Nullable
	public ServerPlayer getTo() {
		return to;
	}

	public GameProfile getToProfile() {
		return to == null ? toProfile : to.getGameProfile();
	}
}