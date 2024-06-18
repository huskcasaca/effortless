package dev.huskuraft.effortless.api.gui.button;

import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.renderer.Renderer;
import dev.huskuraft.effortless.api.text.Text;

public class Checkbox extends Button {

    private static final int CHECKBOX_SIZE = 11;

    private boolean isChecked;

    public Checkbox(Entrance entrance, int x, int y, String name, boolean isChecked) {
        super(entrance, x, y, 0, 0, Text.text(name), b -> {
        });
        this.isChecked = isChecked;
    }

    @Override
    public int getWidth() {
        return getTypeface().measureWidth(getMessage()) + 2 + getCheckboxSize();
    }

    @Override
    public int getHeight() {
        return getCheckboxSize();
    }

    public int getCheckboxSize() {
        return CHECKBOX_SIZE;
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);
        // TODO: 8/10/23
//        this.setHovered(mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.boxWidth && mouseY < this.getY() + this.getHeight());
        // TODO: 27/9/23 render
//            ScreenUtils.blitWithBorder(renderer, AbstractButton.WIDGETS_LOCATION, this.getX(), this.getY(), 0, 46, this.boxWidth, this.getHeight(), 200, 20, 2, 3, 2, 2, this.getBlitOffset());
        var color = 14737632;
        var packedFGColor = 0;
        // FIXME: 8/9/22
        if (packedFGColor != 0) {
            color = packedFGColor;
        } else if (!this.isActive()) {
            color = 10526880;
        }

        if (this.isChecked)
            renderer.renderTextFromCenter(getTypeface(), "x", this.getX() + this.getCheckboxSize() / 2 + 1, this.getY() + 1, 14737632, true);

        renderer.renderTextFromStart(getTypeface(), getMessage(), this.getX() + this.getCheckboxSize() + 2, this.getY() + 2, color, true);
    }

    @Override
    public void onPress() {
        this.isChecked = !this.isChecked;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
