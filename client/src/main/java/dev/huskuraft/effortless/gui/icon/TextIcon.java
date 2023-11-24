package dev.huskuraft.effortless.gui.icon;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class TextIcon extends AbstractWidget {

    public TextIcon(Entrance entrance, int x, int y, Text message) {
        this(entrance, x, y, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, message);
    }

    public TextIcon(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        renderer.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x9f6c6c6c);

        renderer.pushPose();
        renderer.translate(getX() + getWidth() / 2f + 1, getY() + getHeight() / 2f - 8, 0);
        renderer.scale(2, 2, 0);
        renderer.drawTextFromCenter(getTypeface(), getMessage(), 0, 0, 0xFFFFFFFF, true);
        renderer.popPose();
    }

}
