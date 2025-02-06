package xaero.pac.common.server.io.serialization.nbt;

import net.minecraft.nbt.CompoundTag;
import xaero.pac.common.server.io.serialization.SerializedDataFileIOConverter;

public abstract class NBTConverter
<
	S,
	I
> extends SerializedDataFileIOConverter<S, CompoundTag, I>{

	@Override
	public abstract S convert(I id, CompoundTag nbt);
	@Override
	public abstract CompoundTag convert(S data);

}
