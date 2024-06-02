package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.input.Key;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.input.KeyCodes;

public enum EffortlessKeys implements Key {
    SETTINGS("settings", KeyCodes.KEY_UNKNOWN),
    BUILD_MODE_RADIAL("build_mode_radial", KeyCodes.KEY_LEFT_ALT),

    UNDO("undo", KeyCodes.KEY_LEFT_BRACKET),
    REDO("redo", KeyCodes.KEY_RIGHT_BRACKET),
    TOGGLE_REPLACE("toggle_replace", KeyCodes.KEY_UNKNOWN),
    PASSIVE_BUILD_MODIFIER("passive_build_modifier", KeyCodes.KEY_UNKNOWN),

    MOVE_LEFT("move_left", KeyCodes.KEY_LEFT, Category.CLIPBOARD),
    MOVE_RIGHT("move_right", KeyCodes.KEY_RIGHT, Category.CLIPBOARD),
    MOVE_FORWARD("move_forward", KeyCodes.KEY_UP, Category.CLIPBOARD),
    MOVE_BACKWARD("move_backward", KeyCodes.KEY_DOWN, Category.CLIPBOARD),
    MOVE_UP("move_up", KeyCodes.KEY_PAGE_UP, Category.CLIPBOARD),
    MOVE_DOWN("move_down", KeyCodes.KEY_PAGE_DOWN, Category.CLIPBOARD),

//    ROTATE_XYZ("rotate_xyz", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),
//    MIRROR_XYZ("mirror_xyz", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),

    ROTATE_X("rotate_x", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),
    ROTATE_Y("rotate_y", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),
    ROTATE_Z("rotate_z", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),

    MIRROR_X("mirror_x", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),
    MIRROR_Y("mirror_y", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),
    MIRROR_Z("mirror_z", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),

    ;

    private final KeyBinding keyBinding;

    EffortlessKeys(String description, KeyCodes defaultKey) {
        this(description, defaultKey, Category.DEFAULT);
    }

    EffortlessKeys(String description, KeyCodes defaultKey, Category category) {

        this.keyBinding = switch (category) {
            case DEFAULT ->  KeyBinding.of("key.effortless.%s.desc".formatted(description), "key.effortless.category.default", defaultKey.value());
            case CLIPBOARD ->  KeyBinding.of("key.effortless.%s.desc".formatted(description), "key.effortless.category.clipboard", defaultKey.value());
        };
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }

    public enum Category {
        DEFAULT,
        CLIPBOARD
    }
}
