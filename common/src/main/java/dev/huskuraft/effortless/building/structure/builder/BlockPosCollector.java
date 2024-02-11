package dev.huskuraft.effortless.building.structure.builder;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.building.Context;

public interface BlockPosCollector {

    Stream<BlockPosition> collect(Context context);

}
