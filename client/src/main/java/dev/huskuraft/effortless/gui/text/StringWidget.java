package dev.huskuraft.effortless.gui.text;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class StringWidget extends AbstractWidget {

    private int color;

    public StringWidget(Entrance entrance, Text message) {
        super(entrance, 0, 0, 0, 0, message);
        this.color = 16777215;
    }

    public StringWidget(Entrance entrance, int x, int y, Text message) {
        super(entrance, x, y, 0, 0, message);
        this.color = 16777215;
    }

    public void setColor(int color) {
        this.color = color;
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

        renderer.drawText(getTypeface(), getMessage(), getX(), getY(), this.color, true); // drawShadow
    }

}
