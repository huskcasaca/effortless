package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.lang.Lang;
import dev.huskuraft.effortless.api.text.Text;

public interface Option {

    String getName();

    String getCategory();

    default String getNameKey() {
        return Lang.asKey("action.%s".formatted(getName()));
    }

    default String getCategoryKey() {
        return Lang.asKey("option.%s".formatted(getCategory()));
    }

    default String getTooltipKey() {
        return Lang.asKey("action.%s.tooltip".formatted(getName()));
    }

    default Text getNameText() {
        return Text.translate(getNameKey());
    }

    default Text getCategoryText() {
        return Text.translate(getCategoryKey());
    }

    default Text getTooltipText() {
        return Text.empty();
    }

    default ResourceLocation getIcon() {
        return ResourceLocation.of(Effortless.MOD_ID, "textures/option/%s.png".formatted(getName()));
    }

}
