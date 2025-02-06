package xaero.pac.common.server.claims.player.io.serialization.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.PlayerChunkClaim;
import xaero.pac.common.claims.player.PlayerClaimPosList;
import xaero.pac.common.claims.player.PlayerDimensionClaims;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDimensionClaimsNbtSerializer {

	private final PlayerChunkClaimNbtSerializer playerChunkClaimDataNbtSerializer;

	public PlayerDimensionClaimsNbtSerializer(PlayerChunkClaimNbtSerializer playerChunkClaimDataNbtSerializer) {
		super();
		this.playerChunkClaimDataNbtSerializer = playerChunkClaimDataNbtSerializer;
	}

	public PlayerDimensionClaims deserialize(UUID playerId, String dimension, CompoundTag nbt) {
		ListTag claimsTag = nbt.getList("claims", 10);
		HashMap<PlayerChunkClaim, PlayerClaimPosList> claimLists = new HashMap<>(64);
		claimsTag.forEach(t -> {
			CompoundTag posListTag = (CompoundTag) t;
			CompoundTag stateTag = posListTag.getCompound("state");
			ListTag positionsTag = posListTag.getList("positions", 10);
			PlayerChunkClaim state = playerChunkClaimDataNbtSerializer.deserialize(playerId, stateTag);
			PlayerClaimPosList posList = PlayerClaimPosList.Builder.begin().setClaim(state).build();
			positionsTag.forEach(t2 -> {
				CompoundTag posTag = (CompoundTag) t2;
				int x = posTag.getInt("x");
				int z = posTag.getInt("z");
				posList.add(x, z);
			});
			claimLists.put(state, posList);
		});
		return new PlayerDimensionClaims(playerId, ResourceLocation.parse(dimension), claimLists);
	}

	public CompoundTag serialize(PlayerDimensionClaims data) {
		CompoundTag nbt = new CompoundTag();
		ListTag claims = new ListTag();
		data.getTypedStream().forEach(posList -> {
			CompoundTag posListTag = new CompoundTag();
			PlayerChunkClaim claim = posList.getClaimState();
			CompoundTag stateTag = playerChunkClaimDataNbtSerializer.serialize(claim);
			posListTag.put("state", stateTag);
			ListTag positionsTag = new ListTag();
			posList.getStream().forEach(pos -> {
				CompoundTag posTag = new CompoundTag();
				posTag.putInt("x", pos.x);
				posTag.putInt("z", pos.z);
				positionsTag.add(posTag);
			});
			posListTag.put("positions", positionsTag);
			claims.add(posListTag);
		});
		nbt.put("claims", claims);
		return nbt;
	}

}
