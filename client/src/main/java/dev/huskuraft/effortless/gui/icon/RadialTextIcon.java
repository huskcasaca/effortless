package dev.huskuraft.effortless.gui.icon;

import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;

public class RadialTextIcon extends AbstractWidget {

    private int index;

    public RadialTextIcon(Entrance entrance, int x, int y, int width, int height, int index, Text message) {
        super(entrance, x, y, width, height, message);
        this.index = index;
    }

    private static void renderRadialSlices(Renderer renderer, double middleX, double middleY, double ringInnerEdge, double ringOuterEdge, int selected) {
        var totalModes = MathUtils.max(3, Dimens.CellRing.RADIAL_SIZE);
        var innerGap = MathUtils.PI * 0.04; //gap between buttons in radians at inner edge
        var outerGap = innerGap * ringInnerEdge / ringOuterEdge; //gap between buttons in radians at outer edge
        var rad = 2.0 * MathUtils.PI / totalModes;

        for (int i = 0; i < Dimens.CellRing.RADIAL_SIZE; i++) {
            var begRad = (i - 0.5) * rad - MathUtils.PI / 2.0;
            var endRad = (i + 0.5) * rad - MathUtils.PI / 2.0;

            var x1m1 = MathUtils.cos(begRad + innerGap) * ringInnerEdge;
            var x2m1 = MathUtils.cos(endRad - innerGap) * ringInnerEdge;
            var y1m1 = MathUtils.sin(begRad + innerGap) * ringInnerEdge;
            var y2m1 = MathUtils.sin(endRad - innerGap) * ringInnerEdge;

            var x1m2 = MathUtils.cos(begRad + outerGap) * ringOuterEdge;
            var x2m2 = MathUtils.cos(endRad - outerGap) * ringOuterEdge;
            var y1m2 = MathUtils.sin(begRad + outerGap) * ringOuterEdge;
            var y2m2 = MathUtils.sin(endRad - outerGap) * ringOuterEdge;

            var color = Dimens.CellRing.RADIAL_COLOR;
            if (selected % Dimens.CellRing.RADIAL_SIZE == i) color = Dimens.CellRing.HIGHLIGHT_COLOR;

            renderer.drawQuad((int) (middleX + x1m1), (int) (middleY + y1m1), (int) (middleX + x2m1), (int) (middleY + y2m1), (int) (middleX + x2m2), (int) (middleY + y2m2), (int) (middleX + x1m2), (int) (middleY + y1m2), 0, color.getRGB());

        }
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        renderer.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x9f6c6c6c);
        renderRadialSlices(renderer, getX() + getWidth() / 2, getY() + getHeight() / 2, Dimens.CellRing.RING_INNER_EDGE, Dimens.CellRing.RING_OUTER_EDGE, index + 1);

        renderer.pushPose();
        renderer.translate(getX() + getWidth() / 2f + 1, getY() + getHeight() / 2f - 8, 0);
        renderer.scale(2, 2, 0);
        renderer.drawTextFromCenter(getTypeface(), getMessage(), 0, 0, 0xFFFFFFFF, true);
        renderer.popPose();
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
