package xaero.pac.common.claims.player.request;

import xaero.pac.common.claims.ClaimsManager;

public class ClaimActionRequest {

	private final ClaimsManager.Action action;
	private final int left;
	private final int top;
	private final int right;
	private final int bottom;
	private final boolean byServer;
	private final int totalChunks;

	public ClaimActionRequest(ClaimsManager.Action action, int left, int top, int right, int bottom, boolean byServer) {
		super();
		this.action = action;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.byServer = byServer;
		this.totalChunks = (1 + right - left) * (1 + top - bottom);
	}

	public ClaimsManager.Action getAction() {
		return action;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public int getRight() {
		return right;
	}

	public int getBottom() {
		return bottom;
	}

	public boolean isByServer() {
		return byServer;
	}

	public int getTotalChunks() {
		return totalChunks;
	}

}
