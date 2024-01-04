package dev.huskuraft.effortless.api.gui.icon;

import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.text.Text;

public class TextIcon extends AbstractWidget {

    public TextIcon(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        renderer.renderRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x9f6c6c6c);

        renderer.pushPose();
        renderer.translate(getX(), getY(), 0);
        renderer.scale(getWidth() / 18f, getHeight() / 18f, 0);
        renderer.translate(18f / 2 + 0.5f, 18f / 2 + 0.5f, 0);
        renderer.pushPose();
        var factor = Math.min(14f / getTypeface().measureWidth(getMessage()), 1f);
        renderer.scale(factor, factor, 0);
        renderer.translate(0, -getTypeface().measureHeight(getMessage()) / 2f, 0);
        renderer.renderTextFromCenter(getTypeface(), getMessage(), 0, 0, 0xFFFFFFFF, false);
        renderer.popPose();
        renderer.popPose();

    }
}
