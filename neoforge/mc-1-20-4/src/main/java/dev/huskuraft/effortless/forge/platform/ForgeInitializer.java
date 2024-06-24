package dev.huskuraft.effortless.forge.platform;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Effortless.MOD_ID)
public class ForgeInitializer {

    public ForgeInitializer() {
        Entrance.getInstance();

        if (FMLEnvironment.dist.isClient()) {
            ClientEntrance.getInstance();
        }
    }

}
