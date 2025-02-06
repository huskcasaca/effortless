package xaero.pac.common.server.io;

public interface ObjectManagerIOObject {

	public boolean isDirty();
	public void setDirty(boolean dirty);
	public String getFileName();

}
