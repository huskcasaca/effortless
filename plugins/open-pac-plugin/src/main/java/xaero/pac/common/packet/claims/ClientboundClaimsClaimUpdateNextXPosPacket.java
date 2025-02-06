package xaero.pac.common.packet.claims;

import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.function.Function;

public class ClientboundClaimsClaimUpdateNextXPosPacket extends LazyPacket<ClientboundClaimsClaimUpdateNextXPosPacket> {

	public static final Encoder<ClientboundClaimsClaimUpdateNextXPosPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	public ClientboundClaimsClaimUpdateNextXPosPacket() {
		super();
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundClaimsClaimUpdateNextXPosPacket> getDecoder() {
		return DECODER;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf u) {
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundClaimsClaimUpdateNextXPosPacket> {

		@Override
		public ClientboundClaimsClaimUpdateNextXPosPacket apply(FriendlyByteBuf input) {
			try {
				return new ClientboundClaimsClaimUpdateNextXPosPacket();
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundClaimsClaimUpdateNextXPosPacket> {

		@Override
		public void handle(ClientboundClaimsClaimUpdateNextXPosPacket t) {
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onClaimUpdateNextXPos();
		}

	}

}
