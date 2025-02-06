package xaero.pac.common.parties.party;

import net.minecraft.resources.ResourceLocation;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

import java.util.UUID;

public interface IPartyMemberDynamicInfoSyncable extends IPartyMemberDynamicInfoSyncableAPI {

	//internal api

	public void update(ResourceLocation dimension, double x, double y, double z);
	public boolean isActive();
	public IPartyMemberDynamicInfoSyncable getRemover();
	public UUID getPartyId();

}
