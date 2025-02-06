package xaero.pac.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public interface IForgeConfigHelper {

	public void registerServerConfig(ModConfigSpec spec);
	public void registerClientConfig(ModConfigSpec spec);
	public void registerCommonConfig(ModConfigSpec spec);

}
