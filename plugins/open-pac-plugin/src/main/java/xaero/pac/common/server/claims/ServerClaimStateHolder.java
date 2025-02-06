package xaero.pac.common.server.claims;

import xaero.pac.common.claims.ClaimStateHolder;
import xaero.pac.common.claims.player.PlayerChunkClaim;
import xaero.pac.common.util.linked.ILinkedChainNode;

public final class ServerClaimStateHolder extends ClaimStateHolder implements ILinkedChainNode<ServerClaimStateHolder> {

	private long regionCount;
	private ServerClaimStateHolder previous;
	private ServerClaimStateHolder next;
	private boolean destroyed;

	public ServerClaimStateHolder(PlayerChunkClaim state) {
		super(state);
	}

	public long getRegionCount() {
		return regionCount;
	}

	public void countRegions(int direction){
		regionCount += direction;
	}

	@Override
	public void setNext(ServerClaimStateHolder element) {
		this.next = element;
	}

	@Override
	public void setPrevious(ServerClaimStateHolder element) {
		this.previous = element;
	}

	@Override
	public ServerClaimStateHolder getNext() {
		return next;
	}

	@Override
	public ServerClaimStateHolder getPrevious() {
		return previous;
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void onDestroyed() {
		destroyed = true;
	}

}
