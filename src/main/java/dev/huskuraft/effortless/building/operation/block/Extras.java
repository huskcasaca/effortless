package dev.huskuraft.effortless.building.operation.block;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.universal.api.core.Entity;
import dev.huskuraft.universal.api.math.Vector3d;

public record Extras(
        Vector3d position,
        float rotationX,
        float rotationY
) {

    public static Extras get(Entity entity) {
        return new Extras(entity.getPosition(), entity.getXRot(), entity.getYRot());
    }

    public static void set(Entity entity, Extras extras) {
        if (extras == null) {
            Effortless.LOGGER.warn("Attempted to set entity data to null");
            return;
        }
        entity.setPosition(extras.position());
        entity.setXRot(extras.rotationX());
        entity.setYRot(extras.rotationY());
    }
}
