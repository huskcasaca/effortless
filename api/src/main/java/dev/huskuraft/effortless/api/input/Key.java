package dev.huskuraft.effortless.api.input;

import dev.huskuraft.effortless.api.platform.ClientEntrance;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Key extends PlatformReference {

    String getName();

    int getValue();

    default boolean isDown() {
        return ClientEntrance.getInstance().getClient().getWindow().isKeyDown(getValue());
    }

}
