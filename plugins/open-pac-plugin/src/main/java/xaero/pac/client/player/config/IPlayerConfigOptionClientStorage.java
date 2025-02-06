package xaero.pac.client.player.config;

import xaero.pac.client.player.config.api.IPlayerConfigOptionClientStorageAPI;

public interface IPlayerConfigOptionClientStorage<T extends Comparable<T>> extends IPlayerConfigOptionClientStorageAPI<T> {

	//internal api

	public void setValue(T value);

	public void setCastValue(Object value);

	public void setDefaulted(boolean defaulted);

	public void setMutable(boolean mutable);

}
