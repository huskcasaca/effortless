package dev.ftb.mods.ftbteams.api;

import dev.ftb.mods.ftblibrary.ui.input.MouseButton;

@FunctionalInterface
@Deprecated
public interface CustomPartyCreationHandler {
    void createParty(MouseButton button);
}
