package xaero.pac.client.parties.party;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ClientPartyAllyInfo implements IClientPartyAllyInfo {

	private final UUID allyId;
	private final String allyName;
	private final String allyDefaultName;

	public ClientPartyAllyInfo(UUID allyId, String allyName, String allyDefaultName) {
		super();
		this.allyId = allyId;
		this.allyName = allyName;
		this.allyDefaultName = allyDefaultName;
	}

	@Nonnull
	@Override
	public UUID getAllyId() {
		return allyId;
	}

	@Nonnull
	@Override
	public String getAllyName() {
		return allyName;
	}

	@Nonnull
	@Override
	public String getAllyDefaultName() {
		return allyDefaultName;
	}

}
