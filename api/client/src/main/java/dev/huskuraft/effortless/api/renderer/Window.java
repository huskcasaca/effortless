package dev.huskuraft.effortless.api.renderer;

import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Window extends PlatformReference {

    int getWidth();

    int getHeight();

    int getGuiScaledWidth();

    int getGuiScaledHeight();

    double getGuiScaledFactor();

}
