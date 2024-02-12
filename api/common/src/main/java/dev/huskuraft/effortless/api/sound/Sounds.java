package dev.huskuraft.effortless.api.sound;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.platform.ContentFactory;

public enum Sounds implements Sound {
    UI_BUTTON_CLICK,
    UI_TOAST_IN,
    UI_TOAST_OUT,
    ;

    @Override
    public Object referenceValue() {
        return getSound(this).referenceValue();
    }

    @Override
    public ResourceLocation getId() {
        return getSound(this).getId();
    }

    public Sound getSound(Sounds sounds) {
        return ContentFactory.getInstance().getSound(this);
    }
}
