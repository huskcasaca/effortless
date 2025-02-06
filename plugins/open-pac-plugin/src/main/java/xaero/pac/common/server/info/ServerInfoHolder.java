package xaero.pac.common.server.info;

import xaero.pac.common.server.io.single.ObjectHolderIOHolder;

public final class ServerInfoHolder extends ObjectHolderIOHolder<ServerInfo, ServerInfoHolder> {

	private ServerInfo serverInfo;

	@Override
	public ServerInfo getObject() {
		return serverInfo;
	}

	@Override
	public void setObject(ServerInfo serverInfo) {
		if(this.serverInfo != null)
			throw new IllegalStateException();
		this.serverInfo = serverInfo;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

}
