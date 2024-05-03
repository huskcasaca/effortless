package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.text.Text;

public interface Option {

    String getName();

    String getCategory();

    default String getNameKey() {
        return "effortless.action.%s".formatted(getName());
    }

    default String getCategoryKey() {
        return "effortless.option.%s".formatted(getCategory());
    }

    default String getTooltipKey() {
        return "effortless.action.%s.tooltip".formatted(getName());
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
