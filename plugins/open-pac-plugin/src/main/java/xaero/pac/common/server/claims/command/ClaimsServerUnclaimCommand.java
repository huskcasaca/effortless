package xaero.pac.common.server.claims.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import xaero.pac.common.server.config.ServerConfig;

import java.util.function.Predicate;

public class ClaimsServerUnclaimCommand {

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment) {
		Predicate<CommandSourceStack> requirement = ClaimsClaimCommands.getServerClaimCommandRequirement();

		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(ClaimsClaimCommands.createClaimCommand(Commands.literal("unclaim"), false, true, false)));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(Commands.literal("unclaim").then(ClaimsClaimCommands.createClaimCommand(Commands.argument("block pos", ColumnPosArgument.columnPos()), false, true, false))));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(Commands.literal("unclaim").then(ClaimsClaimCommands.createClaimCommand(Commands.literal("anyway").requires(source -> source.hasPermission(2)), false, true, true))));
		dispatcher.register(command);

		command = Commands.literal(ClaimsCommandRegister.COMMAND_PREFIX).requires(context -> ServerConfig.CONFIG.claimsEnabled.get()).then(Commands.literal("server").requires(requirement).then(Commands.literal("unclaim").then(Commands.literal("anyway").requires(source -> source.hasPermission(2)).then(ClaimsClaimCommands.createClaimCommand(Commands.argument("block pos", ColumnPosArgument.columnPos()), false, true, true)))));
		dispatcher.register(command);
	}

}
