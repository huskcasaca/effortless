package xaero.pac.common.packet.claims;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class ClientboundClaimOwnerPropertiesPacket extends LazyPacket<ClientboundClaimOwnerPropertiesPacket> {

	public static final int MAX_PROPERTIES = 32;
	public static final Encoder<ClientboundClaimOwnerPropertiesPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	private final List<PlayerProperties> properties;

	public ClientboundClaimOwnerPropertiesPacket(List<PlayerProperties> properties) {
		super();
		this.properties = properties;
	}

	@Override
	protected Function<FriendlyByteBuf, ClientboundClaimOwnerPropertiesPacket> getDecoder() {
		return DECODER;
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf dest) {
		CompoundTag nbt = new CompoundTag();
		ListTag propertiesListTag = new ListTag();
		for (int i = 0; i < this.properties.size(); i++) {
			PlayerProperties propertiesEntry = this.properties.get(i);
			CompoundTag propertiesEntryNbt = new CompoundTag();
			propertiesEntryNbt.putUUID("p", propertiesEntry.playerId);
			propertiesEntryNbt.putString("u", propertiesEntry.username);
			propertiesListTag.add(propertiesEntryNbt);
		}
		nbt.put("l", propertiesListTag);
		dest.writeNbt(nbt);
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClientboundClaimOwnerPropertiesPacket> {

		@Override
		public ClientboundClaimOwnerPropertiesPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 32768)
					return null;
				CompoundTag nbt = (CompoundTag) input.readNbt(NbtAccounter.unlimitedHeap());
				if(nbt == null)
					return null;
				ListTag propertiesListTag = nbt.getList("l", 10);
				if(propertiesListTag.size() > MAX_PROPERTIES) {
					OpenPartiesAndClaims.LOGGER.info("Received claim owner properties list is too large!");
					return null;
				}
				List<PlayerProperties> propertiesList = new ArrayList<>(propertiesListTag.size());
				for (int i = 0; i < propertiesListTag.size(); i++) {
					CompoundTag propertiesEntryNbt = propertiesListTag.getCompound(i);
					String username = propertiesEntryNbt.getString("u");
					if(username.isEmpty() || username.length() > 128) {
						OpenPartiesAndClaims.LOGGER.info("Received claim owner properties list with invalid player username!");
						return null;
					}
					UUID playerId = propertiesEntryNbt.getUUID("p");
					propertiesList.add(new PlayerProperties(playerId, username));
				}
				return new ClientboundClaimOwnerPropertiesPacket(propertiesList);
			} catch(Throwable t) {
				OpenPartiesAndClaims.LOGGER.error("invalid packet", t);
				return null;
			}
		}

	}

	public static class ClientHandler extends Handler<ClientboundClaimOwnerPropertiesPacket> {

		@Override
		public void handle(ClientboundClaimOwnerPropertiesPacket t) {
			for (PlayerProperties propertiesEntry : t.properties) {
				OpenPartiesAndClaims.INSTANCE.getClientDataInternal().getClientClaimsSyncHandler().
					onPlayerInfo(propertiesEntry.playerId, propertiesEntry.username);
			}
		}

	}

	public static class PlayerProperties {

		private final UUID playerId;
		private final String username;

		public PlayerProperties(UUID playerId, String username) {
			super();
			this.playerId = playerId;
			this.username = username;
		}

		@Override
		public String toString() {
			return String.format("[%s, %s]", playerId, username);
		}

	}

}
