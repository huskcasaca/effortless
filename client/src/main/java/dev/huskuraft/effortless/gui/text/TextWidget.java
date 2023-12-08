package dev.huskuraft.effortless.gui.text;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class TextWidget extends AbstractWidget {

    private int color = 16777215;
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
            case START -> renderer.drawTextFromStart(getTypeface(), getMessage(), getX(), getY(), this.color, true);
            case CENTER -> renderer.drawTextFromCenter(getTypeface(), getMessage(), getX(), getY(), this.color, true);
            case END -> renderer.drawTextFromEnd(getTypeface(), getMessage(), getX(), getY(), this.color, true);
        }
    }

    public enum Gravity {
        START,
        END,
        CENTER
    }

}
