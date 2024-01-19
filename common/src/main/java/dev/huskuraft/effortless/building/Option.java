package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.text.Text;

public interface Option {

    String getName();

    String getCategory();

    default Text getDisplayName() {
        return Text.translate("effortless.action.%s".formatted(getName()));
    }

    default Text getDisplayCategory() {
        return Text.translate("effortless.option.%s".formatted(getCategory()));
    }

    default ResourceLocation getIcon() {
        return ResourceLocation.of(Effortless.MOD_ID, "textures/option/%s.png".formatted(getName()));
    }

}
