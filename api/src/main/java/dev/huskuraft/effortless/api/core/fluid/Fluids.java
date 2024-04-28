package dev.huskuraft.effortless.api.core.fluid;

import dev.huskuraft.effortless.api.platform.ContentFactory;

public enum Fluids implements Fluid {
    EMPTY,
    FLOWING_WATER,
    WATER,
    FLOWING_LAVA,
    LAVA,
    ;

    @Override
    public Object referenceValue() {
        return ContentFactory.getInstance().getFluid(this).referenceValue();
    }
}
