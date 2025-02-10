package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.universal.api.core.ResourceLocation;
import dev.huskuraft.universal.api.lang.Lang;
import dev.huskuraft.universal.api.text.Text;

public interface Option {

    String getName();

    String getCategory();

    default String getNameKey() {
        return Lang.asKey(Effortless.MOD_ID, "action.%s".formatted(getName()));
    }

    default String getCategoryKey() {
        return Lang.asKey(Effortless.MOD_ID, "option.%s".formatted(getCategory()));
    }

    default String getTooltipKey() {
        return Lang.asKey(Effortless.MOD_ID, "action.%s.tooltip".formatted(getName()));
    }

    default Text getNameText() {
        return Text.translate(getNameKey());
    }

    default Text getCategoryText() {
        return Text.translate(getCategoryKey());
    }

    default Text getTooltipText() {
        if (Lang.hasKey(getTooltipKey())) {
            return Text.translate(getTooltipKey());
        } else {
            return Text.empty();
        }
    }

    default ResourceLocation getIcon() {
        return ResourceLocation.of(Effortless.MOD_ID, "textures/option/%s.png".formatted(getName()));
    }

}
