package dev.huskuraft.effortless.building.operation;

import dev.huskuraft.effortless.building.pattern.MoveContext;

public interface Movable<O> extends Trait {

    O move(MoveContext moveContext);

//    O offset(Vec3 vec3);

//    O absolute(Vec3i vec3i);

//    O absolute(Vec3 vec3i);

}
