package dev.huskuraft.effortless.forge;

import dev.huskuraft.effortless.Effortless;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(Effortless.MOD_ID)
public class ForgeEntrance {

    private static final String CLASS_NAME = "ForgeEffortless";
    private static final String CLIENT_CLASS_NAME = "ForgeEffortlessClient";

    public ForgeEntrance() {
        var originalClassPath = "dev.huskuraft.effortless.forge.";
        var relocatedClassPath = "dev.huskuraft.effortless.forge.mc_" + FMLLoader.versionInfo().mcVersion().replace(".", "_") + ".";

        try {
            Class.forName(originalClassPath + CLASS_NAME).getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {
        }
        try {
            Class.forName(relocatedClassPath + CLASS_NAME).getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            try {
                Class.forName(originalClassPath + CLIENT_CLASS_NAME).getDeclaredConstructor().newInstance();
            } catch (Exception ignored) {
            }
            try {
                Class.forName(relocatedClassPath + CLIENT_CLASS_NAME).getDeclaredConstructor().newInstance();
            } catch (Exception ignored) {
            }
        });
    }

}
