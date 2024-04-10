package dev.huskuraft.effortless.api.gui.slot;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public class TextSlot extends Slot {

    private final Text symbol;

    public TextSlot(Entrance entrance, int x, int y, int width, int height, Text message) {
        this(entrance, x, y, width, height, message, null);
    }

    public TextSlot(Entrance entrance, int x, int y, int width, int height, Text message, Text symbol) {
        super(entrance, x, y, width, height, message);
        this.symbol = symbol == null ? null : Text.text(symbol.getString().substring(0, Math.min(symbol.getString().length(), 1)).toUpperCase());
    }

    public Text getSymbol() {
        return symbol;
    }

    public int getFullWidth() {
        if (getMessage() == null) {
            return getWidth();
        }
        return Math.max(getTypeface().measureWidth(getMessage()) + 1, getWidth());
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        renderer.pushPose();
        renderer.translate(getFullWidth() - getWidth(), 0, 0);

        renderer.renderRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x9f6c6c6c);

        if (symbol != null) {
            renderer.enableScissor(getX() + getFullWidth() - getWidth(), getY(), getX() + getFullWidth(), getY() + getHeight());

            renderer.pushPose();
            renderer.translate(getX(), getY(), 0);
            renderer.scale(getWidth() / 18f, getHeight() / 18f, 0);
            renderer.translate(18f / 2 + 0.5f, 18f / 2 + 1f, 0);
            renderer.pushPose();
            renderer.scale(18f / getTypeface().getLineHeight(), 18f / getTypeface().getLineHeight(), 0);
            renderer.translate(0, -getTypeface().measureHeight(getSymbol()) / 2f, 0);
            renderer.renderTextFromCenter(getTypeface(), getSymbol(), 0, 0, 0xFF6C6C6C, false);
            renderer.popPose();
            renderer.popPose();
            renderer.disableScissor();
        }

        if (getMessage() != null) {
            renderer.pushPose();
            renderer.translate(getX(), getY(), 0);
            renderer.scale(getWidth() / 18f, getHeight() / 18f, 0);
            renderer.translate(18f - 1, 18f, 0);
            renderer.pushPose();
            renderer.translate(0, -getTypeface().measureHeight(getMessage()), 0);
            renderer.renderTextFromEnd(getTypeface(), getMessage(), 0, 0, 0xFFFFFFFF, true);
            renderer.popPose();
            renderer.popPose();
        }
        renderer.popPose();

    }
}
