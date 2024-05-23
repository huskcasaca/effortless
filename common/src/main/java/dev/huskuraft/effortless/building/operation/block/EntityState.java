package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.Entity;
import dev.huskuraft.effortless.api.math.Vector3d;

public record EntityState(
        Vector3d position,
        float rotationX,
        float rotationY
) {

    public static EntityState get(Entity entity) {
        return new EntityState(entity.getPosition(), entity.getXRot(), entity.getYRot());
    }

    public static void set(Entity entity, EntityState entityState) {
        if (entityState == null) {
            Effortless.LOGGER.warn("Attempted to set entity data to null");
            return;
        }
        entity.setPosition(entityState.position());
        entity.setXRot(entityState.rotationX());
        entity.setYRot(entityState.rotationY());
    }
}
