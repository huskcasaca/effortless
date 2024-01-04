package dev.huskuraft.effortless.api.gui;

public interface InputHandler {

    long DOUBLE_CLICK_THRESHOLD_MS = 250L;

    boolean isMouseOver(double mouseX, double mouseY);

    boolean onMouseMoved(double mouseX, double mouseY);

    boolean onMouseClicked(double mouseX, double mouseY, int button);

    boolean onMouseReleased(double mouseX, double mouseY, int button);

    boolean onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY);

    boolean onMouseScrolled(double mouseX, double mouseY, double amountX, double amountY);

    boolean onKeyPressed(int keyCode, int scanCode, int modifiers);

    boolean onKeyReleased(int keyCode, int scanCode, int modifiers);

    boolean onCharTyped(char character, int modifiers);

    boolean onFocusMove(boolean forwards);

}
