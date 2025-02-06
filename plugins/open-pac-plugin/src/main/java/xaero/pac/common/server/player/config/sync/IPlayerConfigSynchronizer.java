package xaero.pac.common.server.player.config.sync;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.server.player.config.IPlayerConfig;
import xaero.pac.common.server.player.config.api.IPlayerConfigOptionSpecAPI;

public interface IPlayerConfigSynchronizer {

	//internal api

	public void syncAllToClient(ServerPlayer player);

	public void syncOnLogin(ServerPlayer player);

	public void confirmSubConfigCreationSync(ServerPlayer player, IPlayerConfig mainConfig);

	public void syncGeneralState(ServerPlayer player, IPlayerConfig config);

	public void syncSubExistence(ServerPlayer player, IPlayerConfig subConfig, boolean create);

	public <T extends Comparable<T>> void syncOptionToClient(ServerPlayer player, IPlayerConfig config, IPlayerConfigOptionSpecAPI<T> option);

}
