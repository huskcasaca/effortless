package xaero.pac.common.server.io.exception;

public class IOThreadWorkerException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -8833731198982767555L;

	public IOThreadWorkerException() {
		super("IO thread worker is dead (the game is crashing)!");
	}

}
