package xaero.pac.client.claims.tracker.result;

import xaero.pac.client.claims.tracker.result.api.IClaimsManagerClaimResultListenerAPI;
import xaero.pac.common.claims.result.api.AreaClaimResult;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class ClaimsManagerClaimResultTracker implements IClaimsManagerClaimResultTracker {

	private Set<IClaimsManagerClaimResultListenerAPI> listeners;

	private ClaimsManagerClaimResultTracker(Set<IClaimsManagerClaimResultListenerAPI> listeners) {
		super();
		this.listeners = listeners;
	}

	@Override
	public void register(@Nonnull IClaimsManagerClaimResultListenerAPI listener) {
		listeners.add(listener);
	}

	public void onClaimResult(AreaClaimResult result){
		for(IClaimsManagerClaimResultListenerAPI listener : listeners)
			listener.onClaimResult(result);
	}

	public static final class Builder {

		private Builder() {
		}

		private Builder setDefault() {
			return this;
		}

		public ClaimsManagerClaimResultTracker build() {
			return new ClaimsManagerClaimResultTracker(new HashSet<>());
		}

		public static Builder begin() {
			return new Builder().setDefault();
		}

	}

}
