package dev.huskuraft.effortless.gui;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public abstract class AbstractButton extends AbstractWidget {

    protected AbstractButton(Entrance entrance, int x, int y, int width, int height, Text message) {
        super(entrance, x, y, width, height, message);
    }

    protected abstract void onPress();

    public void onClick(double d, double e) {
        onPress();
    }

    public void onRelease(double d, double e) {
    }

    public void onDrag(double d, double e, double f, double g) {
    }

    protected boolean isClickable(double d, double e) {
        return isMouseOver(d, e);
    }

    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (isActive() && isVisible()) {
            if (keyCode != 257 && keyCode != 32 && keyCode != 335) {
                return false;
            } else {
                playDownSound();
                onPress();
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);
        renderButtonBackground(renderer, mouseX, mouseY, deltaTick);
    }

    public void renderButtonBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        renderer.setShaderColor(1.0F, 1.0F, 1.0F, this.getAlpha());
        renderer.drawWidgetBackgroundTexture(getX(), getY(), getWidth(), getHeight(), isActive(), isHoveredOrFocused());
        renderer.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderString(renderer, getTypeface(), (isActive() ? 16777215 : 10526880) | (int) MathUtils.ceil(this.getAlpha() * 255.0F) << 24);
    }

    public void renderString(Renderer renderer, Typeface typeface, int i) {
        this.renderScrollingString(renderer, typeface, 2, i);
    }

    protected void renderScrollingString(Renderer renderer, Typeface typeface, int x, int y) {
        int k = this.getX() + x;
        int l = this.getX() + this.getWidth() - x;
        renderer.drawScrollingText(typeface, this.getMessage(), k, this.getY(), l, this.getY() + this.getHeight(), y);
    }


    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (super.onMouseClicked(mouseX, mouseY, button) && isActive() && isVisible() && isMouseKeyValid(button)) {
            boolean clickable = isClickable(mouseX, mouseY);
            if (clickable) {
                playDownSound();
                onClick(mouseX, mouseY);
                return true;
            }
        }
        return false;
    }

    public boolean onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isMouseKeyValid(button)) {
            onDrag(mouseX, mouseY, deltaX, deltaY);
            return true;
        } else {
            return false;
        }
    }

    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        if (isMouseKeyValid(button)) {
            onRelease(mouseX, mouseY);
            return true;
        } else {
            return false;
        }
    }

    protected boolean isMouseKeyValid(int i) {
        return i == 0;
    }

    public void playDownSound() {
        getEntrance().getClient().playButtonClickSound();
    }

}
