package dev.huskuraft.effortless.api.core.fluid;

import dev.huskuraft.effortless.api.platform.ContentFactory;

public enum Fluids {
    EMPTY,
    FLOWING_WATER,
    WATER,
    FLOWING_LAVA,
    LAVA,
    ;

    public Fluid fluid() {
        return ContentFactory.getInstance().getFluid(this);
    }

}
