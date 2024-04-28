package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.ContentFactory;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public enum StatTypes {

    ITEM_USED,
    ITEM_BROKEN,
    ITEM_PICKED_UP,
    ITEM_DROPPED,
    ;

    public Stat<?> get(PlatformReference value) {
        return ContentFactory.getInstance().getStatType(this).get(value);
    }

}
