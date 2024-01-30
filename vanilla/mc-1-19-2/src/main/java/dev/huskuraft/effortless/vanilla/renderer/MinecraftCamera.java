package dev.huskuraft.effortless.vanilla.renderer;

import dev.huskuraft.effortless.api.math.Quaternionf;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.renderer.Camera;
import dev.huskuraft.effortless.vanilla.core.MinecraftConvertor;

public class MinecraftCamera implements Camera {

    private final net.minecraft.client.Camera reference;

    public MinecraftCamera(net.minecraft.client.Camera reference) {
        this.reference = reference;
    }

    @Override
    public Vector3d position() {
        return MinecraftConvertor.fromPlatformVector3d(reference.getPosition());
    }

    @Override
    public Quaternionf rotation() {
        return MinecraftConvertor.fromPlatformQuaternion(reference.rotation());
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
