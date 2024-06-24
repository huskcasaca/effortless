package dev.huskuraft.effortless.forge.platform;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.neoforge.fml.DistExecutor;
import net.neoforged.neoforge.fml.common.Mod;

@Mod(Effortless.MOD_ID)
public class ForgeInitializer {

    public ForgeInitializer() {
        Entrance.getInstance();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientEntrance.getInstance();
        });
    }

}
