package xaero.pac.client.player.config;

import com.google.common.collect.Lists;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.packet.config.PlayerConfigOptionValuePacket;
import xaero.pac.common.packet.config.ServerboundPlayerConfigOptionValuePacket;
import xaero.pac.common.packet.config.ServerboundSubConfigExistencePacket;

public class PlayerConfigClientSynchronizer {

	public <T extends Comparable<T>> void syncToServer(PlayerConfigClientStorage config, IPlayerConfigStringableOptionClientStorage<T> option) {
		PlayerConfigOptionValuePacket.Entry packetOptionEntry = new PlayerConfigOptionValuePacket.Entry(option.getId(), option.getType(), option.getValue(), option.isMutable(), option.isDefaulted());

		ServerboundPlayerConfigOptionValuePacket packet = new ServerboundPlayerConfigOptionValuePacket(config.getType(), config.getSubId(), config.getOwner(), Lists.newArrayList(packetOptionEntry));
		OpenPartiesAndClaims.INSTANCE.getPacketHandler().sendToServer(packet);
	}

	public void requestCreateSubConfig(PlayerConfigClientStorage config, String subId){
		ServerboundSubConfigExistencePacket packet = new ServerboundSubConfigExistencePacket(subId, config.getOwner(), config.getType(), true);
		OpenPartiesAndClaims.INSTANCE.getPacketHandler().sendToServer(packet);
	}

	public void requestDeleteSubConfig(PlayerConfigClientStorage config, String subId){
		ServerboundSubConfigExistencePacket packet = new ServerboundSubConfigExistencePacket(subId, config.getOwner(), config.getType(), false);
		OpenPartiesAndClaims.INSTANCE.getPacketHandler().sendToServer(packet);
	}

}
