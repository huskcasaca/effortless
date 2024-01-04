package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.math.MathUtils;

import java.util.Iterator;
import java.util.stream.BaseStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class BlockPositionStream implements BaseStream<BlockPosition, BlockPositionStream> {

    private static BlockPosition transform(BlockPosition original, Axis first, Axis second) {
        assert first != second;
        var third = Axis.values()[3 - first.ordinal() - second.ordinal()];
        return new BlockPosition(original.get(first), original.get(second), original.get(third));
    }

    private static BlockPosition recover(BlockPosition original, Axis first, Axis second) {
        var third = Axis.values()[3 - first.ordinal() - second.ordinal()];
        var arr = new int[3];
        arr[first.ordinal()] = original.x();
        arr[second.ordinal()] = original.y();
        arr[third.ordinal()] = original.z();
        return new BlockPosition(arr[0], arr[1], arr[2]);
    }

    static Stream<BlockPosition> of(BlockPosition position) {
        return Stream.of(position);
    }

    static Stream<BlockPosition> between(BlockPosition start, BlockPosition end) {
        return between(start, end, Axis.X, Axis.Y);
    }

    static Stream<BlockPosition> between(BlockPosition start, BlockPosition end, Axis first, Axis second) {
        return StreamSupport.stream(betweenClosed(transform(start, first, second), transform(end, first, second)).spliterator(), false).map(pos -> recover(pos, first, second));
    }

    static Stream<BlockPosition> between(int x1, int y1, int z1, int x2, int y2, int z2) {
        return between(new BlockPosition(x1, y1, z1), new BlockPosition(x2, y2, z2));
    }

    static Stream<BlockPosition> between(int x1, int y1, int z1, int x2, int y2, int z2, Axis first, Axis second) {
        return between(new BlockPosition(x1, y1, z1), new BlockPosition(x2, y2, z2), first, second);
    }

    private static Iterable<BlockPosition> betweenClosed(BlockPosition start, BlockPosition end) {
        return betweenClosed(start.x(), start.y(), start.z(), end.x(), end.y(), end.z());
    }

    private static Iterable<BlockPosition> betweenClosed(int x1, int y1, int z1, int x2, int y2, int z2) {
        return () -> new Iterator<BlockPosition>() {


            private final int x = MathUtils.abs(x2 - x1) + 1;
            private final int y = MathUtils.abs(y2 - y1) + 1;
            private final int z = MathUtils.abs(z2 - z1) + 1;
            private final int size = x * y * z;

            //            private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return this.index != size;
            }

            @Override
            public BlockPosition next() {
                if (this.index == size) {
                    return null;
                }
//                    int ix = (index) % x;
//                    int jx = (index - ix) / x % y;
//                    int kx = (index - ix - jx * x) / x / y % z;
                int ix = index % x, jx = index / x % y, kx = index / (x * y) % z;
                ++index;
                return new BlockPosition(
                        x1 + ix * (x2 > x1 ? 1 : -1),
                        y1 + jx * (y2 > y1 ? 1 : -1),
                        z1 + kx * (z2 > z1 ? 1 : -1)
                );

//                    index = x + y * xstep + z * xstep * ystep;
            }
        };
    }
}