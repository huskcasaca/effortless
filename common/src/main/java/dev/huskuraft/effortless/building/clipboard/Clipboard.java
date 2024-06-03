package dev.huskuraft.effortless.building.clipboard;

import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.math.Vector3i;
import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.building.pattern.MirrorContext;
import dev.huskuraft.effortless.building.pattern.MoveContext;
import dev.huskuraft.effortless.building.pattern.RotateContext;

public record Clipboard(
        boolean enabled,
        List<BlockSnapshot> blockSnapshots
) implements Option  {
    public static Clipboard DISABLED = new Clipboard(false, List.of());

    public static Clipboard of(boolean enabled, List<BlockSnapshot> blockSnapshots) {
        return new Clipboard(enabled, blockSnapshots);
    }

    public Clipboard withEnabled(boolean enabled) {
        return new Clipboard(enabled, blockSnapshots);
    }

    public Clipboard withBlockSnapshots(List<BlockSnapshot> blockSnapshots) {
        return new Clipboard(enabled, blockSnapshots);
    }

    public boolean isEmpty() {
        return blockSnapshots.isEmpty();
    }

    public int volume() {
        return box().volume();
    }

    public Vector3i box() {
        if (blockSnapshots.isEmpty()) {
            return Vector3i.ZERO;
        }
        return new Vector3i(
                blockSnapshots.stream().mapToInt(blockSnapshot -> blockSnapshot.relativePosition().x()).max().getAsInt() - blockSnapshots.stream().mapToInt(blockSnapshot -> blockSnapshot.relativePosition().x()).min().getAsInt() + 1,
                blockSnapshots.stream().mapToInt(blockSnapshot -> blockSnapshot.relativePosition().y()).max().getAsInt() - blockSnapshots.stream().mapToInt(blockSnapshot -> blockSnapshot.relativePosition().y()).min().getAsInt() + 1,
                blockSnapshots.stream().mapToInt(blockSnapshot -> blockSnapshot.relativePosition().z()).max().getAsInt() - blockSnapshots.stream().mapToInt(blockSnapshot -> blockSnapshot.relativePosition().z()).min().getAsInt() + 1
        );
    }

    public boolean copyAir() {
        return false;
    }

    public Clipboard update(ClipboardAction action) {
        switch (action) {
            case ROTATE_X -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.rotate(RotateContext.CLOCKWISE_X_90)).collect(Collectors.toList()));
            }
            case ROTATE_Y -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.rotate(RotateContext.CLOCKWISE_Y_90)).collect(Collectors.toList()));
            }
            case ROTATE_Z -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.rotate(RotateContext.CLOCKWISE_Z_90)).collect(Collectors.toList()));
            }
            case MIRROR_X -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.mirror(MirrorContext.MIRROR_X)).collect(Collectors.toList()));
            }
            case MIRROR_Y -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.mirror(MirrorContext.MIRROR_Y)).collect(Collectors.toList()));
            }
            case MIRROR_Z -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.mirror(MirrorContext.MIRROR_Z)).collect(Collectors.toList()));
            }
            case INCREASE_X -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(1, 0, 0))).collect(Collectors.toList()));
            }
            case INCREASE_Y -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, 1, 0))).collect(Collectors.toList()));
            }
            case INCREASE_Z -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, 0, 1))).collect(Collectors.toList()));
            }
            case DECREASE_X -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(-1, 0, 0))).collect(Collectors.toList()));
            }
            case DECREASE_Y -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, -1, 0))).collect(Collectors.toList()));
            }
            case DECREASE_Z -> {
                return withBlockSnapshots(blockSnapshots.stream().map(blockSnapshot -> blockSnapshot.move(MoveContext.relative(0, 0, -1))).collect(Collectors.toList()));
            }
        }
        return this;
    }

    @Override
    public String getName() {
        if (enabled) {
            return "clipboard_disabled";
        } else {
            return "clipboard_enabled";
        }
    }

    @Override
    public String getCategory() {
        return "clipboard";
    }
}
