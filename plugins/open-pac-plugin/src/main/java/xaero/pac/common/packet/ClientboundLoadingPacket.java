package xaero.pac.common.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.function.Function;

public class ClientboundLoadingPacket extends LazyPacket<ClientboundLoadingPacket> {

	public static final Encoder<ClientboundLoadingPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	public static final ClientboundLoadingPacket START_PARTY = new ClientboundLoadingPacket(true, false);
	public static final ClientboundLoadingPacket END_PARTY = new ClientboundLoadingPacket(false, false);

	public static final ClientboundLoadingPacket START_CLAIMS = new ClientboundLoadingPacket(true, true);
	public static final ClientboundLoadingPacket END_CLAIMS = new ClientboundLoadingPacket(false, true);

	private final boolean start;
	private final boolean claims;

	private ClientboundLoadingPacket(boolean start, boolean claims) {
		super();
		this.start = start;
		this.claims = claims;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf u) {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("s", start);
		tag.putBoolean("c", claims);
		u.writeNbt(tag);
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundLoadingPacket> getDecoder() {
		return DECODER;
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundLoadingPacket> {

		@Override
		public ClientboundLoadingPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 1024)
					return null;
				CompoundTag tag = (CompoundTag) input.readNbt(NbtAccounter.unlimitedHeap());
				if(tag == null)
					return null;
				boolean start = tag.getBoolean("s");
				boolean claims = tag.getBoolean("c");
				return new ClientboundLoadingPacket(start, claims);
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundLoadingPacket> {

		@Override
		public void handle(ClientboundLoadingPacket t) {
			if(!t.claims) {
				OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientPartyStorage().setLoading(t.start);
			} else {
				OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onLoading(t.start);
			}
		}

	}

}
