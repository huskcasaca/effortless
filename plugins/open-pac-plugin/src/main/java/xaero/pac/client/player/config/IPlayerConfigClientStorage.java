package xaero.pac.client.player.config;

import xaero.pac.client.player.config.api.IPlayerConfigClientStorageAPI;
import xaero.pac.client.player.config.api.IPlayerConfigStringableOptionClientStorageAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;
import xaero.pac.common.server.player.config.api.PlayerConfigType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface IPlayerConfigClientStorage<OS extends IPlayerConfigStringableOptionClientStorage<?>> extends IPlayerConfigClientStorageAPI {

	//internal api
	@Nonnull
	public <T extends Comparable<T>> OS getOptionStorage(@Nonnull IPlayerConfigOptionSpecAPI<T> option);

	@Nonnull
	public Stream<OS> typedOptionStream();

	@Nonnull
	@Override
	@SuppressWarnings("unchecked")
	default Stream<IPlayerConfigStringableOptionClientStorageAPI<?>> optionStream(){
		return (Stream<IPlayerConfigStringableOptionClientStorageAPI<?>>)(Object)typedOptionStream();
	}

	public void reset();

	public boolean isSyncInProgress();

	public void setSyncInProgress(boolean syncInProgress);

	@Nonnull
	@Override
	public List<String> getSubConfigIds();

	@Nullable
	@Override
	IPlayerConfigClientStorage<OS> getSubConfig(@Nonnull String id);

	@Nonnull
	@Override
	IPlayerConfigClientStorage<OS> getEffectiveSubConfig(@Nonnull String id);

	@Nonnull
	@Override
	public Stream<IPlayerConfigClientStorageAPI> getSubConfigAPIStream();

	@Override
	boolean isBeingDeleted();

	public Stream<IPlayerConfigClientStorage<OS>> getSubConfigStream();

	public void setSelectedSubConfig(String selectedSubConfig);

	public String getSelectedSubConfig();

	public IPlayerConfigClientStorage<OS> getOrCreateSubConfig(String subId);

	public void removeSubConfig(String subId);

	public void setGeneralState(boolean beingDeleted, int subConfigLimit);

	public static interface IBuilder<CS extends IPlayerConfigClientStorage<?>> {

		public IBuilder<CS> setDefault();

		public IBuilder<CS> setType(PlayerConfigType type);

		public IBuilder<CS> setOwner(UUID owner);

		public CS build();

	}

}
