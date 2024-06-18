package dev.huskuraft.effortless.api.gui;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.math.MathUtils;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.api.texture.TextureFactory;

public abstract class AbstractButton extends AbstractWidget {

    private final ResourceLocation icon;

    protected AbstractButton(Entrance entrance, int x, int y, int width, int height, Text message, ResourceLocation icon) {
        super(entrance, x, y, width, height, message);
        this.icon = icon;
        this.focusable = true;
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
        if (icon == null) {
            renderScrollingString(renderer, getTypeface(), 2, (isActive() ? 16777215 : 10526880) | (int) MathUtils.ceil(this.getAlpha() * 255.0F) << 24);
        } else {
            renderIconScrollingString(renderer, getTypeface(), 2, (isActive() ? 16777215 : 10526880) | (int) MathUtils.ceil(this.getAlpha() * 255.0F) << 24);
            if (!isActive()) {
                renderer.setRsShaderColor(0.72f, 0.72f ,0.72f , 0.75f);
                renderIcon(renderer);
                renderer.setRsShaderColor(1f, 1f, 1f, 1f);
            } else {
                renderIcon(renderer);
            }
        }
    }

    public void renderButtonBackground(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        renderer.renderSprite(TextureFactory.getInstance().getButtonTextureSprite(isActive(), isHoveredOrFocused()), getX(), getY(), getWidth(), getHeight());
    }

    protected void renderScrollingString(Renderer renderer, Typeface typeface, int padding, int color) {
        var left = this.getX() + padding;
        var right = this.getX() + this.getWidth() - padding;
        var message = isActive() ? this.getMessage() : this.getMessage().withStyle(ChatFormatting.RESET);
        renderer.renderScrollingText(typeface, message, left, this.getY() + (getHeight() - getTypeface().measureHeight(message)) / 2 + 1, right, this.getY() + this.getHeight(), color);
    }

    protected void renderIcon(Renderer renderer) {
        var left = -(Math.min(getTypeface().measureWidth(getMessage()), getWidth() - 18) + 18) / 2f + getWidth() / 2f + getX() + 1;
        var top = getTop() + 2;
        renderer.renderTexture(icon, (int) left, (int) top, 16, 16, 0f, 0f, 16, 16, 16, 16);
    }

    protected void renderIconScrollingString(Renderer renderer, Typeface typeface, int padding, int color) {
        var left = this.getX() + padding + 18;
        var right = this.getX() + this.getWidth() - padding;
        var message = isActive() ? this.getMessage() : this.getMessage().withStyle(ChatFormatting.RESET);
        renderer.renderScrollingText(typeface, message, left, this.getY() + (getHeight() - getTypeface().measureHeight(message)) / 2 + 1, right, this.getY() + this.getHeight(), color);
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
        getEntrance().getClient().getSoundManager().playButtonClickSound();
    }

}
