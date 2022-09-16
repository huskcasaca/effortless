package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.core.BlockPosition;

import java.util.stream.Stream;

public interface BlockPosCollector {

    Stream<BlockPosition> collect(Context context);

}
