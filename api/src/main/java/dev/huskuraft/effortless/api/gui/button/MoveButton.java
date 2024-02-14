package dev.huskuraft.effortless.api.gui.button;

import dev.huskuraft.effortless.api.core.Orientation;
import dev.huskuraft.effortless.api.gui.AbstractButton;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public class MoveButton extends AbstractButton {

    // TODO: 12/9/23 remove
    private static final int SELECTION_TOTAL_WIDTH = 32;
    private static final int SELECTION_WIDTH = 8;
    private static final int SELECTION_MOVE_START = (SELECTION_TOTAL_WIDTH - SELECTION_WIDTH) / 2;
    private static final int SELECTION_MOVE_END = (SELECTION_TOTAL_WIDTH + SELECTION_WIDTH) / 2;

    private OnMove listener;
    private Orientation[] orientations;

    public MoveButton(Entrance entrance, OnMove listener, Orientation... orientations) {
        this(entrance, 0, 0, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, listener, orientations);
    }

    public MoveButton(Entrance entrance, int x, int y, OnMove listener, Orientation... orientations) {
        this(entrance, x, y, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, listener, orientations);
    }

    public MoveButton(Entrance entrance, int x, int y, int width, int height, OnMove listener, Orientation... orientations) {
        super(entrance, x, y, width, height, Text.empty());
        this.listener = listener;
        this.orientations = orientations;
    }

    public void setDirections(Orientation... orientations) {
        this.orientations = orientations;
    }

    public void setOnMoveListener(OnMove onMove) {
        this.listener = onMove;
    }


    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

//        int v = mouseX - getX();
//        int w = mouseY - getY();
//
//        if (v > 0 && v < getWidth() && w > 0 && w < getHeight()) {
//            for (Orientation orientation : orientations) {
//                if (orientation == Orientation.UP) {
//                    renderer.drawMoveButtonTexture(getX() - 7, getY(), true, SELECTION_MOVE_START < v && v < SELECTION_MOVE_END && w < 16);
//                }
//                if (orientation == Orientation.DOWN) {
//                    renderer.drawMoveButtonTexture(getX() - 7, getY(), false, SELECTION_MOVE_START < v && v < SELECTION_MOVE_END && w > 16);
//                }
//            }
//        }
    }

    @Override
    public void renderButtonBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        var v = mouseX - getX();
        var w = mouseY - getY();
        if (v > 0 && v < getWidth() && w > 0 && w < getHeight()) {
            if (SELECTION_MOVE_START < v && v < SELECTION_MOVE_END && w < 16) { // move down
                listener.move(Orientation.UP);
                return true;
            }
            if (SELECTION_MOVE_START < v && v < SELECTION_MOVE_END && w > 16) { // move up
                listener.move(Orientation.DOWN);
                return true;
            }
        }
        return true;
    }

    @Override
    public void onPress() {

    }

    @FunctionalInterface
    public interface OnMove {
        void move(Orientation orientation);
    }
}
