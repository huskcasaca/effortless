package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Effortless.MOD_ID)
public class ForgeInitializer {

    public ForgeInitializer() {
        Entrance.getInstance();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ClientEntrance.getInstance();
        });
    }

}
