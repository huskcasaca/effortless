package xaero.pac.common.claims;

import net.minecraft.resources.ResourceLocation;

public class ClaimLocation {

	private final ResourceLocation dimId;
	private final int chunkX;
	private final int chunkZ;

	public ClaimLocation(ResourceLocation dimId, int chunkX, int chunkZ) {
		this.dimId = dimId;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	public ResourceLocation getDimId() {
		return dimId;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}

}
