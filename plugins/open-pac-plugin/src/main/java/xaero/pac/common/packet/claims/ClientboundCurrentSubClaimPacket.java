package xaero.pac.common.packet.claims;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.function.Function;

public class ClientboundCurrentSubClaimPacket extends LazyPacket<ClientboundCurrentSubClaimPacket> {

	public static final Encoder<ClientboundCurrentSubClaimPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	private final int currentSubConfigIndex;
	private final int currentServerSubConfigIndex;
	private final String currentSubConfigId;
	private final String currentServerSubConfigId;

	public ClientboundCurrentSubClaimPacket(int currentSubConfigIndex, int currentServerSubConfigIndex, String currentSubConfigId, String currentServerSubConfigId) {
		super();
		this.currentSubConfigIndex = currentSubConfigIndex;
		this.currentServerSubConfigIndex = currentServerSubConfigIndex;
		this.currentSubConfigId = currentSubConfigId;
		this.currentServerSubConfigId = currentServerSubConfigId;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf u) {
		CompoundTag tag = new CompoundTag();
		tag.putInt("i", currentSubConfigIndex);
		tag.putInt("si", currentServerSubConfigIndex);
		tag.putString("s", currentSubConfigId);
		tag.putString("ss", currentServerSubConfigId);
		u.writeNbt(tag);
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundCurrentSubClaimPacket> getDecoder() {
		return DECODER;
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundCurrentSubClaimPacket> {

		@Override
		public ClientboundCurrentSubClaimPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 4096)
					return null;
				CompoundTag tag = (CompoundTag) input.readNbt(NbtAccounter.unlimitedHeap());
				if(tag == null)
					return null;
				int currentSubConfigIndex = tag.getInt("i");
				int currentServerSubConfigIndex = tag.getInt("si");
				String currentSubConfigId = tag.getString("s");
				String currentServerSubConfigId = tag.getString("ss");
				if(currentSubConfigId.length() > 100 || currentServerSubConfigId.length() > 100){
					OpenPartiesAndClaims.LOGGER.info("Player config sub ID string is too long!");
					return null;
				}
				return new ClientboundCurrentSubClaimPacket(currentSubConfigIndex, currentServerSubConfigIndex, currentSubConfigId, currentServerSubConfigId);
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet ", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundCurrentSubClaimPacket> {

		@Override
		public void handle(ClientboundCurrentSubClaimPacket t) {
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onSubConfigIndices(t.currentSubConfigIndex, t.currentServerSubConfigIndex, t.currentSubConfigId, t.currentServerSubConfigId);
		}

	}

}
