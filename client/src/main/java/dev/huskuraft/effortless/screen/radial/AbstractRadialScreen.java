package dev.huskuraft.effortless.screen.radial;

import dev.huskuraft.effortless.building.Option;
import dev.huskuraft.effortless.core.AxisDirection;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.Resource;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.math.MathUtils;
import dev.huskuraft.effortless.renderer.Renderer;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

public class AbstractRadialScreen<S, B> extends AbstractScreen {

    private static final float FADE_SPEED = 0.5f;
    private static final int WATERMARK_TEXT_COLOR = 0x8d7f7f7f;
    private static final int DEFAULT_RADIAL_SLOTS = 12;
    private static final ColorState RADIAL_SLOT_COLOR_STATE = new ColorState(
            new Color(0f, 0f, 0f, 0.42f),
            new Color(0f, 0f, 0f, 0.42f),
            new Color(0.24f, 0.24f, 0.24f, 0.50f),
            new Color(0.36f, 0.36f, 0.36f, 0.64f),
            new Color(0.42f, 0.42f, 0.42f, 0.64f)
    );
    private static final ColorState RADIAL_BUTTON_COLOR_STATE = RADIAL_SLOT_COLOR_STATE;
    private static final int WHITE_TEXT_COLOR = 0xFFFFFF;
    private static final double RING_INNER_EDGE = 32;
    private static final double RING_OUTER_EDGE = 67;
    private static final double CATEGORY_LINE_OUTER_EDGE = 36;
    private static final double TEXT_DISTANCE = 84;
    private static final double SECTION_OFFSET_X = 112;
    private static final double SECTION_OFFSET_Y = 0;
    private static final int BUTTON_WIDTH = 22;
    private static final int BUTTON_HEIGHT = 22;
    private static final double BUTTON_OFFSET_X = 26;
    private static final double BUTTON_OFFSET_Y = 26;
    private static final double TITLE_HEIGHT = 10;
    private static final int MIN_RADIAL_SIZE = 8;
    private static final float MOUSE_SCROLL_THRESHOLD = 1;
    private Consumer<Slot<S>> radialSelectResponder;
    private Consumer<Slot<S>> radialSwipeResponder;
    private Consumer<Button<B>> radialOptionSelectResponder;
    private List<? extends Slot<S>> radialSlots = List.of();
    private List<? extends ButtonSet<B>> leftButtons = List.of();
    private List<? extends ButtonSet<B>> rightButtons = List.of();
    private Slot<S> hoveredSlot;
    private Button<B> hoveredButton;
    private Collection<? extends Slot<S>> selectedSlot = new HashSet<>();
    private Collection<? extends Button<B>> selectedButton = new HashSet<>();
    private float lastScrollOffset = 0;
    // TODO: 20/2/23 rename
    private float visibility = 1;

    public AbstractRadialScreen(Entrance entrance, Text text) {
        super(entrance, text);
    }

    protected static <T> Slot<T> slot(Object id, Text name, Resource icon, Color tintColor, T content) {
        return new Slot<>() {

            @Override
            public Object getId() {
                return id;
            }

            @Override
            public Text getDisplayName() {
                return name;
            }

            @Override
            public Text getDisplayCategory() {
                return null;
            }

            @Override
            public Resource getIcon() {
                return icon;
            }

            @Override
            public Color getTintColor() {
                return tintColor;
            }

            @Override
            public T getContent() {
                return content;
            }

        };
    }

    protected static <T> Button<T> button(Object id, Text name, Text category, Resource icon, T content) {
        return new Button<>() {

            @Override
            public Object getId() {
                return id;
            }

            @Override
            public Text getDisplayName() {
                return name;
            }

            @Override
            public Text getDisplayCategory() {
                return category;
            }

            @Override
            public Resource getIcon() {
                return icon;
            }

            @Override
            public Color getTintColor() {
                return null;
            }

            @Override
            public T getContent() {
                return content;
            }

        };
    }

    @SafeVarargs
    protected static <T> ButtonSet<T> buttonSet(Button<T>... entries) {
        return buttonSet(List.of(entries));
    }

    protected static <T> ButtonSet<T> buttonSet(List<? extends Button<T>> entries) {
        return new ButtonSet<>() {
            @Override
            public Text getDisplayName() {
                return null;
            }

            @Override
            public List<? extends Button<T>> getButtons() {
                return entries;
            }
        };
    }

