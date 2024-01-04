package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.api.core.Resource;
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

    default Resource getIcon() {
        return Resource.of("textures/option/%s.png".formatted(getName()));
    }

}
