package xaero.pac.common.capability.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import xaero.pac.common.capability.ICapability;

/**
 * Helper API for getting capability values of objects
 */
public interface ICapabilityHelperAPI {

    /**
     * Gets the capability value of a specified type for a specified object.
     * <p>
     *
     * @param object     the object that the capability is attached to, not null
     * @param capability the capability type, not null
     * @param <T>        the type of the capability value
     * @param <C>        the type of capability types
     * @return the capability value attached to the object,
     * null when a capability of the specified type isn't attached to the object
     */
    @Nullable
    <T, C extends ICapability<T>> T getCapability(@Nonnull Object object, @Nonnull C capability);

}
