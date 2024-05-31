package dev.huskuraft.effortless.building.clipboard;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;

public record BlockSnapshot(
        BlockPosition relativePosition,
        BlockState blockState
) {

}
