package dev.huskuraft.effortless.api.gui.text;

import dev.huskuraft.effortless.api.core.Entrance;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

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

    public void setColor(int color) {
        this.color = color;
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    @Override
    public int getWidth() {
        return getTypeface().measureWidth(getMessage());
    }

    @Override
    public int getHeight() {
        return getTypeface().measureHeight(getMessage());
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);
        switch (gravity) {
            case START -> renderer.renderTextFromStart(getTypeface(), getMessage(), getX(), getY(), this.color, true);
            case CENTER -> renderer.renderTextFromCenter(getTypeface(), getMessage(), getX(), getY(), this.color, true);
            case END -> renderer.renderTextFromEnd(getTypeface(), getMessage(), getX(), getY(), this.color, true);
        }
    }

    public enum Gravity {
        START,
        END,
        CENTER
    }

}
