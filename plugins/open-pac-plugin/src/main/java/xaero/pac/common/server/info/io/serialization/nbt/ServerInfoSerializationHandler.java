package xaero.pac.common.server.info.io.serialization.nbt;

import net.minecraft.nbt.CompoundTag;
import xaero.pac.common.server.info.ServerInfo;
import xaero.pac.common.server.info.ServerInfoHolder;
import xaero.pac.common.server.io.serialization.SerializationHandler;

public class ServerInfoSerializationHandler extends SerializationHandler<CompoundTag, Object, ServerInfo, ServerInfoHolder> {

	@Override
	public CompoundTag serialize(ServerInfo object) {
		CompoundTag tag = new CompoundTag();
		tag.putLong("totalUseTime", object.getTotalUseTime());
		tag.putInt("version", ServerInfo.CURRENT_VERSION);
		return tag;
	}

	@Override
	public ServerInfo deserialize(Object id, ServerInfoHolder manager, CompoundTag serializedData) {
		long useTime = serializedData.getLong("totalUseTime");
		int loadedVersion = serializedData.getInt("version");
		return new ServerInfo(useTime, loadedVersion);
	}

}
