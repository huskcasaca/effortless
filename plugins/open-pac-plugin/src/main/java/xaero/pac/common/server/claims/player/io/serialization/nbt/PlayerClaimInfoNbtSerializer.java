package xaero.pac.common.server.claims.player.io.serialization.nbt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.PlayerDimensionClaims;
import xaero.pac.common.server.claims.player.ServerPlayerClaimInfo;
import xaero.pac.common.server.claims.player.ServerPlayerClaimInfoManager;
import xaero.pac.common.server.io.serialization.SimpleSerializer;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerClaimInfoNbtSerializer implements SimpleSerializer<CompoundTag, UUID, ServerPlayerClaimInfo, ServerPlayerClaimInfoManager> {

	private final PlayerDimensionClaimsNbtSerializer playerDimensionClaimsNbtSerializer;

	private PlayerClaimInfoNbtSerializer(PlayerDimensionClaimsNbtSerializer playerDimensionClaimsNbtSerializer) {
		super();
		this.playerDimensionClaimsNbtSerializer = playerDimensionClaimsNbtSerializer;
	}

	@Override
	public CompoundTag serialize(ServerPlayerClaimInfo object) {
		CompoundTag nbt = new CompoundTag();
		CompoundTag dimensions = new CompoundTag();
		object.getFullStream().forEach(e -> dimensions.put(e.getKey().toString(), playerDimensionClaimsNbtSerializer.serialize(e.getValue())));
		nbt.put("dimensions", dimensions);
		nbt.putString("username", object.getPlayerUsername());
		nbt.putLong("confirmedActivity", object.getRegisteredActivity());
		return nbt;
	}

	@Override
	public ServerPlayerClaimInfo deserialize(UUID id, ServerPlayerClaimInfoManager manager, CompoundTag nbt) {
		CompoundTag dimensionsTag = nbt.getCompound("dimensions");
		String username = nbt.getString("username");
		Map<ResourceLocation, PlayerDimensionClaims> claims = new HashMap<>();
		dimensionsTag.getAllKeys().forEach(key -> claims.put(ResourceLocation.parse(key), playerDimensionClaimsNbtSerializer.deserialize(id, key, dimensionsTag.getCompound(key))));
		ServerPlayerClaimInfo result = new ServerPlayerClaimInfo(manager.getConfig(id), username, id, claims, manager, new ArrayDeque<>());
		result.setRegisteredActivity(nbt.getLong("confirmedActivity"));
		return result;
	}

	public static final class Builder {

		private Builder() {
		}

		private Builder setDefault() {
			return this;
		}

		public PlayerClaimInfoNbtSerializer build() {
			return new PlayerClaimInfoNbtSerializer(new PlayerDimensionClaimsNbtSerializer(new PlayerChunkClaimNbtSerializer()));
		}

		public static Builder begin() {
			return new Builder().setDefault();
		}

	}

}
