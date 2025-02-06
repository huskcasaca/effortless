package xaero.pac.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public interface IEntityAccess {

	public CompoundTag getPersistentData(Entity entity);

}
