package xaero.pac.common.server.parties.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
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
import xaero.pac.common.server.command.CommandRequirementHelper;
import xaero.pac.common.server.parties.party.IPartyManager;
import xaero.pac.common.server.parties.party.IServerParty;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class CommandRequirementProvider {

	public Predicate<CommandSourceStack> getMemberRequirement(BiFunction<IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>, IPartyMember, Boolean> casterMemberInfoRequirement) {
		return CommandRequirementHelper.onServerThread(c -> {
			try {
				ServerPlayer player = c.getPlayerOrException();
				IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData = ServerData.from(c.getServer());
				IPartyManager<IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> partyManager = serverData.getPartyManager();
				IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly> playerParty = partyManager.getPartyByMember(player.getUUID());
				if(playerParty == null)
					return false;
				IPartyMember memberInfo = playerParty.getMemberInfo(player.getUUID());
				return memberInfo == playerParty.getOwner() || casterMemberInfoRequirement.apply(playerParty, memberInfo);
			} catch(CommandSyntaxException e) {
				return false;
			}
		});
	}

	public Predicate<CommandSourceStack> getNonMemberRequirement(Predicate<ServerPlayer> playerRequirement){
		return CommandRequirementHelper.onServerThread(c -> {
			try {
				ServerPlayer player = c.getPlayerOrException();
				IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData = ServerData.from(c.getServer());
				IPartyManager<IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> partyManager = serverData.getPartyManager();
				IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly> playerParty = partyManager.getPartyByMember(player.getUUID());
				return playerParty == null && playerRequirement.test(player);
			} catch(CommandSyntaxException e) {
				return false;
			}
		});
	}

}
