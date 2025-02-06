package xaero.pac.common.server.claims.player.io.serialization.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import xaero.pac.common.claims.player.PlayerChunkClaim;

import java.util.UUID;

public class PlayerChunkClaimNbtSerializer {

	public PlayerChunkClaim deserialize(UUID playerId, CompoundTag nbt) {
		boolean forceloaded = nbt.getBoolean("forceloaded");
		int subConfigIndex = nbt.contains("subConfigIndex", Tag.TAG_INT) ? nbt.getInt("subConfigIndex") : -1;
		return new PlayerChunkClaim(playerId, subConfigIndex, forceloaded, 0);
	}

	public CompoundTag serialize(PlayerChunkClaim object) {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("subConfigIndex", object.getSubConfigIndex());
		nbt.putBoolean("forceloaded", object.isForceloadable());
		return nbt;
	}

}
