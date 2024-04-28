package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.building.pattern.RotateContext;

public interface Rotatable<O> extends Trait {

    O rotate(RotateContext rotateContext);

}
