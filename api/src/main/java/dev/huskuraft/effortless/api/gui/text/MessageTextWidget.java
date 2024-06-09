package dev.huskuraft.effortless.api.gui.text;

import java.util.List;

import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public class MessageTextWidget extends AbstractWidget {

    private int color = 0xFFFFFF;
    private final List<Text> texts;
    private Gravity gravity = Gravity.START;


    public MessageTextWidget(Entrance entrance, int x, int y, int width, int height, Text message, Gravity gravity) {
        super(entrance, x, y, width, height, message);
        this.texts = TooltipHelper.wrapLines(getTypeface(), message, width);
        this.gravity = gravity;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    public int getTextWidth() {
        return super.getWidth() == 0 ? getTypeface().measureWidth(getMessage()) : super.getWidth();
    }

    public int getTextHeight() {
        return texts.stream().mapToInt(getTypeface()::measureHeight).sum();
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        var newY = getY() + (getHeight() - getTextHeight()) / 2;
        for (var text : texts) {
            var textWidth = getTypeface().measureWidth(text);
            var newX = getX() + (getWidth() - textWidth) / 2;
            renderer.renderText(getTypeface(), text, newX, newY, this.color, false);
            newY += getTypeface().measureHeight(text);
        }

    }

    @Override
    public void renderWidgetBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        renderer.renderGradientRect(getLeft(), getTop(), getRight(), getBottom(), 0xff000000, 0xff000000);
    }

    public enum Gravity {
        START,
        END,
        CENTER
    }

}
