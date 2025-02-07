package dev.ftb.mods.ftbteams.data;

import com.mojang.brigadier.Command;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class ServerTeam extends AbstractTeam {
	public ServerTeam(TeamManagerImpl manager, UUID id) {
		super(manager, id);
	}

	@Override
	public TeamType getType() {
		return TeamType.SERVER;
	}

	@Override
	public boolean isServerTeam() {
		return true;
	}

	public int delete(CommandSourceStack source) {
		markDirty();

		invalidateTeam();

		manager.deleteTeam(this);
		manager.saveNow();
		manager.tryDeleteTeamFile(getId() + ".snbt", "server");
		manager.syncToAll(this);

		source.sendSuccess(() -> Component.translatable("ftbteams.message.deleted_server_team", getShortName()), true);

		TeamEvent.DELETED.invoker().accept(new TeamEvent(this));

		return Command.SINGLE_SUCCESS;
	}
}
