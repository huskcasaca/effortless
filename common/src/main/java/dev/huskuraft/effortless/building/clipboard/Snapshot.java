package dev.huskuraft.effortless.building.clipboard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.math.BoundingBox3d;
import dev.huskuraft.effortless.api.math.Vector3d;
import dev.huskuraft.effortless.api.math.Vector3f;
import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public record Snapshot(
        List<BlockData> blockData
) {

    public static Snapshot EMPTY = new Snapshot(List.of());

    public boolean isEmpty() {
        return blockData.isEmpty();
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

    public Vector3d getVisualCenter() {
        if (blockData().isEmpty()) {
            return Vector3d.ZERO;
        }
        var center = getCenter();
        var maxYBlockPosition = blockData().stream().max(Comparator.comparingDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()) * 0.57735026919f + blockData.blockPosition().getCenter().sub(center).y())).map(BlockData::blockPosition).map(BlockPosition::getCenter).get();
        var minYBlockPosition = blockData().stream().min(Comparator.comparingDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()) * 0.57735026919f + blockData.blockPosition().getCenter().sub(center).y())).map(BlockData::blockPosition).map(BlockPosition::getCenter).get();
        var maxXBlockPosition = blockData().stream().max(Comparator.comparingDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()))).map(BlockData::blockPosition).map(BlockPosition::getCenter).get();
        var minXBlockPosition = blockData().stream().min(Comparator.comparingDouble(blockData -> (blockData.blockPosition().getCenter().sub(center).x() - blockData.blockPosition().getCenter().sub(center).z()))).map(BlockData::blockPosition).map(BlockPosition::getCenter).get();
        var xyPos = BoundingBox3d.of(maxXBlockPosition, minXBlockPosition);
        var yPos = BoundingBox3d.of(maxYBlockPosition, minYBlockPosition);

        return new Vector3d(
                xyPos.getCenter().x(),
                yPos.getCenter().y(),
                xyPos.getCenter().z()
        );

    }

    public Snapshot withBlockData(List<BlockData> blockData) {
        return new Snapshot(blockData);
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
