package xaero.pac.common.server.io;

public class ObjectManagerLiveSaver {

	private final ObjectManagerIO<?, ?, ?, ?> io;
	private long lastSave;
	private long saveInterval;

	public ObjectManagerLiveSaver(ObjectManagerIO<?, ?, ?, ?> io, long saveInterval, long offset) {
		super();
		this.io = io;
		this.saveInterval = saveInterval;
		this.lastSave = System.currentTimeMillis() - offset;
	}

	public boolean onServerTick() {
		if(System.currentTimeMillis() > lastSave + saveInterval) {
			if(io.save()) {
				lastSave = System.currentTimeMillis();
			}
			return true;
		}
		return false;
	}

}
