package xaero.pac.common.server.claims.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import xaero.pac.common.server.config.ServerConfig;

public class ClaimsUnforceloadCommand {

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment) {
		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(ClaimsForceloadCommands.createForceloadCommand(Commands.literal("unforceload"), false, false, false));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("unforceload").then(ClaimsForceloadCommands.createForceloadCommand(Commands.argument("block pos", ColumnPosArgument.columnPos()), false, false, false)));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("unforceload").then(ClaimsForceloadCommands.createForceloadCommand(Commands.literal("anyway").requires(source -> source.hasPermission(2)), false, false, true)));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("unforceload").then(Commands.literal("anyway").requires(source -> source.hasPermission(2)).then(ClaimsForceloadCommands.createForceloadCommand(Commands.argument("block pos", ColumnPosArgument.columnPos()), false, false, true))));
		dispatcher.register(command);
	}

}
