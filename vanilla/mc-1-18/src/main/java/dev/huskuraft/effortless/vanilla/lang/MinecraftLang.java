package dev.huskuraft.effortless.vanilla.lang;

import com.google.auto.service.AutoService;

import dev.huskuraft.effortless.api.lang.Lang;
import net.minecraft.locale.Language;

@AutoService(Lang.class)
public class MinecraftLang implements Lang {

    @Override
    public String getOrDefault(String id) {
        return Language.getInstance().getOrDefault(id);
    }

    @Override
    public boolean has(String id) {
        return Language.getInstance().has(id);
    }

    @Override
    public boolean isDefaultRightToLeft() {
        return Language.getInstance().isDefaultRightToLeft();
    }
}
