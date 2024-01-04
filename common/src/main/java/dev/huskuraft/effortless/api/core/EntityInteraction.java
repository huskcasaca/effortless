package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.math.Vector3d;

public class EntityInteraction extends Interaction {

    // FIXME: 15/10/23
    private final Player entity;

    public EntityInteraction(Player entity) {
        this(entity.getPosition(), entity);
    }

    public EntityInteraction(Vector3d vector, Player entity) {
        super(vector);
        this.entity = entity;
    }

    public Player getEntity() {
        return this.entity;
    }

    public Target getTarget() {
        return Target.ENTITY;
    }
}
