package xaero.pac.common.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientboundModesPacket {

	private final boolean adminMode;
	private final boolean serverMode;

	public ClientboundModesPacket(boolean adminMode, boolean serverMode) {
		super();
		this.adminMode = adminMode;
		this.serverMode = serverMode;
	}

	public static class Codec implements BiConsumer<ClientboundModesPacket, FriendlyByteBuf>, Function<FriendlyByteBuf, ClientboundModesPacket> {

		@Override
		public ClientboundModesPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 1024)
					return null;
				CompoundTag tag = (CompoundTag) input.readNbt(NbtAccounter.unlimitedHeap());
				if(tag == null)
					return null;
				boolean adminMode = tag.getBoolean("am");
				boolean serverMode = tag.getBoolean("sm");
				return new ClientboundModesPacket(adminMode, serverMode);
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet ", t);
				return null;
			}
		}

		@Override
		public void accept(ClientboundModesPacket t, FriendlyByteBuf u) {
			CompoundTag tag = new CompoundTag();
			tag.putBoolean("am", t.adminMode);
			tag.putBoolean("sm", t.serverMode);
			u.writeNbt(tag);
		}

	}

	public static class ClientHandler implements Consumer<ClientboundModesPacket> {

		@Override
		public void accept(ClientboundModesPacket t) {
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().onClaimModes(t.adminMode, t.serverMode);
		}

	}

}
