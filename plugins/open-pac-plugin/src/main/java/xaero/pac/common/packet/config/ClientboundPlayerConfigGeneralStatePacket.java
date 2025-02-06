package xaero.pac.common.packet.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.client.player.config.IPlayerConfigClientStorage;
import xaero.pac.client.player.config.IPlayerConfigClientStorageManager;
import xaero.pac.client.player.config.IPlayerConfigStringableOptionClientStorage;
import xaero.pac.common.server.player.config.api.PlayerConfigType;

public class ClientboundPlayerConfigGeneralStatePacket extends ClientboundPlayerConfigAbstractStatePacket {

	private final boolean beingDeleted;
	private final int subConfigLimit;

	public ClientboundPlayerConfigGeneralStatePacket(PlayerConfigType type, boolean otherPlayer, String subId, boolean beingDeleted, int subConfigLimit){
		super(type, otherPlayer, subId);
		this.beingDeleted = beingDeleted;
		this.subConfigLimit = subConfigLimit;
	}

	public static class Codec extends ClientboundPlayerConfigAbstractStatePacket.Codec<ClientboundPlayerConfigGeneralStatePacket> {

		@Override
		protected ClientboundPlayerConfigGeneralStatePacket decode(CompoundTag nbt, PlayerConfigType type, boolean otherPlayer, String subId) {
			if(!nbt.contains("d", Tag.TAG_BYTE)) {
				OpenPartiesAndClaims.LOGGER.info("Unknown player config being deleted state!");
				return null;
			}
			boolean beingDeleted = nbt.getBoolean("d");
			int subConfigLimit = nbt.getInt("sl");
			return new ClientboundPlayerConfigGeneralStatePacket(type, otherPlayer, subId, beingDeleted, subConfigLimit);
		}

		@Override
		protected void encode(ClientboundPlayerConfigGeneralStatePacket packet, CompoundTag nbt) {
			nbt.putBoolean("d", packet.beingDeleted);
			nbt.putInt("sl", packet.subConfigLimit);
		}

		@Override
		protected int getExtraSizeLimit() {
			return 0;
		}

	}

	public static class ClientHandler extends ClientboundPlayerConfigAbstractStatePacket.ClientHandler<ClientboundPlayerConfigGeneralStatePacket> {

		@Override
		protected void accept(ClientboundPlayerConfigGeneralStatePacket t, IPlayerConfigClientStorageManager<IPlayerConfigClientStorage<IPlayerConfigStringableOptionClientStorage<?>>> playerConfigStorageManager, IPlayerConfigClientStorage<IPlayerConfigStringableOptionClientStorage<?>> storage) {
			storage.setGeneralState(t.beingDeleted, t.subConfigLimit);
		}

	}

}
