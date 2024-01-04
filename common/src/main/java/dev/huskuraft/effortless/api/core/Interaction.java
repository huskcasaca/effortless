package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.math.Vector3d;

public abstract class Interaction {
    protected final Vector3d position;

    protected Interaction(Vector3d vector) {
        this.position = vector;
    }

    public abstract Target getTarget();

    public InteractionHand getHand() {
        return InteractionHand.MAIN;
    }

    public Vector3d getPosition() {
        return this.position;
    }

    public enum Target {
        MISS,
        BLOCK,
        ENTITY
    }
}
