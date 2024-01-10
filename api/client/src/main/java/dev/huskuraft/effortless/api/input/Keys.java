package dev.huskuraft.effortless.api.input;

import dev.huskuraft.effortless.api.platform.ClientEntrance;

public enum Keys implements Key {
    KEY_UP,
    KEY_LEFT,
    KEY_DOWN,
    KEY_RIGHT,
    KEY_JUMP,
    KEY_SHIFT,
    KEY_SPRINT,
    KEY_INVENTORY,
    KEY_SWAP_OFFHAND,
    KEY_DROP,
    KEY_USE,
    KEY_ATTACK,
    KEY_PICK_ITEM,
    KEY_CHAT,
    KEY_PLAYER_LIST,
    KEY_COMMAND,
    KEY_SOCIAL_INTERACTIONS,
    KEY_SCREENSHOT,
    KEY_TOGGLE_PERSPECTIVE,
    KEY_SMOOTH_CAMERA,
    KEY_FULLSCREEN,
    KEY_SPECTATOR_OUTLINES,
    KEY_ADVANCEMENTS,
    KEY_HOTBAR_SLOTS_1,
    KEY_HOTBAR_SLOTS_2,
    KEY_HOTBAR_SLOTS_3,
    KEY_HOTBAR_SLOTS_4,
    KEY_HOTBAR_SLOTS_5,
    KEY_HOTBAR_SLOTS_6,
    KEY_HOTBAR_SLOTS_7,
    KEY_HOTBAR_SLOTS_8,
    KEY_HOTBAR_SLOTS_9,
    KEY_SAVE_HOTBAR_ACTIVATOR,
    KEY_LOAD_HOTBAR_ACTIVATOR,
    ;

    @Override
    public KeyBinding getBinding() {
        return ClientEntrance.getInstance().getPlatform().getKeyBinding(this);
    }
}
