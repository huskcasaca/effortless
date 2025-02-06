package xaero.pac.common.mods;

import xaero.pac.common.server.player.permission.api.IPlayerPermissionSystemAPI;
import xaero.pac.common.server.player.permission.impl.PlayerFTBPermissionSystem;

public class FTBRanks {

	private final IPlayerPermissionSystemAPI permissionSystem;

	public FTBRanks() {
		this.permissionSystem = new PlayerFTBPermissionSystem();
	}

	public IPlayerPermissionSystemAPI getPermissionSystem() {
		return permissionSystem;
	}

}
