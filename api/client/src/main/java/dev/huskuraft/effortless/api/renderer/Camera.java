package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.math.Quaternionf;
import dev.huskuraft.effortless.api.math.Vector3d;

public interface Camera {

    Vector3d position();

    Quaternionf rotation();

    float eyeHeight();

}
