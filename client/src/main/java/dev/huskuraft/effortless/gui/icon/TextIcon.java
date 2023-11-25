package dev.huskuraft.effortless.gui.icon;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class TextIcon extends AbstractWidget {

    public TextIcon(Entrance entrance, int x, int y, Text message) {
        this(entrance, x, y, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, message);
    }

    public TextIcon(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        renderer.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x9f6c6c6c);

        renderer.pushPose();
        renderer.translate(getX(), getY(), 0);
        renderer.scale(getWidth() * 1f / Dimens.SLOT_WIDTH, getHeight() * 1f / Dimens.SLOT_HEIGHT, 0);
        renderer.drawTextFromCenter(getTypeface(), getMessage(), 8, 8 - getTypeface().measureHeight(getMessage()) / 2, 0xFFFFFFFF, true);
        renderer.popPose();
    }

}
