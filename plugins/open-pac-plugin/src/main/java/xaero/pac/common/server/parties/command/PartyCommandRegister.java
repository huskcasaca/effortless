package xaero.pac.common.server.parties.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class PartyCommandRegister {

	public static final String COMMAND_PREFIX = "openpac-parties";

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment) {
		CommandRequirementProvider commandRequirementProvider = new CommandRequirementProvider();
		new CreatePartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new LeavePartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new DestroyPartyConfirmCommand().register(dispatcher, environment, commandRequirementProvider);
		new InvitePartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new InviteAcceptPartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new AboutPartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new KickPartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new AllyPartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new UnallyPartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new RankPartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new MessagePartyCommand().register(dispatcher, environment, commandRequirementProvider);
		new TransferPartyCommand().register(dispatcher, environment, commandRequirementProvider);
	}

}
