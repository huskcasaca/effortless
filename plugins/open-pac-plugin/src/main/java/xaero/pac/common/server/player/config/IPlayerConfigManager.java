package xaero.pac.common.server.player.config;

import xaero.pac.common.server.player.config.api.IPlayerConfigManagerAPI;
import xaero.pac.common.server.player.config.sync.IPlayerConfigSynchronizer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface IPlayerConfigManager extends IPlayerConfigManagerAPI {
	//internal API

	@Nonnull
	@Override
	public IPlayerConfig getLoadedConfig(@Nullable UUID id);
	@Nonnull
	@Override
	public IPlayerConfig getDefaultConfig();
	@Nonnull
	@Override
	public IPlayerConfig getWildernessConfig();
	@Nonnull
	@Override
	public IPlayerConfig getServerClaimConfig();
	@Nonnull
	@Override
	public IPlayerConfig getExpiredClaimConfig();
	public IPlayerConfigSynchronizer getSynchronizer();

}
