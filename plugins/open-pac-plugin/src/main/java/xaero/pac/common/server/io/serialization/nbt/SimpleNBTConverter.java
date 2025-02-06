package xaero.pac.common.server.io.serialization.nbt;

import net.minecraft.nbt.CompoundTag;

public final class SimpleNBTConverter<I> extends NBTConverter<CompoundTag, I>{

	@Override
	public CompoundTag convert(CompoundTag data) {
		return data;
	}

	@Override
	public CompoundTag convert(I id, CompoundTag fileContent) {
		return fileContent;
	}

}
