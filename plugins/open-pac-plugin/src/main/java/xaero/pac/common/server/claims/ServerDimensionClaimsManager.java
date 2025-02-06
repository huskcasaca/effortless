package xaero.pac.common.server.claims;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.DimensionClaimsManager;
import xaero.pac.common.claims.storage.RegionClaimsPaletteStorage;
import xaero.pac.common.server.claims.player.ServerPlayerClaimInfoManager;
import xaero.pac.common.util.linked.LinkedChain;

public final class ServerDimensionClaimsManager extends DimensionClaimsManager<ServerPlayerClaimInfoManager, ServerRegionClaims> implements IServerDimensionClaimsManager<ServerRegionClaims> {

	private final ServerClaimsManager manager;
	private final boolean playerClaimsSyncAllowed;

	public ServerDimensionClaimsManager(ResourceLocation dimension, Long2ObjectMap<ServerRegionClaims> claims, LinkedChain<ServerRegionClaims> linkedRegions, ServerClaimsManager manager, boolean playerClaimsSyncAllowed) {
		super(dimension, claims, linkedRegions);
		this.manager = manager;
		this.playerClaimsSyncAllowed = playerClaimsSyncAllowed;
	}

	@Override
	protected ServerRegionClaims create(ResourceLocation dimension, int x, int z, RegionClaimsPaletteStorage storage) {
		return ServerRegionClaims.Builder.begin().setPlayerClaimsSyncAllowed(playerClaimsSyncAllowed).setDimension(dimension).setManager(manager).setX(x).setZ(z).build();
	}

}
