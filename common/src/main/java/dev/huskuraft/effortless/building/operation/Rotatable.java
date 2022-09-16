package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.building.pattern.RevolveContext;

public interface Rotatable<O> extends Trait {

    O revolve(RevolveContext revolveContext);

}
