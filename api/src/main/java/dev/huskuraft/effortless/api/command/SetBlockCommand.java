package dev.huskuraft.effortless.api.command;

import java.util.Locale;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;

public record SetBlockCommand(
        BlockState blockState,
        BlockPosition blockPosition,
        Mode mode
) implements Command {

    public static final String COMMAND = "setblock";

    @Override
    public String build() {
        return "%s %d %d %d %s %s".formatted(
                COMMAND,
                blockPosition.x(),
                blockPosition.y(),
                blockPosition.z(),
                blockState.getString(),
                mode.name().toLowerCase(Locale.ROOT)
        );
    }

    public enum Mode {
        DESTROY,
        KEEP,
        REPLACE
    }

}
