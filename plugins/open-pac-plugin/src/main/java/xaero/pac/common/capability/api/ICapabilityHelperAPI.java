package xaero.pac.common.capability.api;

import xaero.pac.client.world.capability.api.ClientWorldCapabilityTypes;
import xaero.pac.common.capability.ICapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Helper API for getting capability values of objects
 */
public interface ICapabilityHelperAPI {

	/**
	 * Gets the capability value of a specified type for a specified object.
	 * <p>
	 * Client world capability types can be found in {@link ClientWorldCapabilityTypes}
	 *
	 * @param object  the object that the capability is attached to, not null
	 * @param capability  the capability type, not null
	 * @return the capability value attached to the object,
	 *         null when a capability of the specified type isn't attached to the object
	 * @param <T>  the type of the capability value
	 * @param <C>  the type of capability types
	 */
	@Nullable
	public <T, C extends ICapability<T>> T getCapability(@Nonnull Object object, @Nonnull C capability);

}
