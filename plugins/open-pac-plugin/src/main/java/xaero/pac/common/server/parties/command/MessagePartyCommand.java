package xaero.pac.common.server.parties.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.ServerData;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.config.ServerConfig;
import xaero.pac.common.server.parties.party.IPartyManager;
import xaero.pac.common.server.parties.party.IServerParty;

import java.util.UUID;
import java.util.function.Predicate;

public class MessagePartyCommand {

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment, CommandRequirementProvider commandRequirementProvider) {
		Predicate<CommandSourceStack> requirement = commandRequirementProvider.getMemberRequirement((party, mi) -> true);
		Command<CommandSourceStack> action = context -> {
			ServerPlayer player = context.getSource().getPlayerOrException();
			UUID playerId = player.getUUID();
			MinecraftServer server = context.getSource().getServer();
			IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData = ServerData.from(server);
			IPartyManager<IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> partyManager = serverData.getPartyManager();
			IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly> playerParty = partyManager.getPartyByMember(playerId);
			IPartyMember casterInfo = playerParty.getMemberInfo(playerId);

			String inputMessage = StringArgumentType.getString(context, "message");

			Component rankComponent = Component.literal((playerParty.getOwner() == casterInfo ? "OWNER" : casterInfo.getRank().toString()) + " ").withStyle(s -> s.withColor(casterInfo.getRank().getColor()));
			Component nameComponent = Component.literal("<" + player.getGameProfile().getName() + "> ");
			Component contentComponent = Component.literal(inputMessage).withStyle(s -> s.withColor(ChatFormatting.GRAY));
			Component messageComponent = Component.literal("");
			messageComponent.getSiblings().add(rankComponent);
			messageComponent.getSiblings().add(nameComponent);
			messageComponent.getSiblings().add(contentComponent);
			new PartyOnCommandUpdater().update(playerId, serverData, playerParty, serverData.getPlayerConfigs(), mi -> false, messageComponent);
			return 1;
		};

		LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(PartyCommandRegister.COMMAND_PREFIX).requires(c -> ServerConfig.CONFIG.partiesEnabled.get()).then(Commands.literal("chat")
				.requires(requirement)
				.then(Commands.argument("message", StringArgumentType.greedyString())
				.executes(action)));
		dispatcher.register(command);

		LiteralArgumentBuilder<CommandSourceStack> shortCommand = Commands.literal("opm")
				.requires(c -> ServerConfig.CONFIG.partiesEnabled.get() && requirement.test(c))
				.then(Commands.argument("message", StringArgumentType.greedyString())
				.executes(action));
		dispatcher.register(shortCommand);
	}

}
