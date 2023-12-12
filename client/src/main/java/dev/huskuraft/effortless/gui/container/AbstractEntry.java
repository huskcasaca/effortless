package dev.huskuraft.effortless.gui.container;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractContainerWidget;
import dev.huskuraft.effortless.gui.Entry;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public abstract class AbstractEntry extends AbstractContainerWidget implements Entry {

    protected AbstractEntry(Entrance entrance) {
        super(entrance, 0, 0, 0, 0, Text.empty());
    }

    @Override
    public void onPositionChange(int from, int to) {

    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onDeselected() {

    }

    @Deprecated
    public Text getNarration() {
        return Text.empty();
    }

    @Override
    public void render(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        renderer.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        super.render(renderer, mouseX, mouseY, deltaTick);
        renderer.disableScissor();
    }
}
