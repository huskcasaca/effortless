package xaero.pac.common.server.info;

import xaero.pac.common.server.io.ObjectManagerIOObject;

public final class ServerInfo implements ObjectManagerIOObject {

	public static final int CURRENT_VERSION = 1;
	private long totalUseTime;
	private boolean dirty;
	private final int loadedVersion;

	public ServerInfo(long totalUseTime, int loadedVersion) {
		super();
		this.totalUseTime = totalUseTime;
		this.loadedVersion = loadedVersion;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public String getFileName() {
		return null;
	}

	public void setTotalUseTime(long totalUseTime) {
		this.totalUseTime = totalUseTime;
		setDirty(true);
	}

	public long getTotalUseTime() {
		return totalUseTime;
	}

	public int getLoadedVersion() {
		return loadedVersion;
	}
}