    public static <T extends Option> Button<T> button(T option) {
        return button(
                option,
                option.getDisplayName(),
                option.getDisplayCategory(),
                option.getIcon(),
                option);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public boolean isPauseGame() {
        return true;
    }

    @Override
    public void renderWidget(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidget(renderer, mouseX, mouseY, deltaTick);

        hoveredSlot = null;
        hoveredButton = null;

        renderRadialButtonSets(renderer, mouseX, mouseY, leftButtons, AxisDirection.NEGATIVE);
        renderRadialButtonSets(renderer, mouseX, mouseY, rightButtons, AxisDirection.POSITIVE);
        renderRadialSlots(renderer, mouseX, mouseY, radialSlots);

    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        var result = false;
        if (isActive() && isVisible()) {
            if (radialSelectResponder != null && hoveredSlot != null) {
                radialSelectResponder.accept(hoveredSlot);
                result = true;
            }

            if (radialOptionSelectResponder != null && hoveredButton != null) {
                radialOptionSelectResponder.accept(hoveredButton);
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean onMouseScrolled(double mouseX, double mouseY, double amountX, double amountY) {
        var sign = lastScrollOffset * amountY;
        if (sign < 0) {
            lastScrollOffset = 0;
        }
        lastScrollOffset += amountY;
        if (lastScrollOffset > MOUSE_SCROLL_THRESHOLD) {
            cycleBuildMode(getEntrance().getClient().getPlayer(), true);
            lastScrollOffset = 0;
        } else if (lastScrollOffset < -MOUSE_SCROLL_THRESHOLD) {
            cycleBuildMode(getEntrance().getClient().getPlayer(), false);
            lastScrollOffset = 0;
        }
        return true;
    }

    public final void setRadialSelectResponder(Consumer<Slot<S>> consumer) {
        this.radialSelectResponder = consumer;
    }

    public final void setRadialSwipeResponder(Consumer<Slot<S>> consumer) {
        this.radialSwipeResponder = consumer;
    }

    public final void setRadialOptionSelectResponder(Consumer<Button<B>> consumer) {
        this.radialOptionSelectResponder = consumer;
    }

    public final void setRadialSlots(List<Slot<S>> slots) {
        this.radialSlots = slots;
    }

    @SafeVarargs
    public final void setLeftButtons(ButtonSet<B>... options) {
        this.leftButtons = List.of(options);
    }

    public final void setLeftButtons(List<? extends ButtonSet<B>> options) {
        this.leftButtons = options;
    }

    @SafeVarargs
    public final void setRightButtons(ButtonSet<B>... options) {
        this.rightButtons = List.of(options);
    }

    public final void setRightButtons(List<? extends ButtonSet<B>> options) {
        this.rightButtons = options;
    }

    @SafeVarargs
    public final void setSelectedSlots(Slot<S>... slots) {
        this.selectedSlot = Set.of(slots);
    }

    public final void setSelectedSlots(Collection<? extends Slot<S>> slots) {
        this.selectedSlot = slots;
    }

    public final void setSelectedButtons(Collection<? extends Button<B>> options) {
        this.selectedButton = options;
    }

    private void renderRadialSlots(Renderer renderer, int mouseX, int mouseY, List<? extends Slot<S>> slots) {

        double middleX = getWidth() / 2.0;
        double middleY = getHeight() / 2.0;

        var mouseCenterX = mouseX - middleX;
        var mouseCenterY = mouseY - middleY;

        var mouseRad = (MathUtils.atan2(mouseCenterY, mouseCenterX) + 2 * MathUtils.PI) % (2 * MathUtils.PI);

        var ringInnerEdge = RING_INNER_EDGE * 0.72f + RING_INNER_EDGE * visibility * 0.28f;
        var ringOuterEdge = RING_OUTER_EDGE * 0.72f + RING_OUTER_EDGE * visibility * 0.28f;
        var categoryOuterEdge = CATEGORY_LINE_OUTER_EDGE * 0.72f + CATEGORY_LINE_OUTER_EDGE * visibility * 0.28f;

        var innerGap = MathUtils.PI * 0.007; // gap between buttons in radians at inner edge
        var outerGap = innerGap * ringInnerEdge / ringOuterEdge; // gap between buttons in radians at outer edge
        var rad = 2.0 * MathUtils.PI / MathUtils.max(MIN_RADIAL_SIZE, slots.size());

        for (int i = 0; i < slots.size(); i++) {

            var slot = slots.get(i);
            var lRad = (i - 0.5) * rad - MathUtils.PI / 2.0;
            var rRad = (i + 0.5) * rad - MathUtils.PI / 2.0;

            var x1 = MathUtils.cos(lRad);
            var x2 = MathUtils.cos(rRad);
            var y1 = MathUtils.sin(lRad);
            var y2 = MathUtils.sin(rRad);

            var x1m1 = MathUtils.cos(lRad + innerGap) * ringInnerEdge;
            var x2m1 = MathUtils.cos(rRad - innerGap) * ringInnerEdge;
            var y1m1 = MathUtils.sin(lRad + innerGap) * ringInnerEdge;
            var y2m1 = MathUtils.sin(rRad - innerGap) * ringInnerEdge;

            var x1m2 = MathUtils.cos(lRad + outerGap) * ringOuterEdge;
            var x2m2 = MathUtils.cos(rRad - outerGap) * ringOuterEdge;
            var y1m2 = MathUtils.sin(lRad + outerGap) * ringOuterEdge;
            var y2m2 = MathUtils.sin(rRad - outerGap) * ringOuterEdge;

            var isActivated = selectedSlot.stream().anyMatch(obj -> Objects.equals(obj.getId(), slot.getId()));
            var isMouseInQuad = inTriangle(x1m1, y1m1, x2m2, y2m2, x2m1, y2m1, mouseCenterX, mouseCenterY) || inTriangle(x1m1, y1m1, x1m2, y1m2, x2m2, y2m2, mouseCenterX, mouseCenterY);
            var isHovered = (lRad <= mouseRad && mouseRad <= rRad || lRad <= mouseRad - 2 * MathUtils.PI && mouseRad - 2 * MathUtils.PI <= rRad) && isMouseInQuad;

            var color = RADIAL_SLOT_COLOR_STATE.defaultColor();
            if (isActivated) color = RADIAL_SLOT_COLOR_STATE.activedColor();
            if (isHovered) color = RADIAL_SLOT_COLOR_STATE.hoveredColor();
            if (isActivated && isHovered) color = RADIAL_SLOT_COLOR_STATE.activedHoveredColor();

            if (isHovered) {
                hoveredSlot = slot;
                var x = (x1 + x2) * 0.5;
                var y = (y1 + y2) * 0.5;

                int textX = (int) (x * TEXT_DISTANCE);
                int textY = (int) (y * TEXT_DISTANCE) - getTypeface().getLineHeight() / 2;

                var text = slot.getDisplayName();

                if (x <= -0.2) {
                    textX -= getTypeface().measureWidth(text);
                } else if (-0.2 <= x && x <= 0.2) {
                    textX -= getTypeface().measureWidth(text) / 2;
                }
                // FIXME: 27/9/23
                renderer.renderTextFromStart(getTypeface(), text, (int) middleX + textX, (int) middleY + textY, WHITE_TEXT_COLOR, true);
            }

            // background tint
            renderer.renderQuad((int) (middleX + x1m1), (int) (middleY + y1m1), (int) (middleX + x2m1), (int) (middleY + y2m1), (int) (middleX + x2m2), (int) (middleY + y2m2), (int) (middleX + x1m2), (int) (middleY + y1m2), 100, color.getRGB());

            // category line
            color = slot.getTintColor();

            var x1m3 = MathUtils.cos(lRad + innerGap) * categoryOuterEdge;
            var x2m3 = MathUtils.cos(rRad - innerGap) * categoryOuterEdge;
            var y1m3 = MathUtils.sin(lRad + innerGap) * categoryOuterEdge;
            var y2m3 = MathUtils.sin(rRad - innerGap) * categoryOuterEdge;

            renderer.renderQuad((int) (middleX + x1m1), (int) (middleY + y1m1), (int) (middleX + x2m1), (int) (middleY + y2m1), (int) (middleX + x2m3), (int) (middleY + y2m3), (int) (middleX + x1m3), (int) (middleY + y1m3), 200, color.getRGB());
            renderer.flush();

            // icon
            if (slot.getIcon() != null) {
                var iconX = (x1 + x2) * 0.5 * (ringOuterEdge * 0.55 + 0.45 * ringInnerEdge);
                var iconY = (y1 + y2) * 0.5 * (ringOuterEdge * 0.55 + 0.45 * ringInnerEdge);

                renderer.pushPose();
                renderer.translate(0, 0, 300);
                renderer.renderTexture(slot.getIcon(), (int) MathUtils.round(middleX + iconX - 8), (int) MathUtils.round(middleY + iconY - 8), 16, 16, 0f, 0f, 18, 18, 18, 18);
                renderer.popPose();
            }
            renderer.flush();

        }

    }

    private void renderRadialButtonSets(Renderer renderer, int mouseX, int mouseY, List<? extends ButtonSet<B>> buttonSets, AxisDirection direction) {

        double middleX = getWidth() / 2.0;
        double middleY = getHeight() / 2.0;

        var mouseCenterX = mouseX - middleX;
        var mouseCenterY = mouseY - middleY;

        for (var row = 0; row < buttonSets.size(); row++) {
            var buttonSet = buttonSets.get(row);
            var buttons = buttonSet.getButtons();

            for (int col = 0; col < buttons.size(); col++) {
                var button = buttons.get(col);

                var x = (SECTION_OFFSET_X + BUTTON_OFFSET_X * col) * direction.getStep();
                var y = (SECTION_OFFSET_Y + BUTTON_OFFSET_Y * (row - (buttonSets.size() - 1) / 2f)) * 1;

                var x1 = x - BUTTON_WIDTH / 2d;
                var y1 = y - BUTTON_HEIGHT / 2d;

                var x2 = x + BUTTON_WIDTH / 2d;
                var y2 = y + BUTTON_HEIGHT / 2d;

                var isActivated = selectedButton != null && selectedButton.stream().anyMatch(b -> Objects.equals(b.getId(), button.getId()));
                var isHovered = x1 <= mouseCenterX && x2 >= mouseCenterX && y1 <= mouseCenterY && y2 >= mouseCenterY;

                var color = RADIAL_BUTTON_COLOR_STATE.defaultColor();
                if (isActivated) color = RADIAL_BUTTON_COLOR_STATE.activedColor();
                if (isHovered) color = RADIAL_BUTTON_COLOR_STATE.hoveredColor();
                if (isActivated && isHovered) color = RADIAL_BUTTON_COLOR_STATE.activedHoveredColor();

                if (isHovered) {
                    hoveredButton = button;
                }

                renderer.renderRect(renderer.renderTextures().gui(), (int) (middleX + x1), (int) (middleY + y1), (int) (middleX + x2), (int) (middleY + y2), color.getRGB(), 0);
                renderer.flush();
                // icon
                if (button.getIcon() != null) {
                    renderer.pushPose();
                    var iconX = x;
                    var iconY = y;
                    renderer.translate((int) MathUtils.round(middleX + iconX - 8), (int) MathUtils.round(middleY + iconY - 8), 0);
                    renderer.renderTexture(button.getIcon(), 0, 0, 16, 16, 0f, 0f, 18, 18, 18, 18);
                    renderer.popPose();
                }
                renderer.flush();

            }
        }
    }

    @Override
    public void renderWidgetOverlay(Renderer renderer, int mouseX, int mouseY, float deltaTick) {
        super.renderWidgetOverlay(renderer, mouseX, mouseY, deltaTick);

        if (hoveredButton != null) {
            renderer.renderTooltip(
                    getTypeface(),
                    List.of(
                            hoveredButton.getDisplayCategory().withStyle(TextStyle.WHITE),
                            hoveredButton.getDisplayName().withStyle(TextStyle.GOLD)
                    ), mouseX, mouseY);
        }
    }

    private boolean inTriangle(double x1, double y1, double x2, double y2,
                               double x3, double y3, double x, double y) {
        var ab = (x1 - x) * (y2 - y) - (x2 - x) * (y1 - y);
        var bc = (x2 - x) * (y3 - y) - (x3 - x) * (y2 - y);
        var ca = (x3 - x) * (y1 - y) - (x1 - x) * (y3 - y);
        return MathUtils.sign(ab) == MathUtils.sign(bc) && MathUtils.sign(bc) == MathUtils.sign(ca);
    }

    private void cycleBuildMode(Player player, boolean reverse) {
        // TODO: 23/5/23
//        setBuildMode(player, BuildMode.values()[(getBuildMode(player).ordinal() + 1) % BuildMode.values().length]);
//        Constructor.getInstance().reset(player);
    }

    private void playRadialMenuSound() {
        getEntrance().getClient().playButtonClickSound();
    }

    public interface Slot<T> {

        Object getId();

        Text getDisplayName();

        Text getDisplayCategory();

        Resource getIcon();

        Color getTintColor();

        T getContent();

    }

    public interface Button<T> {

        Object getId();

        Text getDisplayName();

        Text getDisplayCategory();

        Resource getIcon();

        Color getTintColor();

        T getContent();

    }

    public interface ButtonSet<T> {

        Text getDisplayName();

        List<? extends Button<T>> getButtons();

    }

    record ColorState(
            Color disabledColor,
            Color defaultColor,
            Color hoveredColor,
            Color activedColor,
            Color activedHoveredColor
    ) {
    }

}

