package dev.huskuraft.effortless.api.input;

import dev.huskuraft.effortless.api.platform.ClientEntrance;

public enum OptionKeys implements Key {
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
        var options = ClientEntrance.getInstance().getClient().getOptions();
        return switch (this) {
            case KEY_UP -> options.keyUp();
            case KEY_LEFT -> options.keyLeft();
            case KEY_DOWN -> options.keyDown();
            case KEY_RIGHT -> options.keyRight();
            case KEY_JUMP -> options.keyJump();
            case KEY_SHIFT -> options.keyShift();
            case KEY_SPRINT -> options.keySprint();
            case KEY_INVENTORY -> options.keyInventory();
            case KEY_SWAP_OFFHAND -> options.keySwapOffhand();
            case KEY_DROP -> options.keyDrop();
            case KEY_USE -> options.keyUse();
            case KEY_ATTACK -> options.keyAttack();
            case KEY_PICK_ITEM -> options.keyPickItem();
            case KEY_CHAT -> options.keyChat();
            case KEY_PLAYER_LIST -> options.keyPlayerList();
            case KEY_COMMAND -> options.keyCommand();
            case KEY_SOCIAL_INTERACTIONS -> options.keySocialInteractions();
            case KEY_SCREENSHOT -> options.keyScreenshot();
            case KEY_TOGGLE_PERSPECTIVE -> options.keyTogglePerspective();
            case KEY_SMOOTH_CAMERA -> options.keySmoothCamera();
            case KEY_FULLSCREEN -> options.keyFullscreen();
            case KEY_SPECTATOR_OUTLINES -> options.keySpectatorOutlines();
            case KEY_ADVANCEMENTS -> options.keyAdvancements();
            case KEY_HOTBAR_SLOTS_1 -> options.keyHotbarSlots()[0];
            case KEY_HOTBAR_SLOTS_2 -> options.keyHotbarSlots()[1];
            case KEY_HOTBAR_SLOTS_3 -> options.keyHotbarSlots()[2];
            case KEY_HOTBAR_SLOTS_4 -> options.keyHotbarSlots()[3];
            case KEY_HOTBAR_SLOTS_5 -> options.keyHotbarSlots()[4];
            case KEY_HOTBAR_SLOTS_6 -> options.keyHotbarSlots()[5];
            case KEY_HOTBAR_SLOTS_7 -> options.keyHotbarSlots()[6];
            case KEY_HOTBAR_SLOTS_8 -> options.keyHotbarSlots()[7];
            case KEY_HOTBAR_SLOTS_9 -> options.keyHotbarSlots()[8];
            case KEY_SAVE_HOTBAR_ACTIVATOR -> options.keySaveHotbarActivator();
            case KEY_LOAD_HOTBAR_ACTIVATOR -> options.keyLoadHotbarActivator();
        };
    }

}
