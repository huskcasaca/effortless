package xaero.pac.common.server.data;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import xaero.pac.OpenPartiesAndClaims;

import javax.annotation.Nonnull;

public abstract class ServerDataReloadListener implements ResourceManagerReloadListener {

	@Override
	public void onResourceManagerReload(@Nonnull ResourceManager manager) {
		OpenPartiesAndClaims.INSTANCE.getCommonEvents().onServerDataReload(manager);
	}

}
