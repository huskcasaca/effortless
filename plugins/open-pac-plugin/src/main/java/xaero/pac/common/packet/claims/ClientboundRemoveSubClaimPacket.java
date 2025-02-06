package xaero.pac.common.packet.claims;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.UUID;
import java.util.function.Function;

public class ClientboundRemoveSubClaimPacket extends LazyPacket<ClientboundRemoveSubClaimPacket> {

	public static final Encoder<ClientboundRemoveSubClaimPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	private final UUID playerId;
	private final int subConfigIndex;

	public ClientboundRemoveSubClaimPacket(UUID playerId, int subConfigIndex) {
		super();
		this.playerId = playerId;
		this.subConfigIndex = subConfigIndex;
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundRemoveSubClaimPacket> getDecoder() {
		return DECODER;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf u) {
		CompoundTag nbt = new CompoundTag();
		nbt.putUUID("p", playerId);
		nbt.putInt("s", subConfigIndex);
		u.writeNbt(nbt);
	}

	@Override
	public String toString() {
		return String.format("[%s, %d]", playerId, subConfigIndex);
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundRemoveSubClaimPacket> {

		@Override
		public ClientboundRemoveSubClaimPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 1024)
					return null;
				CompoundTag nbt = (CompoundTag) input.readNbt(NbtAccounter.unlimitedHeap());
				if(nbt == null)
					return null;
				UUID playerId = nbt.getUUID("p");
				int subConfigIndex = nbt.getInt("s");
				return new ClientboundRemoveSubClaimPacket(playerId, subConfigIndex);
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundRemoveSubClaimPacket> {

		@Override
		public void handle(ClientboundRemoveSubClaimPacket t) {
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onRemoveSubClaim(t.playerId, t.subConfigIndex);
		}

	}

}
