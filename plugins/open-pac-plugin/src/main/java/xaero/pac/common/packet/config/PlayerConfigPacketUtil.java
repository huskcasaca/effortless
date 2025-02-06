package xaero.pac.common.packet.config;

import xaero.pac.client.player.config.IPlayerConfigClientStorage;
import xaero.pac.client.player.config.IPlayerConfigClientStorageManager;
import xaero.pac.client.player.config.IPlayerConfigStringableOptionClientStorage;
import xaero.pac.common.server.player.config.api.PlayerConfigType;

public class PlayerConfigPacketUtil {

	public static IPlayerConfigClientStorage<IPlayerConfigStringableOptionClientStorage<?>> getTargetConfig(boolean isOtherPlayer, PlayerConfigType type, IPlayerConfigClientStorageManager<IPlayerConfigClientStorage<IPlayerConfigStringableOptionClientStorage<?>>> playerConfigStorageManager){
		IPlayerConfigClientStorage<IPlayerConfigStringableOptionClientStorage<?>> storage;
		if(type == PlayerConfigType.PLAYER && isOtherPlayer) {
			storage = playerConfigStorageManager.getOtherPlayerConfig();
		} else {
			storage =
					type == PlayerConfigType.SERVER ? playerConfigStorageManager.getServerClaimsConfig() :
					type == PlayerConfigType.EXPIRED ? playerConfigStorageManager.getExpiredClaimsConfig() :
					type == PlayerConfigType.WILDERNESS ? playerConfigStorageManager.getWildernessConfig() :
					type == PlayerConfigType.DEFAULT_PLAYER ? playerConfigStorageManager.getDefaultPlayerConfig() :
							playerConfigStorageManager.getMyPlayerConfig();
		}
		return storage;
	}

}
