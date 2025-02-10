package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.stream.Stream;

import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockPosition;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.BuildMode;
import dev.huskuraft.effortless.building.structure.builder.BlockStructure;

public record Dome(

) implements BlockStructure {

    public BlockInteraction trace(Player player, Context context, int index) {
        return null;
    }

    public Stream<BlockPosition> collect(Context context, int index) {
        return null;
    }

    @Override
    public int traceSize(Context context) {
        return 3;
    }

    @Override
    public BuildMode getMode() {
        return null;
    }
}
