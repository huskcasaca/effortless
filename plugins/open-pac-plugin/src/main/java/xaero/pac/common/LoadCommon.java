package xaero.pac.common;

import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.client.LoadClient;
import xaero.pac.common.packet.PacketRegister;
import xaero.pac.common.server.config.ServerConfig;

public class LoadCommon {

	protected final OpenPartiesAndClaims modMain;

	public LoadCommon(OpenPartiesAndClaims modMain) {
		this.modMain = modMain;
		modMain.getModSupport().check(this instanceof LoadClient);
		modMain.getModSupport().init();
	}

	public void loadCommon() {
		OpenPartiesAndClaims.LOGGER.info("Loading Open Parties and Claims!");
		modMain.getForgeConfigHelper().registerServerConfig(ServerConfig.SPEC);

		new PacketRegister().register(this);
	}

}
