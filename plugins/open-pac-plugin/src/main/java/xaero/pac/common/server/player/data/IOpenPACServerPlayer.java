package xaero.pac.common.server.player.data;

import xaero.pac.common.server.player.data.api.ServerPlayerDataAPI;

public interface IOpenPACServerPlayer {

	public ServerPlayerDataAPI getXaero_OPAC_PlayerData();
	public void setXaero_OPAC_PlayerData(ServerPlayerDataAPI data);

}
