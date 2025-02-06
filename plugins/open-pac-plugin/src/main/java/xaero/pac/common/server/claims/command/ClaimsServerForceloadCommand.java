package xaero.pac.common.server.claims.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import xaero.pac.common.server.config.ServerConfig;

import java.util.function.Predicate;

public class ClaimsServerForceloadCommand {

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment) {
		Predicate<CommandSourceStack> requirement = ClaimsClaimCommands.getServerClaimCommandRequirement();

		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(ClaimsForceloadCommands.createForceloadCommand(Commands.literal("forceload"), true, true, false)));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(Commands.literal("forceload").then(ClaimsForceloadCommands.createForceloadCommand(Commands.argument("block pos", ColumnPosArgument.columnPos()), true, true, false))));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(Commands.literal("forceload").then(ClaimsForceloadCommands.createForceloadCommand(Commands.literal("anyway").requires(source -> source.hasPermission(2)), true, true, true))));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(Commands.literal("forceload").then(Commands.literal("anyway").requires(source -> source.hasPermission(2)).then(ClaimsForceloadCommands.createForceloadCommand(Commands.argument("block pos", ColumnPosArgument.columnPos()), true, true, true)))));
		dispatcher.register(command);
	}

}
