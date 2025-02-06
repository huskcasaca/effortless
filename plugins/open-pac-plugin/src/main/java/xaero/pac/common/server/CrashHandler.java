package xaero.pac.common.server;

public class CrashHandler {

	private Throwable crashWith;

	public void check() {
		if(crashWith != null) {
			Throwable toThrow = crashWith;
			crashWith = null;
			if(toThrow instanceof RuntimeException)
				throw (RuntimeException)toThrow;
			throw new RuntimeException(toThrow);
		}
	}

	public void crash(Throwable crashWith) {
		if(this.crashWith == null)
			this.crashWith = crashWith;
	}

}
