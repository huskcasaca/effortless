package xaero.pac.common.server.claims.forceload;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.UUID;

public class ClaimTicket {

	private final UUID playerId;
	private final ResourceLocation dimension;
	private final int x;
	private final int z;
	private boolean enabled;

	public ClaimTicket(UUID playerId, ResourceLocation dimension, int x, int z) {
		super();
		this.playerId = playerId;
		this.dimension = dimension;
		this.x = x;
		this.z = z;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof ClaimTicket))
			return false;
		if(obj == this)
			return true;
		ClaimTicket other = (ClaimTicket) obj;
		return Objects.equals(dimension, other.dimension) && x == other.x && z == other.z;//must not include enabled
	}

	@Override
	public int hashCode() {
		return Objects.hash(dimension, x, z);//must not include enabled and playerId is unnecessary
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public ResourceLocation getDimension() {
		return dimension;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
