package xaero.pac.common.server.expiration;

import xaero.pac.common.server.info.ServerInfo;
import xaero.pac.common.server.io.ObjectManagerIOObject;

public interface ObjectManagerIOExpirableObject extends ObjectManagerIOObject {

	public void registerActivity(ServerInfo serverInfo);

	public long getRegisteredActivity();

}
