package xaero.pac.common.packet.claims;

import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.function.Function;

public class ClientboundClaimsClaimUpdateNextZPosPacket extends LazyPacket<ClientboundClaimsClaimUpdateNextZPosPacket> {

	public static final Encoder<ClientboundClaimsClaimUpdateNextZPosPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	public ClientboundClaimsClaimUpdateNextZPosPacket() {
		super();
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundClaimsClaimUpdateNextZPosPacket> getDecoder() {
		return DECODER;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf u) {
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundClaimsClaimUpdateNextZPosPacket> {

		@Override
		public ClientboundClaimsClaimUpdateNextZPosPacket apply(FriendlyByteBuf input) {
			try {
				return new ClientboundClaimsClaimUpdateNextZPosPacket();
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundClaimsClaimUpdateNextZPosPacket> {

		@Override
		public void handle(ClientboundClaimsClaimUpdateNextZPosPacket t) {
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onClaimUpdateNextZPos();
		}

	}

}
