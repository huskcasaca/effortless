package dev.huskuraft.effortless;

import dev.huskuraft.universal.api.input.KeyBindingOwner;
import dev.huskuraft.universal.api.input.KeyBinding;
import dev.huskuraft.universal.api.input.KeyCodes;

public enum EffortlessKeys implements KeyBindingOwner {
    // default
    SETTINGS("settings", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),

    BUILD_MODE_RADIAL("build_mode_radial", Category.DEFAULT, KeyCodes.KEY_LEFT_ALT),
    PASSIVE_BUILD_MODIFIER("passive_build_modifier", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),

    UNDO("undo", Category.DEFAULT, KeyCodes.KEY_LEFT_BRACKET),
    REDO("redo", Category.DEFAULT, KeyCodes.KEY_RIGHT_BRACKET),

    TOGGLE_CLIPBOARD("toggle_clipboard", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),
    TOGGLE_PATTERN("toggle_pattern", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),
    TOGGLE_REPLACE("toggle_replace", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),

    EDIT_CLIPBOARD("edit_clipboard", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),
    EDIT_PATTERN("edit_pattern", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),
//    EDIT_REPLACE("edit_replace", Category.DEFAULT, KeyCodes.KEY_UNKNOWN),

    // clipboard
    MOVE_LEFT("move_left", Category.CLIPBOARD, KeyCodes.KEY_LEFT),
    MOVE_RIGHT("move_right", Category.CLIPBOARD, KeyCodes.KEY_RIGHT),
    MOVE_FORWARD("move_forward", Category.CLIPBOARD, KeyCodes.KEY_UP),
    MOVE_BACKWARD("move_backward", Category.CLIPBOARD, KeyCodes.KEY_DOWN),
    MOVE_UP("move_up", Category.CLIPBOARD, KeyCodes.KEY_PAGE_UP),
    MOVE_DOWN("move_down", Category.CLIPBOARD, KeyCodes.KEY_PAGE_DOWN),

//    ROTATE_XYZ("rotate_xyz", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),
//    MIRROR_XYZ("mirror_xyz", KeyCodes.KEY_UNKNOWN, Category.CLIPBOARD),

    ROTATE_X("rotate_x", Category.CLIPBOARD, KeyCodes.KEY_UNKNOWN),
    ROTATE_Y("rotate_y", Category.CLIPBOARD, KeyCodes.KEY_UNKNOWN),
    ROTATE_Z("rotate_z", Category.CLIPBOARD, KeyCodes.KEY_UNKNOWN),

    MIRROR_X("mirror_x", Category.CLIPBOARD, KeyCodes.KEY_UNKNOWN),
    MIRROR_Y("mirror_y", Category.CLIPBOARD, KeyCodes.KEY_UNKNOWN),
    MIRROR_Z("mirror_z", Category.CLIPBOARD, KeyCodes.KEY_UNKNOWN),

    ;

    private final KeyBinding keyBinding;

    EffortlessKeys(String description, Category category, KeyCodes defaultKey) {

        this.keyBinding = switch (category) {
            case DEFAULT ->  KeyBinding.of("key.effortless.%s.desc".formatted(description), "key.effortless.category.default", defaultKey.value());
            case CLIPBOARD ->  KeyBinding.of("key.effortless.%s.desc".formatted(description), "key.effortless.category.clipboard", defaultKey.value());
        };
    }

    @Override
    public KeyBinding getKeyBinding() {
        return keyBinding;
    }

    public enum Category {
        DEFAULT,
        CLIPBOARD
    }
}
