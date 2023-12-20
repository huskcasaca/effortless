package dev.huskuraft.effortless.renderer;

import dev.huskuraft.effortless.math.Vector3d;
import org.joml.Quaternionf;

public interface Camera {

    Vector3d position();

    Quaternionf rotation();

    float eyeHeight();

}
