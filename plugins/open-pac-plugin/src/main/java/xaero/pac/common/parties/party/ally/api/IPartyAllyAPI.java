package xaero.pac.common.parties.party.ally.api;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * An ally of a party
 */
public interface IPartyAllyAPI {

	/**
	 * Gets the UUID of the ally party.
	 *
	 * @return the UUID of the ally, not null
	 */
	@Nonnull
	public UUID getPartyId();

}
