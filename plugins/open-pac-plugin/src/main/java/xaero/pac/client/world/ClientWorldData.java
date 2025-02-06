package xaero.pac.client.world;

public class ClientWorldData implements IClientWorldData {

	private boolean serverHasMod;
	private boolean serverHasClaimsEnabled;
	private boolean serverHasPartiesEnabled;

	private ClientWorldData() {
		super();
	}

	@Override
	public boolean serverHasMod() {
		return serverHasMod;
	}

	@Override
	public boolean serverHasClaimsEnabled() {
		return serverHasClaimsEnabled;
	}

	@Override
	public boolean serverHasPartiesEnabled() {
		return serverHasPartiesEnabled;
	}

	@Override
	public void setServerHasMod(boolean serverHasMod) {
		this.serverHasMod = serverHasMod;
	}

	@Override
	public void setServerHasClaimsEnabled(boolean serverHasClaimsEnabled) {
		this.serverHasClaimsEnabled = serverHasClaimsEnabled;
	}

	@Override
	public void setServerHasPartiesEnabled(boolean serverHasPartiesEnabled) {
		this.serverHasPartiesEnabled = serverHasPartiesEnabled;
	}

	public static final class Builder {

		private Builder() {
		}

		public ClientWorldData build() {
			return new ClientWorldData();
		}

		public static final Builder begin() {
			return new Builder();
		}

	}

}
