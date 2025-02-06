package xaero.pac.client.player.config;

import xaero.pac.client.player.config.api.IPlayerConfigClientStorageManagerAPI;
import xaero.pac.common.misc.MapFactory;
import xaero.pac.common.player.config.dynamic.PlayerConfigDynamicOptions;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface IPlayerConfigClientStorageManager<CS extends IPlayerConfigClientStorage<?>> extends IPlayerConfigClientStorageManagerAPI {

	//internal api

	@Override
	@Nonnull
	public CS getServerClaimsConfig();

	@Override
	@Nonnull
	public CS getExpiredClaimsConfig();

	@Override
	@Nonnull
	public CS getWildernessConfig();

	@Override
	@Nonnull
	public CS getDefaultPlayerConfig();
	@Override
	@Nonnull
	public CS getMyPlayerConfig();

	public void setOtherPlayerConfig(CS otherPlayerConfig);
	public CS getOtherPlayerConfig();
	public IPlayerConfigClientStorage.IBuilder<CS> beginConfigStorageBuild(MapFactory mapFactory);
	Collection<IPlayerConfigOptionSpecAPI<?>> getOverridableOptions();
	public void setDynamicOptions(PlayerConfigDynamicOptions dynamicOptions);
}
