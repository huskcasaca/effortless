package xaero.pac.common.capability;

import org.jetbrains.annotations.Nullable;
import xaero.pac.common.capability.api.ICapabilityHelperAPI;

import javax.annotation.Nonnull;

public class CapabilityHelper implements ICapabilityHelperAPI {

	@Nullable
	@Override
	public <T, C extends ICapability<T>> T getCapability(@Nonnull Object object, @Nonnull C capability) {
		//only supports ClientLevel instances as of writing this
		//can be extended to other classes with mixins implementing IFabricCapableObject
		if(!(object instanceof ICapableObject capableObject))
			return null;
		ICapabilityProvider provider = capableObject.getXaero_OPAC_CapabilityProvider();
		if(provider == null)
			capableObject.setXaero_OPAC_CapabilityProvider(provider = ((ICapabilityType<?>)capability).createProvider(capableObject));
		return provider.getCapability(capability);
	}

}
