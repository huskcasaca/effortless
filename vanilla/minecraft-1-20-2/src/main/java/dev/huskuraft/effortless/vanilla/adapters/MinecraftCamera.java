package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.math.Quaternionf;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.renderer.Camera;

public class MinecraftCamera implements Camera {

    private final net.minecraft.client.Camera reference;

    MinecraftCamera(net.minecraft.client.Camera reference) {
        this.reference = reference;
    }

    public static Camera fromMinecraftCamera(net.minecraft.client.Camera camera) {
        return new MinecraftCamera(camera);
    }

    @Override
    public Vector3d position() {
        return MinecraftPlayer.fromMinecraftVector3d(reference.getPosition());
    }

    @Override
    public Quaternionf rotation() {
        return MinecraftPrimitives.fromMinecraftQuaternion(reference.rotation());
    }

    @Override
    public float eyeHeight() {
        return reference.getEntity().getEyeHeight();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftCamera camera && reference.equals(camera.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
