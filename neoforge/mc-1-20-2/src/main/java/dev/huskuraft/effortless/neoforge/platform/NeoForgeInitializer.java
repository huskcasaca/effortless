package dev.huskuraft.effortless.neoforge.platform;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.Entrance;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Effortless.MOD_ID)
public class NeoForgeInitializer {

    public static IEventBus EVENT_BUS;

    public NeoForgeInitializer(IEventBus eventBus) {
        EVENT_BUS = eventBus;

        Entrance.getInstance();

        if (FMLEnvironment.dist.isClient()) {
            ClientEntrance.getInstance();
        }
    }

}
