package xaero.pac.client.world;

import xaero.pac.client.world.api.IClientWorldDataAPI;

public interface IClientWorldData extends IClientWorldDataAPI {

	//internal api

	void setServerHasMod(boolean b);
	void setServerHasClaimsEnabled(boolean serverHasClaimsEnabled);
	void setServerHasPartiesEnabled(boolean serverHasPartiesEnabled);

}
