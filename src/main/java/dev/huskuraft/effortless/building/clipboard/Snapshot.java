package dev.huskuraft.effortless.building.clipboard;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.universal.api.math.Vector3d;
import dev.huskuraft.universal.api.math.Vector3i;
import dev.huskuraft.effortless.building.operation.ItemStackUtils;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public record Snapshot(
        String name, long createdTimestamp, List<BlockData> blockData
) {

    public static Snapshot EMPTY = new Snapshot("", 0, List.of());

    public boolean isEmpty() {
        return blockData.isEmpty();
    }

    public List<ItemStack> getItems() {
        return ItemStackUtils.flattenStack(blockData().stream().map(BlockData::blockState).filter(Objects::nonNull).map(blockState -> blockState.getItem().getDefaultStack().withCount(blockState.getRequiredItemCount())).collect(Collectors.toList()));
    }

    public int volume() {
        return box().volume();
    }

    public Vector3i box() {
        if (blockData().isEmpty()) {
            return Vector3i.ZERO;
        }
        return new Vector3i(
                blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().x()).max().getAsInt() - blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().x()).min().getAsInt() + 1,
                blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().y()).max().getAsInt() - blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().y()).min().getAsInt() + 1,
                blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().z()).max().getAsInt() - blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().z()).min().getAsInt() + 1
        );
    }

    public Vector3d getCenter() {
        if (blockData().isEmpty()) {
            return Vector3d.ZERO;
        }

        var maxX = blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().x()).max().getAsInt();
        var maxY = blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().y()).max().getAsInt();
        var maxZ = blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().z()).max().getAsInt();
        var minX = blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().x()).min().getAsInt();
        var minY = blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().y()).min().getAsInt();
        var minZ = blockData().stream().mapToInt(blockSnapshot -> blockSnapshot.blockPosition().z()).min().getAsInt();

        return new Vector3d(
                minX + (maxX - minX) / 2f + 0.5f,
                minY + (maxY - minY) / 2f + 0.5f,
                minZ + (maxZ - minZ) / 2f + 0.5f
        );

    }

    public Snapshot withBlockData(List<BlockData> blockData) {
        return new Snapshot(name, createdTimestamp, blockData);
    }

    public Snapshot update(SnapshotTransform action) {
        switch (action) {
            case ROTATE_X -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.rotate(RotateContext.CLOCKWISE_X_90)).collect(Collectors.toList()));
            }
            case ROTATE_Y -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.rotate(RotateContext.CLOCKWISE_Y_90)).collect(Collectors.toList()));
            }
            case ROTATE_Z -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.rotate(RotateContext.CLOCKWISE_Z_90)).collect(Collectors.toList()));
            }
            case MIRROR_X -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.mirror(MirrorContext.MIRROR_X)).collect(Collectors.toList()));
            }
            case MIRROR_Y -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.mirror(MirrorContext.MIRROR_Y)).collect(Collectors.toList()));
            }
            case MIRROR_Z -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.mirror(MirrorContext.MIRROR_Z)).collect(Collectors.toList()));
            }
            case INCREASE_X -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(1, 0, 0))).collect(Collectors.toList()));
            }
            case INCREASE_Y -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, 1, 0))).collect(Collectors.toList()));
            }
            case INCREASE_Z -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, 0, 1))).collect(Collectors.toList()));
            }
            case DECREASE_X -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(-1, 0, 0))).collect(Collectors.toList()));
            }
            case DECREASE_Y -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, -1, 0))).collect(Collectors.toList()));
            }
            case DECREASE_Z -> {
                return withBlockData(blockData().stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, 0, -1))).collect(Collectors.toList()));
            }
        }
        return this;
    }
}
