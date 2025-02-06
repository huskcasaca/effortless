package xaero.pac.common.mods;

import xaero.pac.common.server.player.permission.impl.PlayerLuckPermsSystem;
import xaero.pac.common.server.player.permission.api.IPlayerPermissionSystemAPI;

public class LuckPerms {

	private final IPlayerPermissionSystemAPI permissionSystem;

	public LuckPerms() {
		this.permissionSystem = new PlayerLuckPermsSystem();
	}

	public IPlayerPermissionSystemAPI getPermissionSystem() {
		return permissionSystem;
	}

}
