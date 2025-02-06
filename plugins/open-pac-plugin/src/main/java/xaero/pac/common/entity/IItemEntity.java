package xaero.pac.common.entity;

import java.util.UUID;

public interface IItemEntity {

	UUID getXaero_OPAC_throwerAccessor();
	void setXaero_OPAC_throwerAccessor(UUID throwerAccessor);
	UUID getXaero_OPAC_thrower();
	UUID getXaero_OPAC_target();

}
