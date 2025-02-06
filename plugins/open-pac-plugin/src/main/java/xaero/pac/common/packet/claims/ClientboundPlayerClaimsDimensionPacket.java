package xaero.pac.common.packet.claims;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.function.Function;

public class ClientboundPlayerClaimsDimensionPacket extends LazyPacket<ClientboundPlayerClaimsDimensionPacket>{

	public static final Encoder<ClientboundPlayerClaimsDimensionPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	private final ResourceLocation dimension;

	public ClientboundPlayerClaimsDimensionPacket(ResourceLocation dimension) {
		super();
		this.dimension = dimension;
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundPlayerClaimsDimensionPacket> getDecoder() {
		return DECODER;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf u) {
		CompoundTag nbt = new CompoundTag();
		if(dimension != null)
			nbt.putString("d", dimension.toString());//4096
		u.writeNbt(nbt);
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundPlayerClaimsDimensionPacket> {

		@Override
		public ClientboundPlayerClaimsDimensionPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 524288)
					return null;
				CompoundTag nbt = (CompoundTag) input.readNbt(NbtAccounter.unlimitedHeap());
				if(nbt == null)
					return null;
				String dimensionString = nbt.contains("d") ? nbt.getString("d") : "";
				if(dimensionString.length() > 2048)
					return null;
				return new ClientboundPlayerClaimsDimensionPacket(dimensionString.isEmpty() ? null : ResourceLocation.parse(dimensionString));
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundPlayerClaimsDimensionPacket> {

		@Override
		public void handle(ClientboundPlayerClaimsDimensionPacket t) {
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onDimension(t.dimension);
		}

	}

}
