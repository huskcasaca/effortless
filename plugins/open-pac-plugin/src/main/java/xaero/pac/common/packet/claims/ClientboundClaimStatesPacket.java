package xaero.pac.common.packet.claims;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.claims.player.PlayerChunkClaim;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class ClientboundClaimStatesPacket extends LazyPacket<ClientboundClaimStatesPacket> {

	public static final int MAX_STATES = 128;
	public static final Encoder<ClientboundClaimStatesPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	private final List<PlayerChunkClaim> claimStates;

	public ClientboundClaimStatesPacket(List<PlayerChunkClaim> claimStates) {
		super();
		this.claimStates = claimStates;
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundClaimStatesPacket> getDecoder() {
		return DECODER;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf dest) {
		CompoundTag nbt = new CompoundTag();
		ListTag stateListTag = new ListTag();
		for (int i = 0; i < claimStates.size(); i++) {
			PlayerChunkClaim state = claimStates.get(i);
			CompoundTag claimStateNbt = new CompoundTag();
			claimStateNbt.putUUID("p", state.getPlayerId());
			claimStateNbt.putInt("s", state.getSubConfigIndex());
			claimStateNbt.putBoolean("f", state.isForceloadable());
			claimStateNbt.putInt("i", state.getSyncIndex());
			stateListTag.add(claimStateNbt);
		}
		nbt.put("l", stateListTag);
		dest.writeNbt(nbt);
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundClaimStatesPacket> {

		@Override
		public ClientboundClaimStatesPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 16384)
					return null;
				CompoundTag nbt = (CompoundTag) input.readNbt(NbtAccounter.unlimitedHeap());
				if(nbt == null)
					return null;
				ListTag stateListTag = nbt.getList("l", 10);
				if(stateListTag.size() > MAX_STATES) {
					OpenPartiesAndClaims.LOGGER.info("Received claim state list is too large!");
					return null;
				}
				List<PlayerChunkClaim> claimStates = new ArrayList<>(stateListTag.size());
				for (int i = 0; i < stateListTag.size(); i++) {
					CompoundTag claimStateNbt = stateListTag.getCompound(i);
					UUID playerId = claimStateNbt.getUUID("p");
					int subConfigIndex = claimStateNbt.getInt("s");
					boolean forceloadable = claimStateNbt.getBoolean("f");
					int syncIndex = claimStateNbt.getInt("i");
					claimStates.add(new PlayerChunkClaim(playerId, subConfigIndex, forceloadable, syncIndex));
				}
				return new ClientboundClaimStatesPacket(claimStates);
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundClaimStatesPacket> {

		@Override
		public void handle(ClientboundClaimStatesPacket t) {
			for (PlayerChunkClaim claimState : t.claimStates) {
				OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onClaimState(claimState);
			}
		}

	}

}
