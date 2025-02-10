package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.building.pattern.MirrorContext;

public interface Mirrorable<O> extends Trait {

    O mirror(MirrorContext mirrorContext);

}
