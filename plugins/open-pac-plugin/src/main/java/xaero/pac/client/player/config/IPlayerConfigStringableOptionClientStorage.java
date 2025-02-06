package xaero.pac.client.player.config;

import xaero.pac.client.player.config.api.IPlayerConfigStringableOptionClientStorageAPI;

public interface IPlayerConfigStringableOptionClientStorage<T extends Comparable<T>> extends IPlayerConfigStringableOptionClientStorageAPI<T>, IPlayerConfigOptionClientStorage<T> {

	//internal api

}
