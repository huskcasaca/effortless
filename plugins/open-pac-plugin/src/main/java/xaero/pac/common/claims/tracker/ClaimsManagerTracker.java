package xaero.pac.common.claims.tracker;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;
import xaero.pac.common.claims.tracker.api.IClaimsManagerListenerAPI;

import javax.annotation.Nonnull;
import java.util.Set;

public class ClaimsManagerTracker implements IClaimsManagerTracker {

	private Set<IClaimsManagerListenerAPI> listeners;

	public ClaimsManagerTracker(Set<IClaimsManagerListenerAPI> listeners) {
		super();
		this.listeners = listeners;
	}

	@Override
	public void register(@Nonnull IClaimsManagerListenerAPI listener) {
		listeners.add(listener);
	}

	public void onWholeRegionChange(ResourceLocation dimension, int regionX, int regionZ) {
		for(IClaimsManagerListenerAPI listener : listeners)
			listener.onWholeRegionChange(dimension, regionX, regionZ);
	}

	public void onChunkChange(ResourceLocation dimension, int chunkX, int chunkZ, IPlayerChunkClaimAPI claim) {
		for(IClaimsManagerListenerAPI listener : listeners)
			listener.onChunkChange(dimension, chunkX, chunkZ, claim);
	}

	public void onDimensionChange(ResourceLocation dimension) {
		for(IClaimsManagerListenerAPI listener : listeners)
			listener.onDimensionChange(dimension);
	}

}
