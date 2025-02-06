package xaero.pac.client.claims.player;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import xaero.pac.client.claims.ClientClaimsManager;
import xaero.pac.common.claims.player.PlayerChunkClaim;
import xaero.pac.common.claims.player.PlayerClaimInfoManager;
import xaero.pac.common.claims.player.PlayerDimensionClaims;
import xaero.pac.common.claims.tracker.ClaimsManagerTracker;
import xaero.pac.common.util.linked.LinkedChain;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

//only used by ClientClaimsManager
public final class ClientPlayerClaimInfoManager extends PlayerClaimInfoManager<ClientPlayerClaimInfo, ClientPlayerClaimInfoManager> {

	public ClientPlayerClaimInfoManager(Map<UUID, ClientPlayerClaimInfo> storage, LinkedChain<ClientPlayerClaimInfo> linkedPlayerInfo) {
		super(storage, linkedPlayerInfo);
	}

	@Override
	protected ClientPlayerClaimInfo create(String username, UUID playerId,
			Map<ResourceLocation, PlayerDimensionClaims> claims) {
		return new ClientPlayerClaimInfo(username, playerId, claims, this, new Int2ObjectOpenHashMap<>());
	}

	public void updatePlayerInfo(UUID playerId, String username, ClientClaimsManager claimsManager) {
		ClientPlayerClaimInfo playerInfo = getInfo(playerId);
		playerInfo.setPlayerUsername(username);
		if(playerInfo.getClaimsColor(-1) == null)
			updateSubClaimInfo(playerId, -1, "", 0, claimsManager);//ensuring something is always there for the main sub
	}

	public void updateSubClaimInfo(UUID playerId, int subConfigIndex, String claimsName, Integer claimsColor, ClientClaimsManager claimsManager) {
		ClientPlayerClaimInfo playerInfo = getInfo(playerId);
		playerInfo.ensureSubClaim(subConfigIndex);
		playerInfo.setClaimsName(subConfigIndex, claimsName);
		Integer oldColor = playerInfo.getClaimsColor(subConfigIndex);
		playerInfo.setClaimsColor(subConfigIndex, claimsColor);
		if(!Objects.equals(oldColor, claimsColor)) {
			boolean notManyClaims = playerInfo.getClaimCount() < 1024;
			boolean isSub = subConfigIndex != -1;
			ClaimsManagerTracker tracker = claimsManager.getTracker();
			playerInfo.getTypedStream().map(Entry::getValue).forEach(dim -> {
				ResourceLocation dimensionId = dim.getDimension();
				if(notManyClaims) {
					BiConsumer<PlayerChunkClaim, ChunkPos> claimConsumer = (claim, pos) -> tracker.onChunkChange(dimensionId, pos.x, pos.z, claim);
					dim.getTypedStream().forEach(posList -> {
						PlayerChunkClaim state = posList.getClaimState();
						int claimSubConfigIndex = state.getSubConfigIndex();
						if(claimSubConfigIndex != subConfigIndex && (isSub || playerInfo.getClaimsColor(claimSubConfigIndex) != null))
							return;
						posList.getStream().forEach(pos -> claimConsumer.accept(state, pos));
					});
				} else
					tracker.onDimensionChange(dimensionId);
			});
		}
	}

}
