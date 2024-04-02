package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.input.KeyBinding;

public interface Options extends PlatformReference {

    KeyBinding keyUp();
    KeyBinding keyLeft();
    KeyBinding keyDown();
    KeyBinding keyRight();
    KeyBinding keyJump();
    KeyBinding keyShift();
    KeyBinding keySprint();
    KeyBinding keyInventory();
    KeyBinding keySwapOffhand();
    KeyBinding keyDrop();
    KeyBinding keyUse();
    KeyBinding keyAttack();
    KeyBinding keyPickItem();
    KeyBinding keyChat();
    KeyBinding keyPlayerList();
    KeyBinding keyCommand();
    KeyBinding keySocialInteractions();
    KeyBinding keyScreenshot();
    KeyBinding keyTogglePerspective();
    KeyBinding keySmoothCamera();
    KeyBinding keyFullscreen();
    KeyBinding keySpectatorOutlines();
    KeyBinding keyAdvancements();
    KeyBinding keySaveHotbarActivator();
    KeyBinding keyLoadHotbarActivator();
    KeyBinding[] keyHotbarSlots();

    int maxRenderDistance();

}
