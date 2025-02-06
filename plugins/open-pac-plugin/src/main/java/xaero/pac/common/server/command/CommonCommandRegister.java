package xaero.pac.common.server.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommonCommandRegister {

	public static final String COMMAND_PREFIX = "openpac";

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment) {
		new ConfigGetOrHelpCommand().register(dispatcher, environment);
		new ConfigSetCommand().register(dispatcher, environment);
		new ConfigSubCreateCommand().register(dispatcher, environment);
		new ConfigSubDeleteCommand().register(dispatcher, environment);
		new ConfigSubListCommand().register(dispatcher, environment);
	}

}
