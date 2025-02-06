package xaero.pac.common.server.io.serialization.nbt;

import net.minecraft.nbt.CompoundTag;

public class SimpleNBTSerializedDataFileIO
<
	I
>
extends NBTSerializedDataFileIO<CompoundTag, I> {


	public SimpleNBTSerializedDataFileIO(){
		super(new SimpleNBTConverter<>());
	}

}
