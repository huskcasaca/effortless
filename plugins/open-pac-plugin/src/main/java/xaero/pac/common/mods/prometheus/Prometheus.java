package xaero.pac.common.mods.prometheus;

import earth.terrarium.prometheus.api.roles.options.RoleOptionsApi;
import xaero.pac.client.mods.prometheus.PrometheusClient;
import xaero.pac.common.server.player.permission.api.IPlayerPermissionSystemAPI;
import xaero.pac.common.server.player.permission.impl.PlayerPrometheusPermissions;

public class Prometheus {

	private final boolean client;

	public Prometheus(boolean client) {
		this.client = client;
		permissionSystem = new PlayerPrometheusPermissions();
	}

	private final IPlayerPermissionSystemAPI permissionSystem;

	public IPlayerPermissionSystemAPI getPermissionSystem() {
		return permissionSystem;
	}

	public void init(){
		RoleOptionsApi.API.register(OPACOptions.SERIALIZER);
	}

	public void initClient(){
		if(client)
			new PrometheusClient().init();
	}


}
