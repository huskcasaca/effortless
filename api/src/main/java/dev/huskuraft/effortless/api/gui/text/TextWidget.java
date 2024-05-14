package dev.huskuraft.effortless.api.gui.text;

import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public class TextWidget extends AbstractWidget {

    private int color = 0xFFFFFF;
    private Gravity gravity = Gravity.START;

    public TextWidget(Entrance entrance, int x, int y, Text message) {
        super(entrance, x, y, 0, 0, message);
    }

    public TextWidget(Entrance entrance, int x, int y, Text message, Gravity gravity) {
        super(entrance, x, y, 0, 0, message);
        this.gravity = gravity;
    }

    public TextWidget(Entrance entrance, int x, int y, int width, int height, Text message, Gravity gravity) {
        super(entrance, x, y, width, height, message);
        this.gravity = gravity;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    @Override
    public int getWidth() {
        return super.getWidth() == 0 ? getTypeface().measureWidth(getMessage()) : super.getWidth();
    }

    @Override
    public int getHeight() {
        return getTypeface().measureHeight(getMessage());
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        if (getTypeface().measureWidth(getMessage()) > getWidth()) {

            switch (gravity) {
                case START -> renderer.renderScrollingText(getTypeface(), getMessage(), getX(), getY(), getWidth(), getHeight());
                case CENTER -> renderer.renderScrollingText(getTypeface(), getMessage(), getX() - getWidth() / 2, getY(), getWidth(), getHeight());
                case END -> renderer.renderScrollingText(getTypeface(), getMessage(), getX() + getWidth() / 2, getY(), getWidth(), getHeight());
            }
            return;
        }

        switch (gravity) {
            case START -> renderer.renderTextFromStart(getTypeface(), getMessage(), getX(), getY(), this.color, false);
            case CENTER -> renderer.renderTextFromCenter(getTypeface(), getMessage(), getX(), getY(), this.color, false);
            case END -> renderer.renderTextFromEnd(getTypeface(), getMessage(), getX(), getY(), this.color, false);
        }
    }

    public enum Gravity {
        START,
        END,
        CENTER
    }

}
