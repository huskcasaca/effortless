package xaero.pac.client.claims.player;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.client.claims.player.api.IClientPlayerClaimInfoAPI;
import xaero.pac.common.claims.player.IPlayerClaimInfo;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.claims.player.api.IPlayerDimensionClaimsAPI;

import javax.annotation.Nonnull;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

public interface IClientPlayerClaimInfo<DC extends IPlayerDimensionClaims<?>> extends IClientPlayerClaimInfoAPI, IPlayerClaimInfo<DC>	 {

	//internal api

	@Override
	public int getClaimCount();

	@Override
	public int getForceloadCount();

	@Nonnull
	@Override
	public UUID getPlayerId();

	@Nonnull
	@Override
	public String getPlayerUsername();

	@Nonnull
	@Override
	public Stream<Entry<ResourceLocation, DC>> getTypedStream();

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<Entry<ResourceLocation, IPlayerDimensionClaimsAPI>> getStream(){
		return (Stream<Entry<ResourceLocation, IPlayerDimensionClaimsAPI>>)(Object)getTypedStream();
	}

}
